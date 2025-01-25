package org.ignis.backend.ui;

import org.ignis.backend.cluster.*;
import org.ignis.backend.cluster.tasks.ITaskGroup;
import org.ignis.properties.IKeys;
import org.ignis.properties.IProperties;
import org.ignis.properties.IPropertyException;
import org.ignis.scheduler.model.IBind;
import org.ignis.scheduler.model.IContainerInfo;
import org.ignis.scheduler.model.IPort;
import org.ignis.scheduler.model.IVolume;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ComplexJsonMapper {

    private static <T> void safeAppend(StringBuilder sb, String key, T value, Function<T, String> mapper) {
        if (value != null) {
            sb.append(", \"").append(key).append("\": ");
            sb.append(mapper.apply(value));
        }
    }

    private static void safeAppendString(StringBuilder sb, String key, String value) {
        safeAppend(sb, key, value, v -> "\"" + v + "\"");
    }
    private static void safeAppendList(StringBuilder sb, String key, List<String> values) {
        if (values != null && !values.isEmpty()) {
            sb.append(", \"").append(key).append("\": [");
            sb.append(values.stream()
                    .map(value -> "\"" + value.replace("\"", "\\\"") + "\"")
                    .collect(Collectors.joining(", ")));
            sb.append("]");
        }
    }

    private static void safeAppendNumber(StringBuilder sb, String key, Number value) {
        safeAppend(sb, key, value, Object::toString);
    }

    private static <T> void safeAppendCollection(StringBuilder sb, String key, Collection<T> collection, Function<T, String> elementMapper) {
        if (collection != null && !collection.isEmpty()) {
            sb.append(", \"").append(key).append("\": [");
            boolean first = true;
            for (T item : collection) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(elementMapper.apply(item));
                first = false;
            }
            sb.append("]");
        }
    }

    private static List<IContainer> executorstoContainers(List<IExecutor> executors) {
        List<IContainer> result = new ArrayList<>();
        if (executors != null) {
            for (IExecutor executor : executors) {
                if (executor != null && executor.getContainer() != null) {
                    result.add(executor.getContainer());
                }
            }
        }
        return result;
    }

    static String jobPostBuilder(IProperties properties, IContainer driver) {
        StringBuilder sb = new StringBuilder("{");
        safeAppendString(sb, "id", properties.getString(IKeys.JOB_ID));
        safeAppendString(sb, "name", properties.getString(IKeys.JOB_NAME));

        try {
            safeAppendString(sb, "directory", properties.getString(IKeys.JOB_DIRECTORY));
        } catch (IPropertyException e) {
            // Do nothing
        }

        try {
            safeAppendString(sb, "worker", properties.getString(IKeys.JOB_WORKER));
        } catch (IPropertyException e) {
            // Do nothing
        }
        safeAppend(sb, "driver", driver, ComplexJsonMapper::ContainerMapper);
        safeAppendString(sb, "status","Running");
        sb.append(", \"clusters\": []}");
        return sb.toString().replaceFirst("^\\{, ", "{");
    }
    static String jobFinishBuilder(IProperties properties) {
        StringBuilder sb = new StringBuilder("{");
        safeAppendString(sb, "name", properties.getString(IKeys.JOB_NAME));
        safeAppendString(sb, "id", properties.getString(IKeys.JOB_ID));

        try {
            safeAppendString(sb, "directory", properties.getString(IKeys.JOB_DIRECTORY));
        } catch (IPropertyException e) {
            // Do nothing
        }

        try {
            safeAppendString(sb, "worker", properties.getString(IKeys.JOB_WORKER));
        } catch (IPropertyException e) {
            // Do nothing
        }
        safeAppendString(sb, "status","Finished");
        sb.append("}");
        return sb.toString().replaceFirst("^\\{, ", "{");

    }


    static String workerPostBuilder(String jobId, long clusterId, IWorker worker) {
        StringBuilder sb = new StringBuilder("{");
        safeAppendString(sb, "jobId", jobId);
        safeAppendNumber(sb, "clusterId", clusterId);
        safeAppend(sb, "worker", worker, ComplexJsonMapper::WorkerMapper);
        sb.append("}");
        return sb.toString().replaceFirst("^\\{, ", "{");
    }
    static String clusterPostBuilder(String jobId, ICluster cluster) {
        StringBuilder sb = new StringBuilder("{");
        safeAppendString(sb, "jobId", jobId);
        safeAppend(sb, "cluster", cluster, ComplexJsonMapper::ClusterMapper);
        sb.append("}");
        return sb.toString().replaceFirst("^\\{, ", "{");
    }
    static String containersPostBuilder(String jobId, long clusterId, List<IContainer> containers) {
        StringBuilder sb = new StringBuilder("{");
        safeAppendString(sb, "jobId", jobId);
        safeAppendNumber(sb, "clusterId", clusterId);
        safeAppendCollection(sb, "containers", containers, ComplexJsonMapper::ContainerMapper);
        sb.append("}");

        return sb.toString().replaceFirst("^\\{, ", "{");
    }

    static String ClusterMapper(ICluster cluster) {
        if (cluster == null) return "{}";

        StringBuilder sb = new StringBuilder("{");
        safeAppendString(sb, "name", cluster.getName());
        safeAppendNumber(sb, "id", cluster.getId());
        safeAppend(sb, "taskgroup", cluster.getTasks(), ComplexJsonMapper::TaskgroupMapper);
        safeAppendCollection(sb, "workers", cluster.getWorkers(), ComplexJsonMapper::WorkerMapper);
        safeAppendCollection(sb, "containers", cluster.getContainers(), ComplexJsonMapper::ContainerMapper);
        safeAppend(sb, "properties", cluster.getProperties(), ComplexJsonMapper::PropertiesMapper);
        sb.append("}");
        return sb.toString().replaceFirst("^\\{, ", "{");
    }

    private static String WorkerMapper(IWorker worker) {
        if (worker == null) return "{}";

        StringBuilder sb = new StringBuilder("{");
        safeAppendNumber(sb, "cluster", worker.getCluster() != null ? worker.getCluster().getId() : null);
        safeAppendString(sb, "name", worker.getName());
        safeAppendNumber(sb, "id", worker.getId());
        safeAppendString(sb, "type", worker.getType());
        safeAppendNumber(sb, "cores", worker.getCores());
        safeAppend(sb, "taskgroup", worker.getTasks(), ComplexJsonMapper::TaskgroupMapper);
        safeAppendCollection(sb, "containers", executorstoContainers(worker.getExecutors()), ComplexJsonMapper::ContainerMapper);
        safeAppendCollection(sb, "dataframes", worker.getDataFrames(), ComplexJsonMapper::DataFrameMapper);
        sb.append("}");
        return sb.toString().replaceFirst("^\\{, ", "{");
    }

    private static String DataFrameMapper(IDataFrame dataFrame) {
        if (dataFrame == null) return "{}";

        StringBuilder sb = new StringBuilder("{");
        safeAppendString(sb, "name", dataFrame.getName());
        safeAppendNumber(sb, "id", dataFrame.getId());
        safeAppendString(sb, "worker", dataFrame.getWorker() != null ? dataFrame.getWorker().getName() : null);
        safeAppend(sb, "taskgroup", dataFrame.getTasks(), ComplexJsonMapper::TaskgroupMapper);
        sb.append("}");
        return sb.toString().replaceFirst("^\\{, ", "{");
    }

    private static String ContainerMapper(IContainer container) {
        if (container == null) return "{}";

        StringBuilder sb = new StringBuilder("{");
        safeAppendNumber(sb, "id", container.getId());
        safeAppendNumber(sb, "cluster", container.getCluster());
        IContainerInfo info = container.getInfo();
        if (info != null) {
            safeAppendString(sb, "infoid", info.getId());
            safeAppendString(sb, "host", info.getHost());
            safeAppendString(sb, "image", info.getImage());
            safeAppendString(sb, "command", info.getCommand());
            safeAppendList(sb, "arguments", info.getArguments());
            safeAppendNumber(sb, "cpus", info.getCpus());
            safeAppendNumber(sb, "memory", info.getMemory());
            safeAppendNumber(sb, "swappiness", info.getSwappiness());
            safeAppendCollection(sb, "ports", info.getPorts(), ComplexJsonMapper::PortMapper);
            safeAppendString(sb, "networkMode", info.getNetworkMode() != null ? info.getNetworkMode().toString() : null);
            safeAppendCollection(sb, "binds", info.getBinds(), ComplexJsonMapper::BindMapper);
            safeAppendCollection(sb, "volumes", info.getVolumes(), ComplexJsonMapper::VolumeMapper);
            safeAppendList(sb, "preferedHosts", info.getPreferedHosts());
            safeAppendList(sb, "hostnames", info.getHostnames());
            safeAppend(sb, "environmentVariables", info.getEnvironmentVariables(), ComplexJsonMapper::mapToJsonString);
            safeAppend(sb, "schedulerParams", info.getSchedulerParams(), ComplexJsonMapper::mapToJsonString);
        }
        safeAppendNumber(sb, "resets", container.getResets());
        safeAppend(sb, "properties", container.getProperties(), ComplexJsonMapper::PropertiesMapper);
        sb.append("}");
        return sb.toString().replaceFirst("^\\{, ", "{").replace("\n", "").replace("\r", "");
    }

    private static String PortMapper(IPort port) {
        if (port == null) return "{}";

        StringBuilder sb = new StringBuilder("{");
        safeAppendNumber(sb, "containerPort", port.getContainerPort());
        safeAppendNumber(sb, "hostPort", port.getHostPort());
        safeAppendString(sb, "protocol", port.getProtocol());
        sb.append("}");
        return sb.toString().replaceFirst("^\\{, ", "{");
    }

    private static String BindMapper(IBind bind) {
        if (bind == null) return "{}";

        StringBuilder sb = new StringBuilder("{");
        safeAppendString(sb, "hostpath", bind.getHostPath());
        safeAppendString(sb, "containerpath", bind.getContainerPath());
        safeAppendString(sb, "readOnly", Boolean.toString(bind.isReadOnly()));
        sb.append("}");
        return sb.toString().replaceFirst("^\\{, ", "{");
    }

    private static String VolumeMapper(IVolume volume) {
        if (volume == null) return "{}";
        StringBuilder sb = new StringBuilder("{");
        safeAppendString(sb, "containerPath", volume.getContainerPath());
        safeAppendNumber(sb, "size", volume.getSize());
        sb.append("}");
        return sb.toString().replaceFirst("^\\{, ", "{");
    }

    private static String PropertiesMapper(IProperties properties) {
        return properties != null ? mapToJsonString(properties.toMap(true)) : "{}";
    }

    private static String TaskgroupMapper(ITaskGroup taskgroup) {
        if (taskgroup == null) return "{}";
        StringBuilder sb = new StringBuilder("{");
        safeAppendCollection(sb, "tasks", taskgroup.getTasks(),
                task -> "{\"name\": \"" +task.getClass().getName().replaceAll("^.*\\.", "")+" <-- "+task.getName() + "\"}");
        safeAppendDependencies(sb, taskgroup.getDepencencies());
        safeAppendCollection(sb, "subTasksGroup", taskgroup.getSubTasksGroup(), ComplexJsonMapper::TaskgroupMapper);
        sb.append("}");
        return sb.toString().replaceFirst("^\\{, ", "{");
    }

    private static void safeAppendDependencies(StringBuilder sb, List<ITaskGroup> dependencies) {
        safeAppendCollection(sb, "dependencies", dependencies,
                taskgroup -> {
                    String taskclass = taskgroup.getTasks().get(0).getClass().getName().replaceAll("^.*\\.", "");
                    String taskName = taskgroup.getTasks().get(0).getName();

                    return switch (taskclass) {
                        case "IContainerCreateTask" -> "{\"class\": \"Cluster\", \"parent\": \"" + taskName + "\"}";
                        case "IExecutorCreateTask" -> "{\"class\": \"Worker\", \"parent\": \"" + taskName + "\"}";
                        case "ICacheTask" -> "{\"class\": \"Dataframe\", \"parent\": \"" + taskName + "\"}";
                        default -> TaskgroupMapper(taskgroup);
                    };
                }
        );
    }

    private static String mapToJsonString(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!first) {
                sb.append(", ");
            }
            String value = entry.getValue().replace("\n", "").replace("\r", "");
            sb.append("\"").append(entry.getKey()).append("\": \"")
                    .append(value.replace("\"", "\\\"")).append("\"");
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }
}