package com.fombico.orders;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController
public class OrdersController {

    @RequestMapping("/user")
    public Principal user(Principal user) {
        log.info("User request {}", user);
        return user;
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMERS')")
    @RequestMapping("/api/orders")
    public String orders() {
        log.info("Getting orders");
        return "Order 1234567890";
    }

    @PreAuthorize("hasRole('ROLE_ADMINS')")
    @RequestMapping("/api/admin/orders")
    public String admin_orders() {
        log.info("Getting admin orders");
        return "Admin Order 1234567890";
    }

}

