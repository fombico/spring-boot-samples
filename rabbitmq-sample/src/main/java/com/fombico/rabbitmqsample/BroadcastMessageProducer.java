package com.fombico.rabbitmqsample;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class BroadcastMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessages(String message) {
        log.info("Sending message: {}", message);
        rabbitTemplate.convertAndSend(Config.FANOUT_EXCHANGE, "", message);
        rabbitTemplate.convertAndSend(Config.TOPIC_EXCHANGE, "user.important.info", message);
        if (message.contains("error")) {
            rabbitTemplate.convertAndSend(Config.TOPIC_EXCHANGE, "system.error", message);
        }
    }
}
