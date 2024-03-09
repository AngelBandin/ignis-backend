package org.ignis.backend.api.controller;


import org.ignis.backend.api.model.IClusterMessage;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalTime;
import java.util.stream.Stream;

@RestController
@RequestMapping("/events")
public class IClusterController {

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    //solicitud de eventos para actualizar UI
    public Flux<String> getMessages() {
        // ... Logic to generate a stream of messages
        return Flux.interval(Duration.ofSeconds(1))
                .map(l -> "Message: " + l);
    }
    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    //solicitud de eventos para actualizar UI
    public Flux<ResponseEntity<IClusterMessage>> updateUI() {
        // ... Logic to generate a stream of messages
        return Flux.interval(Duration.ofSeconds(1)).
    }

    /*@GetMapping("/events")
    public SseEmitter getEvents() {
        new Thread(() -> {
            while (true) {
                try {
                    emitter.send(SseEmitter.event().data("Hola mundo!"));
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return emitter;
    }
}*/

}
