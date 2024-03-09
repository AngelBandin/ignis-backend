package org.ignis.backend.api.model;

import org.ignis.backend.cluster.ICluster;
import org.ignis.backend.cluster.IContainer;
import org.ignis.backend.cluster.ITunnel;
import org.ignis.properties.IProperties;
import org.ignis.scheduler.model.IContainerInfo;

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
