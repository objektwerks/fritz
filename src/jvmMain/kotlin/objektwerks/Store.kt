package objektwerks

import org.jetbrains.exposed.sql.Table

data class H2Config(val url: String,
                    val driver: String,
                    val user: String,
                    val password: String)

object Accounts : Table() {
    val id = integer("id").autoIncrement()
    val license = varchar("license", 36)
    val pin = varchar("pin", 7)
    val email = varchar("email", 128)
    override val primaryKey = PrimaryKey(id, name = "pk")
}

class Store {
}