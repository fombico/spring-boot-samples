package com.fombico.rabbitmqsample;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    private BroadcastMessageProducer broadcastMessageProducer;

    public ApiController(BroadcastMessageProducer broadcastMessageProducer) {
        this.broadcastMessageProducer = broadcastMessageProducer;
    }

    @PostMapping("/message")
    public void message(@RequestBody String message) {
        this.broadcastMessageProducer.sendMessages(message);
    }
}
