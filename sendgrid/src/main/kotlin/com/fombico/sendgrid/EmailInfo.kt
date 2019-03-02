package com.fombico.sendgrid

fun email(block: EmailInfo.() -> Unit): EmailInfo = EmailInfo().apply(block)

data class EmailInfo(var subject: String? = null,
                     var message: String? = null,
                     var fromEmail: String? = null,
                     var fromName: String? = null,
                     var toEmail: String? = null,
                     var toName: String? = null)