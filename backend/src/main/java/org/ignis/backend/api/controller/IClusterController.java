package org.ignis.backend.api.controller;


import org.ignis.backend.api.model.IClusterMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;


import java.time.Duration;
/*
@RestController
@RequestMapping("/events")
public class IClusterController {

    /*@GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    //solicitud de eventos para actualizar UI
    public Flux<String> getMessages() {
        // ... Logic to generate a stream of messages
        return Flux.interval(Duration.ofSeconds(1))
                .map(l -> "Message: " + l);
    }
    @GetMapping(value = "/ICluster", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    //solicitud de eventos para actualizar UI
    public Flux<IClusterMessage> updateUI() {
        // ... Logic to generate a stream of messages
        return Flux.interval(Duration.ofSeconds(1)).map(IClusterMessage::new);
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
}

}*/
