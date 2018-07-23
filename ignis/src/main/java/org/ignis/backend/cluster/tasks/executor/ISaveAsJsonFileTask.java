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
import org.ignis.backend.cluster.tasks.ILock;
import org.ignis.backend.cluster.tasks.Task;
import org.ignis.backend.exception.IgnisException;

/**
 *
 * @author César Pomar
 */
public class ISaveAsJsonFileTask extends IExecutorTask {

    private final String path;
    private final boolean joined;
    
    public ISaveAsJsonFileTask(IExecutor executor, String path, boolean joined, ILock lock, Task... dependencies) {
        super(executor, lock, dependencies);
        this.path = path;
        this.joined = joined;
    }

    @Override
    public void execute() throws IgnisException {
        try {
            executor.getFilesModule().saveJson(path, joined);
        } catch (TException ex) {
            throw new IgnisException("Save fails", ex);
        }
    }

}
