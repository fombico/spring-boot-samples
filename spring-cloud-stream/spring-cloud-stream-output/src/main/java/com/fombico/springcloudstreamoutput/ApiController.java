package com.fombico.springcloudstreamoutput;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    private MessagingService messagingService;

    public ApiController(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @PostMapping("/book")
    public void book(@RequestBody Book book) {
        messagingService.sendBookAsMessage(book);
    }

}
