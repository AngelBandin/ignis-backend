package org.ignis.backend.api.server;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.ignis.backend.api.client.IClusterMessageClient;
import org.ignis.backend.api.services.IClusterMessageService;
import org.ignis.backend.api.services.IClusterMessageServiceImpl;

public class IClusterMessageServer {
    public static void main(String[] args) {
        try {
            // Se crea el servicio
            IClusterMessageServiceImpl serviceImpl = new IClusterMessageServiceImpl();
            IClusterMessageService.Processor<IClusterMessageServiceImpl> processor = new IClusterMessageService.Processor<>(serviceImpl);

            // Se configura el socket del servidor
            TServerTransport serverTransport = new TServerSocket(9090);
            TThreadPoolServer.Args serverArgs = new TThreadPoolServer.Args(serverTransport)
                    .processor(processor)
                    .protocolFactory(new TBinaryProtocol.Factory());

            // Se crea el servidor
            TServer server = new TThreadPoolServer(serverArgs);

            // Se inicia un hilo para enviar datos continuamente al cliente

            // Se inicia el servidor
            System.out.println("Starting the server...");
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }
}
