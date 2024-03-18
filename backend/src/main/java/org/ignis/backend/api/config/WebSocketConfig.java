/*package org.ignis.backend.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer
{
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //aqui se conectara el cliente(front end de la página web) al backend

        registry.addEndpoint("/stomp-endpoint").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        //identificadas como tópicos
        registry.enableSimpleBroker("/topic");
        //mensajes pensados para l aplicación
        registry.setApplicationDestinationPrefixes("/app");

    }

}*/
