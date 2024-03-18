package org.ignis.backend.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.ignis.backend.cluster.ICluster;
import org.ignis.backend.cluster.IContainer;



import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.List;


//@JsonInclude(JsonInclude.Include.NON_NULL)
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

    //constructor provisional para pruebas
    public IClusterMessage(Long c){
        this.name = "ClusterPrueba";
        this.id = c;
        //this.tasks = c.getTasks();
        this.containers = new ArrayList<>();
        this.containers.add(new IContainerMessage(c));
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

