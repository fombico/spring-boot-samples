package com.fombico.sendgrid

import com.sendgrid.*
import org.springframework.http.MediaType
import org.springframework.stereotype.Service

@Service
class SendGridEmailService(val sendGridClient: SendGrid) : EmailService {
    override fun sendEmail(emailInfo: EmailInfo) {
        val content = Content(MediaType.TEXT_HTML_VALUE, emailInfo.message)
        val mail = Mail(Email(emailInfo.fromEmail), emailInfo.subject, Email(emailInfo.toEmail), content)

        val request = Request()
        request.method = Method.POST
        request.endpoint = "mail/send"
        request.body = mail.build()

        sendGridClient.api(request)
    }
}