package objektwerks

import com.sksamuel.hoplite.ConfigLoader

import jodd.mail.MailServer
import jodd.mail.SmtpServer

data class EmailerConfig(val host: String,
                         val sender: String,
                         val password: String) {
    companion object {
        fun load(resource: String): EmailerConfig = ConfigLoader().loadConfigOrThrow<EmailerConfig>(resource)
    }
}

class Emailer(config: EmailerConfig) {
    private val smtpServer: SmtpServer = MailServer.create()
        .host(config.host)
        .ssl(true)
        .auth(config.sender, config.password)
        .buildSmtpMailServer()
}