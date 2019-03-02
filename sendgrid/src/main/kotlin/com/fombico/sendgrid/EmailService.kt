package com.fombico.sendgrid

interface EmailService
{
    fun sendEmail(emailInfo: EmailInfo)
}