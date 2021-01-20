/*
 * Copyright (C) 2018
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.ignis.backend;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import org.apache.thrift.TMultiplexedProcessor;
import org.ignis.backend.exception.IPropertyException;
import org.ignis.backend.exception.ISchedulerException;
import org.ignis.backend.properties.IKeys;
import org.ignis.backend.properties.IProperties;
import org.ignis.backend.scheduler.IScheduler;
import org.ignis.backend.scheduler.ISchedulerBuilder;
import org.ignis.backend.services.IAttributes;
import org.ignis.backend.services.IBackendServiceImpl;
import org.ignis.backend.services.IClusterServiceImpl;
import org.ignis.backend.services.IDataFrameServiceImpl;
import org.ignis.backend.services.IPropertiesServiceImpl;
import org.ignis.backend.services.IWorkerServiceImpl;
import org.ignis.rpc.driver.IBackendService;
import org.ignis.rpc.driver.IClusterService;
import org.ignis.rpc.driver.IDataFrameService;
import org.ignis.rpc.driver.IPropertiesService;
import org.ignis.rpc.driver.IWorkerService;
import org.slf4j.LoggerFactory;

/**
 * @author César Pomar
 */
public final class Main {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Main.class);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LOGGER.info("Backend started");
        IProperties props = new IProperties();

        LOGGER.info("Loading environment variables");
        props.fromEnv(System.getenv());

        if (props.contains(IKeys.OPTIONS)) {//Submit user options
            try {
                props.load(new ByteArrayInputStream(props.getProperty(IKeys.OPTIONS).getBytes()));
            } catch (IOException ex) {
            }
            props.rmProperty(IKeys.OPTIONS);
        }

        LOGGER.info("Loading configuration file");
        try {
            String conf = new File(props.getString(IKeys.HOME), "etc/ignis.conf").getPath();
            props.load(conf, false);//only load not set properties
        } catch (IPropertyException | IOException ex) {
            LOGGER.error("Error loading ignis.conf, aborting", ex);
            System.exit(-1);
        }

        if (props.contains(IKeys.DEBUG) && props.getBoolean(IKeys.DEBUG)) {
            System.setProperty(IKeys.DEBUG, "true");
            LOGGER.info("DEBUG enabled");
        } else {
            System.setProperty(IKeys.DEBUG, "false");
        }

        LOGGER.info("Loading scheduler");
        IScheduler scheduler = null;
        String schedulerType = null;
        String schedulerUrl = null;
        try {
            schedulerType = props.getString(IKeys.SCHEDULER_TYPE);
            schedulerUrl = props.getString(IKeys.SCHEDULER_URL);
        } catch (IPropertyException ex) {
            LOGGER.error(ex.getMessage(), ex);
            System.exit(-1);
        }
        try {
            LOGGER.info("Checking scheduler " + schedulerType);
            scheduler = ISchedulerBuilder.create(schedulerType, schedulerUrl);
            scheduler.healthCheck();
            LOGGER.info("Scheduler " + schedulerType + " " + schedulerUrl + " ... OK");
        } catch (ISchedulerException ex) {
            LOGGER.error("Scheduler " + schedulerType + " " + schedulerUrl + " ... Fails\n" + ex);
            System.exit(-1);
        }

        IAttributes attributes = new IAttributes(props);
        try {
            LOGGER.info("Getting Driver container info from scheduler");
            attributes.driver.initInfo(scheduler.getContainer(props.getProperty(IKeys.JOB_NAME)));
            LOGGER.info("Driver container info found");
        } catch (ISchedulerException ex) {
            LOGGER.error("Not found", ex);
            System.exit(-1);
        }

        LOGGER.info("Setting dynamic properties");
        props.setProperty(IKeys.DRIVER_PUBLIC_KEY, attributes.ssh.getPublicKey());
        props.setProperty(IKeys.DRIVER_PRIVATE_KEY, attributes.ssh.getPrivateKey());
        int healthcheckPort = props.getInteger(IKeys.DRIVER_HEALTHCHECK_PORT);
        String healthcheck = "http://" + attributes.driver.getInfo().getHost() + ":";
        healthcheck += attributes.driver.getInfo().searchHostPort(healthcheckPort);
        props.setProperty(IKeys.DRIVER_HEALTHCHECK_URL, healthcheck);

        if (Boolean.getBoolean(IKeys.DEBUG)) {
            LOGGER.info("Debug: " + props.toString());
        }

        TMultiplexedProcessor processor = new TMultiplexedProcessor();

        IBackendServiceImpl backend = null;
        IClusterServiceImpl clusters = null;

        try {
            processor.registerProcessor("IBackend", new IBackendService.Processor<>(backend = new IBackendServiceImpl(attributes)));
            processor.registerProcessor("ICluster", new IClusterService.Processor<>(clusters = new IClusterServiceImpl(attributes, scheduler)));
            processor.registerProcessor("IWorker", new IWorkerService.Processor<>(new IWorkerServiceImpl(attributes)));
            processor.registerProcessor("IDataFrame", new IDataFrameService.Processor<>(new IDataFrameServiceImpl(attributes)));
            processor.registerProcessor("IProperties", new IPropertiesService.Processor<>(new IPropertiesServiceImpl(attributes)));
        } catch (Exception ex) {
            LOGGER.error("Error starting services, aborting", ex);
            System.exit(-1);
        }

        try {
            Integer backendPort = props.getInteger(IKeys.DRIVER_RPC_PORT);
            Integer backendCompression = props.getInteger(IKeys.DRIVER_RPC_COMPRESSION);
            Integer driverPort = props.getInteger(IKeys.EXECUTOR_RPC_PORT);
            Integer driverCompression = props.getInteger(IKeys.EXECUTOR_RPC_COMPRESSION);
            System.out.println(backendPort);
            System.out.println(backendCompression);
            System.out.println(driverPort);
            System.out.println(driverCompression);
            backend.start(processor, backendPort, backendCompression);

            if (!Boolean.getBoolean(IKeys.DEBUG)) {
                clusters.destroyClusters();
            }
            try {
                attributes.driver.getExecutor().disconnect();
            } catch (Exception ex) {
                LOGGER.warn("Driver callback disconnect error", ex);
            }

            LOGGER.info("Backend stopped");
            System.exit(0);
        } catch (Exception ex) {
            LOGGER.error("Server error, aborting", ex);
            System.exit(-1);
        }
    }

}
