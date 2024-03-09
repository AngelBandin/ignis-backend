package org.ignis.backend.api.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ignis.backend.cluster.ICluster;
import org.ignis.backend.cluster.IContainer;
import org.ignis.backend.cluster.ISSH;
import org.ignis.backend.cluster.IWorker;
import org.ignis.backend.cluster.helpers.cluster.IClusterCreateHelper;
import org.ignis.backend.cluster.tasks.ILock;
import org.ignis.backend.cluster.tasks.ITask;
import org.ignis.backend.cluster.tasks.ITaskGroup;
import org.ignis.backend.cluster.tasks.IThreadPool;
import org.ignis.backend.exception.IgnisException;
import org.ignis.properties.IKeys;
import org.ignis.properties.IProperties;
import org.ignis.scheduler.IScheduler;

import java.util.ArrayList;
import java.util.List;

public class IClusterMessage {

    private final String name;
    private final long id;
    //private final IProperties properties;
    //private final ITaskGroup tasks;
    //private final IThreadPool pool;
    private final List<IContainerMessage> containers;
    //private final List<IWorker> workers;

    public IClusterMessage(ICluster c){
        this.name = c.getName();
        this.id = c.getId();
        //this.tasks = c.getTasks();
        this.containers = convertir(c.getContainers());
    }

    public String getName() {
        return name;
    }

    public List<IContainerMessage> getContainers() {
        return containers;
    }
    public long getId() {
        return id;
    }
    public static List<IContainerMessage> convertir(List<IContainer> listaX) {
        List<IContainerMessage> listaY = new ArrayList<>();
        for (IContainer x : listaX) {
            listaY.add(new IContainerMessage(x));
        }
        return listaY;
    }
    /*public void aux (){
        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(persona);

        Persona personaDeserializada = mapper.readValue(json, Persona.class);
    }*/


}

