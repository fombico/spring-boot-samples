package com.fombico.springcloudstreaminput;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Store {

    public void save(Book book) {
        log.info("Saved book: {}", book);
    }
}
