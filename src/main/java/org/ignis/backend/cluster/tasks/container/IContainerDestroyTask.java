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
import org.ignis.backend.exception.ISchedulerException;
import org.ignis.backend.exception.IgnisException;
import org.ignis.backend.scheduler.IScheduler;
import org.slf4j.LoggerFactory;

/**
 *
 * @author César Pomar
 */
public final class IContainerDestroyTask extends IContainerTask {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(IContainerDestroyTask.class);

    private final IScheduler scheduler;

    public IContainerDestroyTask(String name, IContainer container, IScheduler scheduler) {
        super(name, container);
        this.scheduler = scheduler;
    }

    @Override
    public void run(ITaskContext context) throws IgnisException {
        LOGGER.info(log() + "Check if container status");
        if (container.getInfo() == null) {
            LOGGER.info(log() + "Continer is not started");
            return;
        }

        switch (scheduler.getStatus(container.getInfo().getId())) {
            case ACCEPTED:
                LOGGER.info(log() + "Container is not launched yet");
                break;
            case RUNNING:
                LOGGER.info(log() + "Continer is running");
                break;
            case ERROR:
                LOGGER.info(log() + "Continer has an error");
                break;
            case FINISHED:
                LOGGER.info(log() + "Continer is finieshed");
                break;
            case DESTROYED:
                LOGGER.info(log() + "Continer already destroyed");
                return;
            case UNKNOWN:
                LOGGER.info(log() + "Continer has a unknown status");
                break;
        }
        LOGGER.info(log() + "Destroying container");
        try {
            scheduler.destroyContainer(container.getInfo().getId());
        } catch (ISchedulerException ex) {
            LOGGER.warn(log() + "Container destroyed " + ex);
        }
        LOGGER.info(log() + "Container destroyed");
    }

}
