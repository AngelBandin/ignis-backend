package org.ignis.backend.api.client;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.ignis.backend.api.model.IClusterMessage;
import org.ignis.backend.api.model.IContainerMessage;
import org.ignis.backend.api.services.IClusterMessageService;


import java.io.ObjectInputStream;
import java.util.List;

public class IClusterMessageClient {
        public static void main(String[] args) {
            try {
                TTransport transport = new TSocket("localhost", 9090);
                transport.open();

                TProtocol protocol = new TBinaryProtocol(transport);
                IClusterMessageService.Client client = new IClusterMessageService.Client(protocol);


                    // Llamar al método del servicio para recibir datos del servidor
                    List<IClusterMessage> message = client.sendData();
                    printClusterInfo(message);
                //transport.close();
            } catch (TException e) {
                e.printStackTrace();
            }
        }

    private static void printClusterInfo(List<IClusterMessage> message) {
        for (IClusterMessage iClusterMessage : message) {
            System.out.println("Cluster: " + iClusterMessage.getId());
            System.out.println("--Nombre: " + iClusterMessage.getName());
            System.out.println("--Contenedores:");
            for (IContainerMessage iContainerMessage : iClusterMessage.getContainers()) {
                System.out.println("----Contenedor: " + iContainerMessage.getId());
                System.out.println("------Cluster:" + iContainerMessage.getCluster());
                System.out.println("------Info cluster:" + iContainerMessage.getInfoid());
            }
        }
    }


}
