/*package org.ignis.backend.api.services;


import org.apache.thrift.TException;
import org.ignis.backend.api.model.IClusterMessage;
import org.ignis.backend.api.model.IContainerMessage;

import java.util.ArrayList;

//Cluster de inicializacion para pruebas
public class IClusterThread implements Runnable{

        private IClusterMessageService.Client client;

        public IClusterThread(IClusterMessageService.Client client) {
            this.client = client;
        }

        @Override
        public void run() {
            // Generar datos
            while (true) {
                IClusterMessage data = new IClusterMessage();
                data.name = "Cluster";
                data.id = (long)(Math.random()*1000);
                data.containers = new ArrayList<>();

                // Agregar algunos contenedores de ejemplo
                for (int i = 0; i < 3; i++) {
                    IContainerMessage container = new IContainerMessage();
                    container.id = i;
                    container.cluster = data.id;
                    container.infoid = "info-" + i;
                    container.host = "host-" + i;
                    container.image = "image-" + i;
                    container.command = "command-" + i;
                    container.resets = 0;

                    data.containers.add(container);
                }

                // Enviar datos al servidor
                try {
                    client.sendData(data);
                } catch (TException e) {
                    e.printStackTrace();
                }

                // Esperar un segundo
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

}*/
