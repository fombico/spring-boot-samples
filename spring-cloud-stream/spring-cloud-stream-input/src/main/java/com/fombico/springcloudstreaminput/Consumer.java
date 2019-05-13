package com.fombico.springcloudstreaminput;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

@Slf4j
@AllArgsConstructor
@EnableBinding({CustomProcessor.class})
public class Consumer {

    private Store store;

    @StreamListener(CustomProcessor.CHANNEL)
    public void handleMessage(Message<Book> message) {
        log.info("Received message: {}", message.getPayload());
        store.save(message.getPayload());
    }
}
