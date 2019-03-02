package com.fombico.sendgrid

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/v1/email"])
class EmailController(val emailService: EmailService) {

    @PostMapping
    fun email(@RequestBody emailInfo: EmailInfo): ResponseEntity<Unit> {
        emailService.sendEmail(emailInfo)
        return ResponseEntity.ok().build()
    }
}