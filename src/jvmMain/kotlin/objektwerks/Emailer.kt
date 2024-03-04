package objektwerks

import com.sksamuel.hoplite.ConfigLoader

data class EmailerConfig(val host: String,
                         val address: String,
                         val password: String) {
    companion object {
        fun load(resource: String): EmailerConfig = ConfigLoader().loadConfigOrThrow<EmailerConfig>(resource)
    }
}

class Emailer() {
}