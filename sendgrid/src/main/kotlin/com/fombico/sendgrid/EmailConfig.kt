package com.fombico.sendgrid

import com.sendgrid.SendGrid
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EmailConfig {

    @Bean
    fun sendGridClient(@Value("\${sendGrid.api.key}") apiKey: String): SendGrid {
        return SendGrid(apiKey)
    }
}