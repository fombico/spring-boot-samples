package com.fombico.client;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestOperations;

@Slf4j
@Controller
@AllArgsConstructor
public class SiteController {

    private final RestOperations restOperations;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        log.info("orders");
        String orders = restOperations.getForObject("http://localhost:9000/api/orders", String.class);
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        log.info("admin");
        String orders = restOperations.getForObject("http://localhost:9000/api/admin/orders", String.class);
        model.addAttribute("orders", orders);
        return "admin";
    }

}
