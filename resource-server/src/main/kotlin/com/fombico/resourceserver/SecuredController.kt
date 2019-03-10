package com.fombico.resourceserver

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SecuredController {

    @GetMapping("/hello")
    fun hello(): String {
        return "hello"
    }

    @GetMapping("/read")
    fun read(): String {
        return "read"
    }

    @PostMapping("/write")
    fun write(): String {
        return "write"
    }
}