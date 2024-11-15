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
package org.ignis.backend.cluster.tasks.container;

import org.ignis.backend.cluster.IContainer;
import org.ignis.backend.cluster.ITaskContext;
import org.ignis.scheduler.ISchedulerException;
import org.ignis.backend.exception.IgnisException;
import org.ignis.properties.IKeys;
import org.ignis.scheduler.IScheduler;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.ignis.backend.ui.DBUpdateService.destroyManyContainers;
import static org.ignis.backend.ui.DBUpdateService.upsertManyContainers;

/**
 * @author César Pomar
 */
public final class IContainerDestroyTask extends IContainerTask {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(IContainerDestroyTask.class);

    private final List<IContainer> containers;
    private final IScheduler scheduler;

    public IContainerDestroyTask(String name, IContainer container, IScheduler scheduler, List<IContainer> containers) {
        super(name, container);
        this.containers = containers;
        this.scheduler = scheduler;
    }

    @Override
    public void run(ITaskContext context) throws IgnisException {
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < containers.size(); i++) {
            IContainer container = containers.get(i);
            if (container.getInfo() == null) {
                if (Boolean.getBoolean(IKeys.DEBUG)) {
                    LOGGER.info(log() + "Container " + i + " is not started");
                }
                return;
            }
            ids.add(container.getInfo().getId());

            switch (scheduler.getStatus(container.getInfo().getId())) {
                case ACCEPTED:
                    LOGGER.info(log() + "Container " + i + " is not launched yet");
                    break;
                case RUNNING:
                    LOGGER.info(log() + "Container " + i + " is running");
                    break;
                case ERROR:
                    LOGGER.info(log() + "Container " + i + " has an error");
                    break;
                case FINISHED:
                    LOGGER.info(log() + "Container " + i + " is finished");
                    break;
                case DESTROYED:
                    LOGGER.info(log() + "Container " + i + " already destroyed");
                    return;
                case UNKNOWN:
                    LOGGER.info(log() + "Container " + i + " has a unknown status");
                    break;
            }
        }

        try {
            String killScript = "kill -SIGTERM 1";
            container.getTunnel().execute(killScript, true);
            Thread.sleep(2000);
            container.getTunnel().close();
        } catch (IgnisException | InterruptedException ex) {
            LOGGER.warn(log() + ex.toString());
        }

        try {
            scheduler.destroyExecutorInstances(ids);
            LOGGER.info(log() + "Container destroyed");
        } catch (ISchedulerException ex) {
            LOGGER.warn(log() + "Containers destroyed with errors: " + ex);
        }

        for (int i = 0; i < containers.size(); i++) {
            containers.get(i).setInfo(null);
        }
        try {

            List<Long> containerids = containers.stream()
                    .map(IContainer::getId)  // Extract the id using method reference
                    .toList();
            //upsertManyContainers(container.getProperties().getProperty(IKeys.JOB_ID),container.getCluster(), containers);
            destroyManyContainers(container.getProperties().getProperty(IKeys.JOB_ID),container.getCluster(), containerids);
        } catch (IOException e) {
            LOGGER.info(log() +"Error while updating container: "+container.getId());
        }

    }

}
