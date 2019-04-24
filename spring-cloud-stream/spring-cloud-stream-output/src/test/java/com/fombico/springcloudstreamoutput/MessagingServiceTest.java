package com.fombico.springcloudstreamoutput;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.messaging.Message;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.BlockingQueue;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessagingServiceTest {

    @Autowired
    private CustomProcessor processor;

    @Autowired
    private MessageCollector messageCollector;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void sendMessage_sendsUpdatedMessageToQueue() throws Exception {
        BlockingQueue<Message<?>> queue = messageCollector.forChannel(processor.outputChannel());
        assertThat(queue.size()).isZero();

        Book book = Book.builder()
                .isbn("978-0486272788")
                .title("Hamlet")
                .author("William Shakespeare")
                .build();
        String expectedPayload = objectMapper.writeValueAsString(book);
        messagingService.sendBookAsMessage(book);

        assertThat(queue.size()).isEqualTo(1);
        String payload = (String) queue.poll().getPayload();
        assertThat(payload).isEqualTo(expectedPayload);
    }
}