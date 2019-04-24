package com.fombico.springcloudstreaminput;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface CustomProcessor {

    String CHANNEL = "sharedChannel";

    @Input(CHANNEL)
    SubscribableChannel inputChannel();

}
