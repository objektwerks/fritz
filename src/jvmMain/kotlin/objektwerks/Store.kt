package objektwerks

import com.sksamuel.hoplite.ConfigLoader

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

data class H2Config(val url: String,
                    val driver: String,
                    val user: String,
                    val password: String)

object Accounts : Table() {
    val id = long("id").autoIncrement()
    val license = varchar("license", 36)
    val pin = varchar("pin", 7)
    val email = varchar("email", 128)
    override val primaryKey = PrimaryKey(id, name = "id")
}

class Store {
    init {
        val config = ConfigLoader().loadConfigOrThrow<H2Config>("/store.yaml")
        Database.connect(
            url = config.url,
            driver = config.driver,
            user = config.user,
            password = config.password
        )
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create( Accounts )
        }
    }

    fun register(email: String): Account =
        addAccount(
            Account(
                id = 0,
                license = UUID.randomUUID().toString(),
                pin = UUID.randomUUID().toString().substring(0, 7),
                email = email
            )
        )

    fun login(email: String, pin: String): Account =
        Accounts
            .selectAll()
            .where { (Accounts.email eq email) and (Accounts.pin eq pin) }
            .map { row ->
                Account(
                    id = row[Accounts.id],
                    license = row[Accounts.license],
                    pin = row[Accounts.pin],
                    email = row[Accounts.email]
                )
            }
            .single()


    fun listAccounts(): List<Account> =
        Accounts
            .selectAll()
            .map { row ->
                Account(
                    id = row[Accounts.id],
                    license = row[Accounts.license],
                    pin = row[Accounts.pin],
                    email = row[Accounts.email]
                )
            }

    fun addAccount(account: Account): Account =
        transaction {
            account.copy(id =
            Accounts.insert {
                it[id] = account.id
                it[license] = account.license
                it[pin] = account.pin
                it[email] = account.email
            } get Accounts.id
            )
        }

    fun updateAccount(account: Account): Int =
        transaction {
            Accounts.update({ Accounts.id eq account.id }) {
                it[license] = account.license
                it[pin] = account.pin
                it[email] = account.email
            }
        }
}