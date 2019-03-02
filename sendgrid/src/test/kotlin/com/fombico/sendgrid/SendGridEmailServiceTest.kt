package com.fombico.sendgrid

import com.sendgrid.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
internal class SendGridEmailServiceTest {

    @TestConfiguration
    class TestConfig {
        @Bean
        fun sendGridClient() = mockk<SendGrid>()
    }

    @Autowired
    lateinit var sendGridClient: SendGrid

    private lateinit var emailService: SendGridEmailService

    @BeforeEach
    fun beforeEach() {
        emailService = SendGridEmailService(sendGridClient)
    }

    @Test
    fun `sendEmail() sends email via SendGrid`() {
        every { sendGridClient.api(any()) } returns Response()

        val email = email {
            subject = "Greetings"
            message = "Hello there!"
            fromEmail = "joey@grr.la"
            fromName = "Joey"
            toEmail = "fred@grr.la"
            toName = "Fred"
        }
        emailService.sendEmail(email)

        val content = Content("text/html", email.message)
        val mail = Mail(Email(email.fromEmail), email.subject, Email(email.toEmail), content)

        val slot = slot<Request>()
        verify { sendGridClient.api(capture(slot)) }

        val request = slot.captured
        assertThat(request.method).isEqualTo(Method.POST)
        assertThat(request.endpoint).isEqualTo("mail/send")
        assertThat(request.body).isEqualTo(mail.build())
    }
}