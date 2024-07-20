package org.ignis.backend.ui;

import org.ignis.backend.cluster.*;
import org.ignis.backend.cluster.tasks.ITask;
import org.ignis.backend.cluster.tasks.ITaskGroup;
import org.ignis.properties.IKeys;
import org.ignis.properties.IProperties;
import org.ignis.scheduler.model.IBind;
import org.ignis.scheduler.model.IContainerInfo;
import org.ignis.scheduler.model.IPort;
import org.ignis.scheduler.model.IVolume;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ComplexJsonMapper {

    private static List<IContainer> executorstoContainers(List<IExecutor> executors) {
        List<IContainer> result = new ArrayList<>();
        for (IExecutor executor : executors) {
            result.add(executor.getContainer());
        }
        return result;
    }

    static String jobPostBuilder(IProperties properties){
        StringBuilder aux;
        aux = new StringBuilder("{\"name\": \"").append(properties.getString(IKeys.JOB_NAME))
                .append("\", \"id\": \"").append(properties.getString(IKeys.JOB_ID))
                .append("\", \"directory\": \"").append(properties.getString(IKeys.JOB_DIRECTORY))
                .append("\", \"directory\": \"").append(properties.getString(IKeys.JOB_WORKER))
                .append("\", \"clusters\": []}");
        return aux.toString();
    }

    static String workerPostBuilder(String jobId, long clusterId, IWorker worker) {
        StringBuilder aux;
        aux = new StringBuilder("{\"jobId\": \"")
                .append(jobId).append("\", \"clusterId\": ")
                .append(clusterId).append(", \"worker\": ")
                .append(WorkerMapper(worker)).append("}");
        return aux.toString();
    }
    static String clusterPostBuilder(String jobId, ICluster cluster) {
        StringBuilder aux;
        aux = new StringBuilder("{\"jobId\": \"")
                .append(jobId).append("\", \"cluster\": ")
                .append(ClusterMapper(cluster)).append("}");
        return aux.toString();
    }
    static String containerPostBuilder(String jobId, long clusterId, IContainer container) {
        StringBuilder aux;
        aux = new StringBuilder("{\"jobId\": \"")
                .append(jobId).append("\", \"clusterId\": ")
                .append(clusterId).append(", \"container\": ")
                .append(ContainerMapper(container)).append("}");
        return aux.toString();
    }
    static String containersPostBuilder(String jobId, long clusterId, List<IContainer> containers) {
        StringBuilder aux;
        boolean first = true;
        aux = new StringBuilder("{\"jobId\": \"")
                .append(jobId).append("\", \"clusterId\": ")
                .append(clusterId).append(", \"containerid\": [");
        for(IContainer container : containers){
            if (!first) {
                aux.append(", ");
            }
            aux.append(ContainerMapper(container));
            first = false;
        }
                aux.append("]}");
        return aux.toString();
    }
    static String containersDestroyBuilder(String jobId, long clusterId, List<Long> containerids) {
        StringBuilder aux;
        aux = new StringBuilder("{\"jobId\": \"")
                .append(jobId).append("\", \"clusterId\": ")
                .append(clusterId).append(", \"containerid\": ")
                .append(containerids.toString()).append("}");
        return aux.toString();
    }


    private static String ClusterMapper(ICluster cluster){
        StringBuilder aux;
        boolean first = true;
        aux = new StringBuilder("{\"name\": \"");
        aux.append(cluster.getName()).append("\", \"id\": ")
                .append(cluster.getId()).append(", \"taskgroup\": ")
                .append(TaskgroupMapper(cluster.getTasks()))
                .append(", \"workers\": [");
        for (IWorker worker : cluster.getWorkers()){
            if (!first) {
                aux.append(",");
            }
            aux.append(WorkerMapper(worker));
            first = false;
        }
        first = true;
        aux.append("], \"containers\": [");
        for (IContainer container : cluster.getContainers()){
            if (!first) {
                aux.append(",");
            }
            aux.append(ContainerMapper(container));
            first = false;
        }
        aux.append("], \"properties\": ")
                .append(PropertiesMapper(cluster.getProperties()))
                .append("}");
        return aux.toString();
    }

    private static String WorkerMapper(IWorker worker){
        StringBuilder aux;
        boolean first = true;
        aux = new StringBuilder("{\"cluster\": ");
        aux.append(worker.getCluster() != null ? worker.getCluster().getId() : "N/A")
                .append("\"name\": \"").append(worker.getName()).append("\",")
                .append("\"id\": ").append(worker.getId()).append(",")
                .append("\"type\": \"").append(worker.getType()).append("\",")
                .append("\"cores\": ").append(worker.getCores()).append(",")
                .append("\"taskgroup\": ").append(TaskgroupMapper(worker.getTasks())).append(",");
        aux.append("\"containers\": [");
        for (IContainer container : executorstoContainers(worker.getExecutors())){
            if (!first) {
                aux.append(",");
            }
            aux.append(ContainerMapper(container));
            first = false;
        }
        first = true;
        aux.append("],\"dataframes\":[");
        for (IDataFrame dataFrame : worker.getDataFrames()){
            if (!first) {
                aux.append(",");
            }
            aux.append(DataFrameMapper(dataFrame));
            first = false;
        }
        aux.append("]}");
        return aux.toString();
    }

    private static String DataFrameMapper(IDataFrame dataFrame) {
        StringBuilder aux;
        aux = new StringBuilder("{\"name\": \"");
        aux.append(dataFrame.getName())
                .append("\", \"id\": ").append(dataFrame.getId())
                .append(", \"worker\": \"").append(dataFrame.getWorker() != null ? dataFrame.getWorker().getName(): "N/A")
                .append("\"taskgroup\": ").append(TaskgroupMapper(dataFrame.getTasks())).append("}");
        return aux.toString();
    }

    private static String ContainerMapper(IContainer container) {
        StringBuilder aux;
        boolean first = true;
        IContainerInfo info = container.getInfo();
        aux = new StringBuilder("{\"id\": ").append(container.getId())
                .append(", \"cluster\": \"").append(container.getCluster())
                .append("\", \"infoid\": \"").append(info.getId())
                .append("\", \"host\": \"").append(info.getHost())
                .append("\", \"image\": \"").append(info.getImage())
                .append("\", \"command\": \"").append(info.getCommand())
                .append("\", \"arguments\": ").append(info.getArguments().toString())
                .append(", \"cpus\": ").append(info.getCpus())
                .append(", \"memory\": \"").append(info.getMemory())
                //a lo mejor cambiarlo a numero
                .append("m\", \"swappiness\": ").append(info.getSwappiness())
                .append(", \"ports\": [");
        for(IPort port : info.getPorts()){
            if (!first) {
                aux.append(", ");
            }
            aux.append("{\"containerPort\": ").append(port.getContainerPort())
                    .append(", \"hostPort\": ").append(port.getHostPort())
                    .append(", \"protocol\": \"").append(port.getHostPort())
                    .append("\"}");
            first = false;
        }
        first = true;
        aux.append("], \"networkMode\": \"").append(info.getNetworkMode().toString())
                .append("\", \"binds\": [");
        for(IBind bind : info.getBinds()){
            if (!first) {
                aux.append(", ");
            }
            aux.append("{\"hostpath\": \"").append(bind.getHostPath())
                    .append("\", \"containerpath\": \"").append(bind.getContainerPath())
                    .append("\", \"readOnly\": ").append(bind.isReadOnly())
                    .append("}");
            first = false;
        }
        first = true;
        aux.append("],\"volumes\": [");
        for(IVolume volume: info.getVolumes()){
            if (!first) {
                aux.append(", ");
            }
            aux.append("{\"containerPath\": \"").append(volume.getContainerPath())
                    .append("\", \"size\": ").append(volume.getSize())
                    .append("}");
            first = false;
        }

        aux.append("], \"preferedHosts\": ").append(info.getPreferedHosts().toString())
                .append(", \"hostnames\": ").append(info.getHostnames().toString())
                .append(", \"environmentVariables\": ").append(mapToJsonString(info.getEnvironmentVariables()))
                .append(", \"schedulerParams\": ").append(mapToJsonString(info.getSchedulerParams()))
                .append(", \"resets\": ").append(container.getResets())
                .append(", \"properties\": ").append(PropertiesMapper(container.getProperties()));
        aux.append("}");
        return aux.toString();
    }

    private static String mapToJsonString(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");

        boolean first = true;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!first) {
                sb.append(", ");
            }
            sb.append("\"").append(entry.getKey()).append("\"");
            sb.append(": ");
            sb.append("\"").append(entry.getValue()).append("\"");
            first = false;
        }

        sb.append("}");
        return sb.toString();
    }
    private static String PropertiesMapper(IProperties properties) {
        return mapToJsonString(properties.toMap(false));
    }
    private static String TaskgroupMapper (ITaskGroup taskgroup) {
        StringBuilder aux;
        boolean first = true;
        aux = new StringBuilder("{tasks:[");
        for (ITask task : taskgroup.getTasks()){
            if (!first) {
                aux.append(",");
            }
            aux.append("{\"name\": \"").append(task.getName()).append("\"}");
            first = false;
        }
        first = true;
        aux.append("],");
        aux.append("{\"dependencies\":[");
        for (ITaskGroup dependency : taskgroup.getDepencencies()){
            if (!first) {
                aux.append(",");
            }
            aux.append(TaskgroupMapper(dependency));
            first = false;
        }
        first = true;
        aux.append("],");
        aux.append("{\"subTasksGroup\":[");
        for (ITaskGroup subtasksgroup : taskgroup.getSubTasksGroup()){
            if (!first) {
                aux.append(",");
            }
            aux.append(TaskgroupMapper(subtasksgroup));
            first = false;
        }
        aux.append("]" + "}");
        return aux.toString();
    }
}