/*
 * Copyright (C) 2019 César Pomar
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
package org.ignis.backend.cluster.helpers.dataframe;

import org.ignis.backend.cluster.IDataFrame;
import org.ignis.backend.cluster.IDriver;
import org.ignis.backend.cluster.IExecutor;
import org.ignis.backend.cluster.ITaskContext;
import org.ignis.backend.cluster.tasks.ILazy;
import org.ignis.backend.cluster.tasks.ITaskGroup;
import org.ignis.backend.cluster.tasks.executor.*;
import org.ignis.backend.exception.IgnisException;
import org.ignis.properties.IKeys;
import org.ignis.properties.IProperties;
import org.ignis.rpc.ISource;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.ignis.backend.ui.DBUpdateService.upsertWorker;

/**
 * @author César Pomar
 */
public final class IDataMathHelper extends IDataHelper {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(IDataMathHelper.class);

    public IDataMathHelper(IDataFrame data, IProperties properties) {
        super(data, properties);
    }

    public IDataFrame sample(boolean withReplacement, double fraction, int seed) throws IgnisException {
        ITaskGroup.Builder builder = new ITaskGroup.Builder(data.getLock());
        builder.newDependency(data.getTasks());
        ISampleTask.Shared shared = new ISampleTask.Shared(data.getExecutors().size());
        for (IExecutor executor : data.getExecutors()) {
            builder.newTask(new ISampleTask(getName(), executor, shared, withReplacement, fraction, seed));
        }
        IDataFrame target = data.createDataFrame("", builder.build());
        LOGGER.info(log() + "sample(" +
                "withReplacement=" + withReplacement +
                ", fraction=" + fraction +
                ", seed=" + seed +
                ") registered -> " + target.getName());
        try {
            upsertWorker(data.getWorker().getProperties().getProperty(IKeys.JOB_ID),data.getWorker().getCluster().getId(),data.getWorker());
        } catch (IOException e) {
            throw new IgnisException("Error while updating worker: "+data.getWorker().getName());
        }
        return target;
    }

    public ILazy<Long> takeSample(IDriver driver, boolean withReplacement, long num, int seed, ISource tp) {
        ITaskGroup.Builder builder = new ITaskGroup.Builder(data.getLock());
        builder.newDependency(data.getTasks());
        ITakeSampleTask.Shared shared = new ITakeSampleTask.Shared(data.getExecutors().size());
        for (IExecutor executor : data.getExecutors()) {
            builder.newTask(new ITakeSampleTask(getName(), executor, shared, false, withReplacement, num, seed, tp));
        }

        builder.newLock(driver.getLock());
        builder.newDependency(driver.driverContection());
        builder.newTask(new ITakeSampleTask(driver.getName(), driver.getExecutor(), shared, true, withReplacement, num, seed, tp));
        LOGGER.info(log() + "takeSample(" +
                "withReplacement=" + withReplacement +
                ", num=" + num +
                ", seed=" + seed +
                ") registered");

        return () -> {
            ITaskContext context = builder.build().start(data.getPool());
            return context.popContext(driver.getExecutor());
        };
    }

    public ILazy<Long> count() throws IgnisException {
        ITaskGroup.Builder builder = new ITaskGroup.Builder(data.getLock());
        builder.newDependency(data.getTasks());
        ICountTask.Shared shared = new ICountTask.Shared(data.getExecutors().size());
        for (IExecutor executor : data.getExecutors()) {
            builder.newTask(new ICountTask(getName(), executor, shared));
        }
        LOGGER.info(log() + "count(" +
                ") registered");
        try {
            upsertWorker(data.getWorker().getProperties().getProperty(IKeys.JOB_ID),data.getWorker().getCluster().getId(),data.getWorker());
        } catch (IOException e) {
            throw new IgnisException("Error while updating worker: "+data.getWorker().getName());
        }
        return () -> {
            ITaskContext context = builder.build().start(data.getPool());
            return context.<Long>get("result");
        };
    }

    public ILazy<Long> max(IDriver driver, ISource tp) throws IgnisException {
        ITaskGroup.Builder builder = new ITaskGroup.Builder(data.getLock());
        builder.newDependency(data.getTasks());
        IMaxTask.Shared shared = new IMaxTask.Shared(data.getExecutors().size());
        for (IExecutor executor : data.getExecutors()) {
            builder.newTask(new IMaxTask(getName(), executor, shared, false, tp));
        }

        builder.newLock(driver.getLock());
        builder.newDependency(driver.driverContection());
        builder.newTask(new IMaxTask(driver.getName(), driver.getExecutor(), shared, true, tp));
        LOGGER.info(log() + "max(" +
                ") registered");
        try {
            upsertWorker(data.getWorker().getProperties().getProperty(IKeys.JOB_ID),data.getWorker().getCluster().getId(),data.getWorker());
        } catch (IOException e) {
            throw new IgnisException("Error while updating worker: "+data.getWorker().getName());
        }
        return () -> {
            ITaskContext context = builder.build().start(data.getPool());
            return context.popContext(driver.getExecutor());
        };
    }

    public ILazy<Long> max(IDriver driver, ISource cmp, ISource tp) throws IgnisException {
        ITaskGroup.Builder builder = new ITaskGroup.Builder(data.getLock());
        builder.newDependency(data.getTasks());
        IMaxTask.Shared shared = new IMaxTask.Shared(data.getExecutors().size());
        for (IExecutor executor : data.getExecutors()) {
            builder.newTask(new IMaxTask(getName(), executor, shared, false, cmp, tp));
        }

        builder.newLock(driver.getLock());
        builder.newDependency(driver.driverContection());
        builder.newTask(new IMaxTask(driver.getName(), driver.getExecutor(), shared, true, cmp, tp));
        LOGGER.info(log() + "max(" +
                "cmp=" + srcToString(cmp) +
                ") registered");
        try {
            upsertWorker(data.getWorker().getProperties().getProperty(IKeys.JOB_ID),data.getWorker().getCluster().getId(),data.getWorker());
        } catch (IOException e) {
            throw new IgnisException("Error while updating worker: "+data.getWorker().getName());
        }
        return () -> {
            ITaskContext context = builder.build().start(data.getPool());
            return context.popContext(driver.getExecutor());
        };
    }

    public ILazy<Long> min(IDriver driver, ISource tp) throws IgnisException {
        ITaskGroup.Builder builder = new ITaskGroup.Builder(data.getLock());
        builder.newDependency(data.getTasks());
        IMinTask.Shared shared = new IMinTask.Shared(data.getExecutors().size());
        for (IExecutor executor : data.getExecutors()) {
            builder.newTask(new IMinTask(getName(), executor, shared, false, tp));
        }

        builder.newLock(driver.getLock());
        builder.newDependency(driver.driverContection());
        builder.newTask(new IMinTask(driver.getName(), driver.getExecutor(), shared, true, tp));
        LOGGER.info(log() + "min(" +
                ") registered");
        try {
            upsertWorker(data.getWorker().getProperties().getProperty(IKeys.JOB_ID),data.getWorker().getCluster().getId(),data.getWorker());
        } catch (IOException e) {
            throw new IgnisException("Error while updating worker: "+data.getWorker().getName());
        }
        return () -> {
            ITaskContext context = builder.build().start(data.getPool());
            return context.popContext(driver.getExecutor());
        };
    }

    public ILazy<Long> min(IDriver driver, ISource cmp, ISource tp) throws IgnisException {
        ITaskGroup.Builder builder = new ITaskGroup.Builder(data.getLock());
        builder.newDependency(data.getTasks());
        IMinTask.Shared shared = new IMinTask.Shared(data.getExecutors().size());
        for (IExecutor executor : data.getExecutors()) {
            builder.newTask(new IMinTask(getName(), executor, shared, false, cmp, tp));
        }

        builder.newLock(driver.getLock());
        builder.newDependency(driver.driverContection());
        builder.newTask(new IMinTask(driver.getName(), driver.getExecutor(), shared, true, cmp, tp));
        LOGGER.info(log() + "min(" +
                "cmp=" + srcToString(cmp) +
                ") registered");
        try {
            upsertWorker(data.getWorker().getProperties().getProperty(IKeys.JOB_ID),data.getWorker().getCluster().getId(),data.getWorker());
        } catch (IOException e) {
            throw new IgnisException("Error while updating worker: "+data.getWorker().getName());
        }
        return () -> {
            ITaskContext context = builder.build().start(data.getPool());
            return context.popContext(driver.getExecutor());
        };
    }

    public IDataFrame sampleByKey(boolean withReplacement, ISource fractions, int seed) throws IgnisException {
        throw new UnsupportedOperationException("Not supported on this version."); //TODO next version
    }

    public ILazy<Long> countByKey(IDriver driver, ISource tp) throws IgnisException {
        ITaskGroup.Builder builder = new ITaskGroup.Builder(data.getLock());
        builder.newDependency(data.getTasks());
        ICountByKeyTask.Shared shared = new ICountByKeyTask.Shared(data.getExecutors().size());
        for (IExecutor executor : data.getExecutors()) {
            builder.newTask(new ICountByKeyTask(getName(), executor, shared, false, tp));
        }

        builder.newLock(driver.getLock());
        builder.newDependency(driver.driverContection());
        builder.newTask(new ICountByKeyTask(driver.getName(), driver.getExecutor(), shared, true, tp));
        LOGGER.info(log() + "countByKey(" +
                ") registered");
        try {
            upsertWorker(data.getWorker().getProperties().getProperty(IKeys.JOB_ID),data.getWorker().getCluster().getId(),data.getWorker());
        } catch (IOException e) {
            throw new IgnisException("Error while updating worker: "+data.getWorker().getName());
        }
        return () -> {
            ITaskContext context = builder.build().start(data.getPool());
            return context.popContext(driver.getExecutor());
        };
    }

    public ILazy<Long> countByValue(IDriver driver, ISource tp) throws IgnisException {
        ITaskGroup.Builder builder = new ITaskGroup.Builder(data.getLock());
        builder.newDependency(data.getTasks());
        ICountByValueTask.Shared shared = new ICountByValueTask.Shared(data.getExecutors().size());
        for (IExecutor executor : data.getExecutors()) {
            builder.newTask(new ICountByValueTask(getName(), executor, shared, false, tp));
        }

        builder.newLock(driver.getLock());
        builder.newDependency(driver.driverContection());
        builder.newTask(new ICountByValueTask(driver.getName(), driver.getExecutor(), shared, true, tp));
        LOGGER.info(log() + "countByValue(" +
                ") registered");
        try {
            upsertWorker(data.getWorker().getProperties().getProperty(IKeys.JOB_ID),data.getWorker().getCluster().getId(),data.getWorker());
        } catch (IOException e) {
            throw new IgnisException("Error while updating worker: "+data.getWorker().getName());
        }
        return () -> {
            ITaskContext context = builder.build().start(data.getPool());
            return context.popContext(driver.getExecutor());
        };
    }

}
