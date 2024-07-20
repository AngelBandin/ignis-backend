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
package org.ignis.backend.cluster.helpers.worker;

import org.ignis.backend.cluster.IExecutor;
import org.ignis.backend.cluster.IWorker;
import org.ignis.backend.cluster.tasks.ITaskGroup;
import org.ignis.backend.cluster.tasks.executor.IExecutorDestroyTask;
import org.ignis.properties.IKeys;
import org.ignis.properties.IProperties;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.ignis.backend.ui.DBUpdateService.destroyCluster;
import static org.ignis.backend.ui.DBUpdateService.destroyWorker;

/**
 * @author César Pomar
 */
public final class IWorkerDestroyHelper extends IWorkerHelper {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(IWorkerDestroyHelper.class);

    public IWorkerDestroyHelper(IWorker worker, IProperties properties) {
        super(worker, properties);
    }

    public ITaskGroup destroy() {
        LOGGER.info(log() + "Preparing worker to destroy");
        ITaskGroup.Builder builder = new ITaskGroup.Builder(worker.getLock());
        for (IExecutor executor : worker.getExecutors()) {
            builder.newTask(new IExecutorDestroyTask(getName(), executor));
        }
        try {
            destroyWorker(worker.getCluster().getProperties().getProperty(IKeys.JOB_ID),worker.getCluster().getId(), worker.getId());
        } catch (IOException e) {
            LOGGER.info(log() + "Error while destroying worker: "+worker.getName());
        }
        return builder.build();
    }

}
