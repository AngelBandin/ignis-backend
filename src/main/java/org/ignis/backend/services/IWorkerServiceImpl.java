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
package org.ignis.backend.services;

import org.apache.thrift.TException;
import org.ignis.backend.cluster.ICluster;
import org.ignis.backend.cluster.IDataFrame;
import org.ignis.backend.cluster.IWorker;
import org.ignis.backend.cluster.helpers.worker.IWorkerImportDataHelper;
import org.ignis.backend.cluster.helpers.worker.IWorkerReadFileHelper;
import org.ignis.backend.cluster.tasks.ILock;
import org.ignis.backend.exception.IDriverExceptionImpl;
import org.ignis.backend.exception.IgnisException;
import org.ignis.backend.properties.IKeys;
import org.ignis.rpc.IDriverException;
import org.ignis.rpc.ISource;
import org.ignis.rpc.driver.IDataFrameId;
import org.ignis.rpc.driver.IWorkerId;
import org.ignis.rpc.driver.IWorkerService;

/**
 *
 * @author César Pomar
 */
public final class IWorkerServiceImpl extends IService implements IWorkerService.Iface {

    public IWorkerServiceImpl(IAttributes attributes) {
        super(attributes);
    }

    @Override
    public IWorkerId newInstance(long id, String type) throws IDriverException, TException {
        return newInstance3a(id, "", type);
    }

    @Override
    public IWorkerId newInstance3a(long id, String name, String type) throws IDriverException, TException {
        try {
            int cores = attributes.getCluster(id).getProperties().getInteger(IKeys.EXECUTOR_CORES);
            return newInstance4(id, name, type, cores);
        } catch (IgnisException ex) {
            throw new IDriverExceptionImpl(ex);
        }
    }

    @Override
    public IWorkerId newInstance3b(long id, String type, int cores) throws IDriverException, TException {
        return newInstance4(id, "", type, cores);
    }

    @Override
    public IWorkerId newInstance4(long id, String name, String type, int cores) throws IDriverException, TException {
        try {
            ICluster cluster = attributes.getCluster(id);
            synchronized (cluster.getLock()) {
                IWorker worker = cluster.createWorker(name, type, cores);
                return new IWorkerId(id, worker.getId());
            }
        } catch (Exception ex) {
            throw new IDriverExceptionImpl(ex);
        }
    }

    @Override
    public void setName(IWorkerId id, String name) throws IDriverException, TException {
        try {
            ICluster clusterObject = attributes.getCluster(id.getCluster());
            IWorker workerObject = clusterObject.getWorker(id.getWorker());
            synchronized (workerObject.getLock()) {
                workerObject.setName(name);
            }
        } catch (Exception ex) {
            throw new IDriverExceptionImpl(ex);
        }
    }

    @Override
    public IDataFrameId parallelize(IWorkerId id) throws IDriverException, TException {
        try {
            ICluster cluster = attributes.getCluster(id.getCluster());
            IWorker worker = cluster.getWorker(id.getWorker());
            synchronized (worker.getLock()) {
                IDataFrame data = new IWorkerReadFileHelper(worker, cluster.getProperties()).parallelize();
                return new IDataFrameId(cluster.getId(), worker.getId(), data.getId());
            }
        } catch (Exception ex) {
            throw new IDriverExceptionImpl(ex);
        }

    }

    @Override
    public IDataFrameId importDataFrame(IWorkerId id, IDataFrameId data) throws IDriverException, TException {
        try {
            ICluster clusterSource = attributes.getCluster(data.getCluster());
            ICluster clusterTarget = attributes.getCluster(id.getCluster());
            IWorker workerSource = clusterSource.getWorker(data.getWorker());
            IWorker workerTarget = clusterSource.getWorker(id.getWorker());

            ILock lock1 = workerSource.getLock();
            ILock lock2 = workerTarget.getLock();
            if (lock1.compareTo(lock2) < 0) {
                ILock tmp = lock1;
                lock1 = lock2;
                lock2 = tmp;
            }
            synchronized (lock1) {
                synchronized (lock2) {
                    IDataFrame source = workerSource.getDataFrame(data.getDataFrame());
                    IDataFrame target = new IWorkerImportDataHelper(workerTarget, clusterTarget.getProperties()).importData(source);
                    return new IDataFrameId(clusterTarget.getId(), workerTarget.getId(), target.getId());
                }
            }
        } catch (Exception ex) {
            throw new IDriverExceptionImpl(ex);
        }
    }

    @Override
    public IDataFrameId importDataFrame3(IWorkerId id, ISource src, IDataFrameId data) throws IDriverException, TException {
        try {
            ICluster clusterSource = attributes.getCluster(data.getCluster());
            ICluster clusterTarget = attributes.getCluster(id.getCluster());
            IWorker workerSource = clusterSource.getWorker(data.getWorker());
            IWorker workerTarget = clusterSource.getWorker(id.getWorker());

            ILock lock1 = workerSource.getLock();
            ILock lock2 = workerTarget.getLock();
            if (lock1.compareTo(lock2) < 0) {
                ILock tmp = lock1;
                lock1 = lock2;
                lock2 = tmp;
            }
            synchronized (lock1) {
                synchronized (lock2) {
                    IDataFrame source = workerSource.getDataFrame(data.getDataFrame());
                    IDataFrame target = new IWorkerImportDataHelper(workerTarget, clusterTarget.getProperties()).importData(source, src);
                    return new IDataFrameId(clusterTarget.getId(), workerTarget.getId(), target.getId());
                }
            }
        } catch (Exception ex) {
            throw new IDriverExceptionImpl(ex);
        }
    }

    @Override
    public IDataFrameId textFile(IWorkerId id, String path, long partitions) throws IDriverException, TException {
        try {
            ICluster cluster = attributes.getCluster(id.getCluster());
            IWorker worker = cluster.getWorker(id.getWorker());
            synchronized (worker.getLock()) {
                IDataFrame data = new IWorkerReadFileHelper(worker, worker.getProperties()).readFile(path, partitions);
                return new IDataFrameId(cluster.getId(), worker.getId(), data.getId());
            }
        } catch (Exception ex) {
            throw new IDriverExceptionImpl(ex);
        }
    }

    @Override
    public IDataFrameId openPartitionObjectFile(IWorkerId id, String path, long partitions) throws IDriverException, TException {
        try {
            ICluster cluster = attributes.getCluster(id.getCluster());
            IWorker worker = cluster.getWorker(id.getWorker());
            synchronized (worker.getLock()) {
                IDataFrame data = new IWorkerReadFileHelper(worker, worker.getProperties()).openPartitionObjectFile(path, partitions);
                return new IDataFrameId(cluster.getId(), worker.getId(), data.getId());
            }
        } catch (Exception ex) {
            throw new IDriverExceptionImpl(ex);
        }
    }

    @Override
    public IDataFrameId openPartitionObjectFileFunction(IWorkerId id, ISource src, String path, long partitions) throws IDriverException, TException {
        try {
            ICluster cluster = attributes.getCluster(id.getCluster());
            IWorker worker = cluster.getWorker(id.getWorker());
            synchronized (worker.getLock()) {
                IDataFrame data = new IWorkerReadFileHelper(worker, worker.getProperties()).openPartitionObjectFileFunction(path, src, partitions);
                return new IDataFrameId(cluster.getId(), worker.getId(), data.getId());
            }
        } catch (Exception ex) {
            throw new IDriverExceptionImpl(ex);
        }
    }
}
