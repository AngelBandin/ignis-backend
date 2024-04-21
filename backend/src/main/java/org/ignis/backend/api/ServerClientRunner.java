package org.ignis.backend.api;

import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.ignis.backend.api.client.IClusterMessageClient;
import org.ignis.backend.api.server.IClusterMessageServer;
import org.ignis.backend.api.services.IClusterMessageService;
import org.ignis.backend.services.*;
import org.ignis.rpc.driver.IBackendService;
import org.ignis.rpc.driver.IClusterService;
import org.ignis.rpc.driver.IPropertiesService;
import org.ignis.rpc.driver.IWorkerService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerClientRunner {
    public static void main(String[] args) {
        // Crear un ExecutorService con dos hilos
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Ejecutar el servidor en un hilo
        executor.execute(() -> IClusterMessageServer.main(null));

        // Esperar un poco para asegurarse de que el servidor esté en funcionamiento antes de iniciar el cliente
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Ejecutar el cliente en otro hilo
        executor.execute(() -> IClusterMessageClient.main(null));

        // Apagar el ExecutorService cuando se completan todas las tareas
        executor.shutdown();
    }
}
