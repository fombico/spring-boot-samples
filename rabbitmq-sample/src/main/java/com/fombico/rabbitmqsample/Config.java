package com.fombico.rabbitmqsample;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

import static org.springframework.amqp.core.BindingBuilder.bind;

@Configuration
public class Config {

    public static final String FANOUT_EXCHANGE = "fanout.exchange";
    public static final String TOPIC_EXCHANGE = "topic.exchange";
    public static final String FANOUT_QUEUE_1 = "fanout.queue1";
    public static final String FANOUT_QUEUE_2 = "fanout.queue2";
    public static final String TOPIC_QUEUE_1 = "topic.queue1";
    public static final String TOPIC_QUEUE_2 = "topic.queue2";

    @Bean
    public List<Declarable> fanoutBindings() {
        Queue fanoutQueue1 = new Queue(FANOUT_QUEUE_1, false);
        Queue fanoutQueue2 = new Queue(FANOUT_QUEUE_2, false);
        FanoutExchange fanoutExchange = new FanoutExchange(FANOUT_EXCHANGE);

        return Arrays.asList(
                fanoutQueue1,
                fanoutQueue2,
                fanoutExchange,
                bind(fanoutQueue1).to(fanoutExchange),
                bind(fanoutQueue2).to(fanoutExchange));
    }

    @Bean
    public List<Declarable> topicBindings() {
        Queue topicQueue1 = new Queue(TOPIC_QUEUE_1, false);
        Queue topicQueue2 = new Queue(TOPIC_QUEUE_2, false);

        TopicExchange topicExchange = new TopicExchange(TOPIC_EXCHANGE);

        return Arrays.asList(
                topicQueue1,
                topicQueue2,
                topicExchange,
                BindingBuilder
                        .bind(topicQueue1)
                        .to(topicExchange).with("*.important.*"),
                BindingBuilder
                        .bind(topicQueue2)
                        .to(topicExchange).with("#.error"));
    }
}
