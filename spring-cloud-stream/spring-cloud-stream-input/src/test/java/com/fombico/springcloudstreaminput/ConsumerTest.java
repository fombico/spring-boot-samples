package com.fombico.springcloudstreaminput;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConsumerTest {

    @Autowired
    private CustomProcessor processor;

    @SpyBean
    private Store store;

    @Test
    public void handleMessage() throws Exception {
        Book book = Book.builder()
                .isbn("978-0486272788")
                .title("Hamlet")
                .author("William Shakespeare")
                .build();
        processor.inputChannel().send(MessageBuilder.withPayload(book).build());

        verify(store).save(book);
    }
}