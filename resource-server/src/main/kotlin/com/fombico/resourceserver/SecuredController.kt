package com.fombico.resourceserver

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SecuredController {

    @GetMapping("/info")
    fun info(authentication: Authentication): String {
        return "Authentication: $authentication"
    }

    @GetMapping("/hello")
    fun hello(): String {
        return "hello"
    }

    @PreAuthorize("#oauth2.hasScope('todo.read')")
    @GetMapping("/read")
    fun read(): String {
        return "read"
    }

    @PreAuthorize("#oauth2.hasScope('todo.write')")
    @PostMapping("/write")
    fun write(): String {
        return "write"
    }
}