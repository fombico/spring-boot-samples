package com.fombico.springcloudstreamoutput;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ApiController {

    private MessagingService messagingService;

    @PostMapping("/book")
    public void book(@RequestBody Book book) {
        messagingService.sendBookAsMessage(book);
    }

}
