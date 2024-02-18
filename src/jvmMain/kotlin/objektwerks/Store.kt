package objektwerks

import com.sksamuel.hoplite.ConfigLoader

import java.util.UUID

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

data class StoreConfig(val url: String,
                       val driver: String,
                       val user: String,
                       val password: String) {
    companion object {
        fun load(resource: String): StoreConfig = ConfigLoader().loadConfigOrThrow<StoreConfig>(resource)
    }
}

object Accounts : Table() {
    val id: Column<Id> = long("id").autoIncrement()
    val license: Column<License> = varchar("license", 36)
    val pin: Column<Pin> = varchar("pin", 7)
    val email: Column<Email> = varchar("email", 128)
    val created: Column<Long> = long("created")
    override val primaryKey = PrimaryKey(id, name = "id")
}

class Store(private val config: StoreConfig) {
    init {
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

    private fun registerAccount(account: Account): Account =
        transaction {
            account.copy(id =
                Accounts.insert {
                    it[license] = account.license
                    it[pin] = account.pin
                    it[email] = account.email
                    it[created] = account.created
                } get Accounts.id
            )
        }


    fun register(email: String): Account =
        registerAccount(
            Account(
                id = 0,
                license = UUID.randomUUID().toString(),
                pin = UUID.randomUUID().toString().substring(0, 7),
                email = email
            )
        )

    fun login(email: String, pin: String): Account =
        transaction {
            Accounts
                .selectAll()
                .where { (Accounts.email eq email) and (Accounts.pin eq pin) }
                .map { row ->
                    Account(
                        id = row[Accounts.id],
                        license = row[Accounts.license],
                        pin = row[Accounts.pin],
                        email = row[Accounts.email],
                        created = row[Accounts.created]
                    )
                }
                .single()
        }


    fun listAccounts(): List<Account> =
        transaction {
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
        }

    fun updateAccount(account: Account): Int =
        transaction {
            Accounts.update({ Accounts.id eq account.id }) {
                it[email] = account.email
            }
        }
}