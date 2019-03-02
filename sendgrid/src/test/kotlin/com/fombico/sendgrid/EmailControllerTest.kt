package com.fombico.sendgrid

import io.mockk.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod.POST
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(SpringExtension::class)
@WebMvcTest(EmailController::class)
internal class EmailControllerTest {

    @TestConfiguration
    class ControllerTestConfig {
        @Bean
        fun emailService() = mockk<EmailService>()
    }

    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var emailService: EmailService

    private val baseUrl = "/v1/email"

    @Test
    fun `email() sends email`() {
        every { emailService.sendEmail(any()) } just Runs

        mvc.perform(request(POST, baseUrl)
                .content("""{
                    "subject":"Greetings",
                    "message": "Hello there!",
                    "fromEmail":"joey@grr.la",
                    "fromName":"Joey",
                    "toEmail":"fred@grr.la",
                    "toName":"Fred"
                }""".trimIndent())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)

        verify {
            emailService.sendEmail(email {
                subject = "Greetings"
                message = "Hello there!"
                fromEmail = "joey@grr.la"
                fromName = "Joey"
                toEmail = "fred@grr.la"
                toName = "Fred"
            })
        }
    }

}