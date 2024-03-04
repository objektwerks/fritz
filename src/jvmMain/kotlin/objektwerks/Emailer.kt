package objektwerks

import com.sksamuel.hoplite.ConfigLoader

import jodd.mail.Email
import jodd.mail.MailServer

data class EmailerConfig(val host: String,
                         val sender: String,
                         val password: String) {
    companion object {
        fun load(resource: String): EmailerConfig = ConfigLoader().loadConfigOrThrow<EmailerConfig>(resource)
    }
}

class Emailer(config: EmailerConfig) {
    private val sender = config.sender
    private val smtpServer = MailServer.create()
        .host(config.host)
        .ssl(true)
        .auth(sender, config.password)
        .buildSmtpMailServer()

    private fun sendEmail(recipients: List<String>,
                          subject: String,
                          message: String): Unit =
        smtpServer.createSession().use { session ->
            val email = Email.create()
                .from(sender)
                .subject(subject)
                .htmlMessage(message, "UTF-8")
                .cc(sender)
                recipients.forEach { email.to(it) }
                session.open()
                session.sendMail(email)
        }
}