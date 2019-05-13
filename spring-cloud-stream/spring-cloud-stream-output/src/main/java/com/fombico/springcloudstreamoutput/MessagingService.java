package com.fombico.springcloudstreamoutput;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
@EnableBinding({CustomProcessor.class})
public class MessagingService {

    private CustomProcessor customProcessor;

    public void sendBookAsMessage(Book book) {
        log.info("Sending message: {}", book);
        customProcessor.outputChannel().send(MessageBuilder.withPayload(book).build(), 1000L);
    }
}
