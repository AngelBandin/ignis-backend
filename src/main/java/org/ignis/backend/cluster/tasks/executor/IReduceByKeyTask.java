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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.thrift.TException;
import org.ignis.backend.cluster.IContainer;
import org.ignis.backend.cluster.IExecutor;
import org.ignis.backend.cluster.helpers.IHelper;
import org.ignis.backend.cluster.tasks.IBarrier;
import org.ignis.backend.exception.IgnisException;
import org.ignis.backend.properties.IPropsKeys;
import org.ignis.rpc.ISource;
import org.slf4j.LoggerFactory;
import org.ignis.rpc.executor.IExecutorKeys;

/**
 *
 * @author César Pomar
 */
public final class IReduceByKeyTask extends IExecutorTask {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(IReduceByKeyTask.class);

    public static class Shared {

        //Executor -> Key -> Count (Multiple Write, One Read)
        private final Map<IExecutor, List<Long>> count = new ConcurrentHashMap<>();
        //Key -> Executor (One write, Multiple read)
        private final Map<IExecutor, Map<IExecutor, List<Long>>> msgs = new HashMap<>();
    }

    private final ISource function;
    private final IBarrier barrier;
    private final Shared keyShared;
    private final boolean single;

    public IReduceByKeyTask(IHelper helper, IExecutor executor, ISource function, IBarrier barrier, Shared keyShared) {
        super(helper, executor);
        this.function = function;
        this.barrier = barrier;
        this.keyShared = keyShared;
        this.single = barrier.getParties() == 1;
    }

    private void keyDistribution() {
        long total = keyShared.count.values().stream().map(m -> m.size()).reduce(0, (a, b) -> a + b);

        Map<Long, List<IExecutor>> keys = new HashMap<>();
        Map<IExecutor, Long> load = new HashMap<>();
        for (Map.Entry<IExecutor, List<Long>> values : keyShared.count.entrySet()) {
            for (long key : values.getValue()) {
                List<IExecutor> list = keys.get(key);
                if (list == null) {
                    keys.put(key, list = new ArrayList<>());
                }
                list.add(values.getKey());
            }
            load.put(values.getKey(), 0l);
            keyShared.msgs.put(values.getKey(), new HashMap<>());
        }

        for (Map.Entry<Long, List<IExecutor>> values : keys.entrySet()) {
            long maxKeys = 0;
            for (IExecutor target : values.getValue()) {
                maxKeys += keyShared.count.get(target).size();
            }
            maxKeys /= values.getValue().size();
            for (IExecutor target : values.getValue()) {
                long eload = load.get(target);
                if (eload <= maxKeys) {
                    Map<IExecutor, List<Long>> emsgs = keyShared.msgs.get(target);
                    for (IExecutor source : values.getValue()) {
                        List<Long> list = emsgs.get(source);
                        if (list == null) {
                            emsgs.put(source, list = new ArrayList<>());
                        }
                        list.add(values.getKey());
                    }
                    load.put(target, eload + 1);
                    break;
                }
            }
        }
    }

    @Override
    public void execute() throws IgnisException {
        try {
            LOGGER.info(log() + "Reducing executor keys");
            executor.getKeysModule().reduceByKey(function);
            LOGGER.info(log() + "Executor keys reduced");
            barrier.await();
            if (single) {
                LOGGER.info(log() + "Avoiding key exchange");
            } else {
                LOGGER.info(log() + "Preparing keys");
                List<Long> keys = executor.getKeysModule().getKeys();
                LOGGER.info(log() + "Keys ready");
                keyShared.count.put(executor, keys);
                LOGGER.info(log() + keys.size() + " keys");
                if (barrier.await() == 0) {
                    LOGGER.info(log() + "Calculating key distribution");
                    keyDistribution();
                }
                barrier.await();

                int port = executor.getContainer().getProperties().getInteger(IPropsKeys.TRANSPORT_PORT);

                LOGGER.info(log() + "Preparing keys to send to " + keyShared.msgs.size() + " executors");
                StringBuilder addr = new StringBuilder();
                List<IExecutorKeys> executorKeys = new ArrayList<>();
                for (Map.Entry<IExecutor, List<Long>> entry : keyShared.msgs.get(executor).entrySet()) {
                    addr.setLength(0);
                    //TODO shared memory
                    if (entry.getKey() == executor) {
                        addr.append("local");
                    } else {
                        IContainer container = entry.getKey().getContainer();
                        addr.append("socket!").append(container.getHost()).append("!").append(port);
                    }
                    executorKeys.add(new IExecutorKeys(executor.getId(), addr.toString(), entry.getValue()));
                    LOGGER.info(log() + entry.getValue().size() + " keys prepared to" + addr.toString());
                }
                executor.getKeysModule().prepareKeys(executorKeys);
                try {
                    LOGGER.info(log() + "Preparing to recive keys");
                    executor.getPostmanModule().start();
                    barrier.await();
                    LOGGER.info(log() + "Preparing to send keys");
                    executor.getPostmanModule().sendAll();
                    LOGGER.info(log() + "Keys sent");
                    barrier.await();
                } finally {
                    executor.getPostmanModule().stop();
                }
                LOGGER.info(log() + "Joining keys");
                executor.getKeysModule().collectKeys();
                LOGGER.info(log() + "Reducing keys");
                executor.getKeysModule().reduceByKey(function);
            }
            LOGGER.info(log() + "Keys Reduced");
            barrier.await();
        } catch (IgnisException ex) {
            barrier.fails();
            throw ex;
        } catch (BrokenBarrierException ex) {
            //Other Task has failed
        } catch (Exception ex) {
            barrier.fails();
            throw new IgnisException(ex.getMessage(), ex);
        }
    }

}