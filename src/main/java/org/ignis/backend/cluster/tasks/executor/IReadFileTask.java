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
package org.ignis.backend.cluster.tasks.executor;

import org.apache.thrift.TException;
import org.ignis.backend.cluster.IExecutor;
import org.ignis.backend.cluster.IExecutionContext;
import org.ignis.backend.cluster.helpers.IHelper;
import org.ignis.backend.exception.IgnisException;
import org.slf4j.LoggerFactory;

/**
 *
 * @author César Pomar
 */
public final class IReadFileTask extends IExecutorContextTask {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(IReadFileTask.class);

    private final String path;
    private final long offset;
    private final long length;
    private final long lines;

    public IReadFileTask(IHelper helper, IExecutor executor, String path, long offset, long length, long lines) {
        super(helper, executor, Mode.SAVE);
        this.path = path;
        this.offset = offset;
        this.length = length;
        this.lines = lines;
    }

    @Override
    public void execute(IExecutionContext context) throws IgnisException {
        LOGGER.info(log() + "Reading file");
        try {
            executor.getFilesModule().readFile(path, offset, length, lines);
        } catch (TException ex) {
            throw new IgnisException(ex.getMessage(), ex);
        }
        LOGGER.info(log() + "File read");
    }

}
