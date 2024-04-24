package org.ignis.backend.api.services;

import org.apache.thrift.TException;
import org.ignis.backend.api.client.IClusterMessageClient;
import org.ignis.backend.api.model.IClusterMessage;
import org.ignis.backend.api.model.IContainerMessage;
import org.ignis.backend.cluster.IContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IClusterMessageServiceImpl implements IClusterMessageService.Iface{
    public ExecutorService executorService;

    public IClusterMessageServiceImpl() {
        // Se utiliza un ExecutorService para manejar los hilos
        executorService = Executors.newSingleThreadExecutor();
    }

    // Método para enviar datos continuamente al cliente
    /*public void sendDataContinuously(IClusterMessageClient client) {
        while (true) {
            // Supongamos que tienes una forma de obtener los datos del hilo endpoint
            List<IClusterMessage> clusterMessages = obtenerDatosDelHiloEndpoint();

            // Enviamos cada mensaje al cliente

            client.sendData(clusterMessages);


            // Agregamos un pequeño retraso antes de enviar el siguiente lote de datos
            try {
                Thread.sleep(1000); // 1 segundo de retraso
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }*/

    // Método para simular la obtención de datos del hilo endpoint
    private List<IClusterMessage> obtenerDatosDelHiloEndpoint(int c) {
        List<IClusterMessage> listaX = new ArrayList<>();
        for (long i = 0; i < c; i++) {
            listaX.add(new IClusterMessage(i));
        }
        return listaX;
    }
    private List<IClusterMessage> obtenerDatosDelHiloEndpoint() {
        List<IClusterMessage> listaX = new ArrayList<>();
            listaX.add(new IClusterMessage((long)5));
            synchronized (clusterObject.getLock()) {
                result = clusterObject.start();
            }
        return listaX;
    }

    @Override
    public List<IClusterMessage> sendData() throws TException {
        return obtenerDatosDelHiloEndpoint(5);
    }
}
