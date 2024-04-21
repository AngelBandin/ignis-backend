package org.ignis.backend.api.model;

import org.ignis.backend.cluster.ICluster;
import org.ignis.backend.cluster.IContainer;


import java.util.ArrayList;
import java.util.List;


//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClusterDeprecated {
    private final String name;
    private final long id;
    private final List<ContainerDeprecated> containers;

    public ClusterDeprecated(ICluster c){
        this.name = c.getName();
        this.id = c.getId();
        this.containers = convertir(c.getContainers());
    }

    //constructor provisional para pruebas
    public ClusterDeprecated(Long c){
        this.name = "ClusterPrueba";
        this.id = c;
        this.containers = new ArrayList<>();
        this.containers.add(new ContainerDeprecated(c));
    }

    public String getName() {
        return name;
    }

    public List<ContainerDeprecated> getContainers() {
        return containers;
    }
    public long getId() {
        return id;
    }
    public static List<ContainerDeprecated> convertir(List<IContainer> listaX) {
        List<ContainerDeprecated> listaY = new ArrayList<>();
        for (IContainer x : listaX) {
            listaY.add(new ContainerDeprecated(x));
        }
        return listaY;
    }

}

