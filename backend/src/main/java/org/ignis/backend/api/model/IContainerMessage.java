package org.ignis.backend.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.ignis.backend.cluster.IContainer;
import org.ignis.scheduler.model.IContainerInfo;


//@JsonInclude(JsonInclude.Include.NON_NULL)
public class IContainerMessage {
    private final long id;
    private final long cluster;
    private final String infoid;
    private final String host;
    private final String image;
    private final String command;
    private final int resets;
    public IContainerMessage(IContainer c){
        this.id = c.getId();
        this.cluster= c.getCluster();
        IContainerInfo aux = c.getInfo();
        this.infoid = aux.getId();
        this.host = aux.getHost();
        this.image = aux.getImage();
        this.command= aux.getCommand();
        this.resets= c.getResets();
    }
    //contructor para pruebas
    public IContainerMessage(Long c){
        this.id = c;
        this.cluster= c;
        this.infoid = "IClusterPrueba";
        this.host = "IClusterPrueba";
        this.image = "IClusterPrueba";
        this.command= "IClusterPrueba";
        this.resets= c.byteValue();
    }

    public long getId() {
        return id;
    }

    public long getCluster() {
        return cluster;
    }

    public String getInfoid() {
        return infoid;
    }

    public String getHost() {
        return host;
    }

    public String getImage() {
        return image;
    }

    public String getCommand() {
        return command;
    }

    public int getResets() {
        return resets;
    }

}
