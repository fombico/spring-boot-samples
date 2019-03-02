package com.fombico.sendgrid

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SendGridApplication

fun main(args: Array<String>) {
	runApplication<SendGridApplication>(*args)
}
