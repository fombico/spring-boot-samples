package com.fombico.rabbitmqsample;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ApiController {

    private BroadcastMessageProducer broadcastMessageProducer;

    @PostMapping("/message")
    public void message(@RequestBody String message) {
        this.broadcastMessageProducer.sendMessages(message);
    }
}
