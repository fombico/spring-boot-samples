package com.fombico.rabbitmqsample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BroadcastMessageConsumers {

    @RabbitListener(queues = {Config.FANOUT_QUEUE_1})
    public void receiveMessageFromFanout1(String message) {
        log.info("Receive message from fanout 1: {}", message);
    }

    @RabbitListener(queues = {Config.FANOUT_QUEUE_2})
    public void receiveMessageFromFanout2(String message) {
        log.info("Receive message from fanout 2: {}", message);
    }

    @RabbitListener(queues = {Config.TOPIC_QUEUE_1})
    public void receiveMessageFromTopic1(String message) {
        log.info("Receive message from topic 1: {}", message);
    }

    @RabbitListener(queues = {Config.TOPIC_QUEUE_2})
    public void receiveMessageFromTopic2(String message) {
        log.info("Receive message from topic 2: {}", message);
    }
}

