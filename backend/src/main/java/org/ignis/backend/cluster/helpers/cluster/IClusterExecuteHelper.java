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
package org.ignis.backend.cluster.helpers.cluster;

import org.ignis.backend.cluster.ICluster;
import org.ignis.backend.cluster.IContainer;
import org.ignis.backend.cluster.tasks.ICondicionalTaskGroup;
import org.ignis.backend.cluster.tasks.ILazy;
import org.ignis.backend.cluster.tasks.ITaskGroup;
import org.ignis.backend.cluster.tasks.container.IExecuteCmdTask;
import org.ignis.backend.cluster.tasks.container.IExecuteScriptTask;
import org.ignis.backend.exception.IgnisException;
import org.ignis.properties.IKeys;
import org.ignis.properties.IProperties;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static org.ignis.backend.ui.DBUpdateService.upsertCluster;

/**
 * @author César Pomar
 */
public final class IClusterExecuteHelper extends IClusterHelper {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(IClusterExecuteHelper.class);

    public IClusterExecuteHelper(ICluster cluster, IProperties properties) {
        super(cluster, properties);
    }

    public ILazy<Void> execute(List<String> cmd) throws IgnisException {
        LOGGER.info(log() + "Registering a cmd in cluster init");
        ITaskGroup.Builder builder = new ICondicionalTaskGroup.Builder(() -> cluster.isRunning());
        for (IContainer container : cluster.getContainers()) {
            builder.newTask(new IExecuteCmdTask(getName(), container, cmd));
        }
        ITaskGroup group = builder.build();
        cluster.getTasks().getSubTasksGroup().add(group);
        try {
            String aux;
            aux =upsertCluster(cluster.getProperties().getProperty(IKeys.JOB_ID),cluster);
            LOGGER.info(log() + "insertado cluster:\n" + aux);
        } catch (IOException e) {
            LOGGER.info(log() +"Error while updating cluster: "+cluster.getName());
        }
        return () -> {
            ITaskGroup dummy = new ITaskGroup.Builder(cluster.getLock()).build();
            dummy.getSubTasksGroup().add(group);
            dummy.start(cluster.getPool());
            return null;
        };
    }

    public ILazy<Void> executeScript(String script) throws IgnisException {
        LOGGER.info(log() + "Registering script in cluster init");
        ITaskGroup.Builder builder = new ICondicionalTaskGroup.Builder(() -> cluster.isRunning());
        for (IContainer container : cluster.getContainers()) {
            builder.newTask(new IExecuteScriptTask(script, container, script));
        }
        ITaskGroup group = builder.build();
        cluster.getTasks().getSubTasksGroup().add(group);
        try {
            String aux;
            aux =upsertCluster(cluster.getProperties().getProperty(IKeys.JOB_ID),cluster);
            LOGGER.info(log() + "insertado cluster:\n" + aux);
        } catch (IOException e) {
            LOGGER.info(log() +"Error while updating cluster: "+cluster.getName());
        }
        return () -> {
            ITaskGroup dummy = new ITaskGroup.Builder(cluster.getLock()).build();
            dummy.getSubTasksGroup().add(group);
            dummy.start(cluster.getPool());
            return null;
        };
    }

}
