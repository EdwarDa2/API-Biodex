package infrastructure.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import infrastructure.persistence.SpecimenImagesTable
import infrastructure.persistence.SpecimensTable
import infrastructure.persistence.TaxonomyTable
import io.ktor.server.application.*
import io.ktor.server.config.ApplicationConfig
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(config: ApplicationConfig) {

        val dbUrl = config.property("db.url").getString()
        val dbDriver = config.property("db.driver").getString()
        val dbUser = config.property("db.user").getString()
        val dbPassword = config.property("db.password").getString()
        val poolSize = config.property("db.poolSize").getString().toInt()


        Database.connect(createHikariDataSource(dbUrl, dbDriver, dbUser, dbPassword, poolSize))


        transaction {
            SchemaUtils.create(TaxonomyTable, SpecimensTable, SpecimenImagesTable)
        }
    }

    private fun createHikariDataSource(
        url: String,
        driver: String,
        user: String,
        password: String,
        poolSize: Int
    ): HikariDataSource {
        val config = HikariConfig().apply {
            driverClassName = driver
            jdbcUrl = url
            username = user
            this.password = password
            maximumPoolSize = poolSize
            validate()
        }
        return HikariDataSource(config)
    }
}