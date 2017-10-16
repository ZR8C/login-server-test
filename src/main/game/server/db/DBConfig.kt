package game.server.db

import game.server.model.Models
import io.requery.sql.KotlinConfiguration
import io.requery.sql.KotlinEntityDataStore
import io.requery.sql.SchemaModifier
import io.requery.sql.TableCreationMode
import org.h2.jdbcx.JdbcDataSource

class DBConfig {
    companion object {

        private val jdbcDataSource = JdbcDataSource()
        init {
            jdbcDataSource.setUrl("jdbc:h2:˜./test")
            jdbcDataSource.user = "test"
            jdbcDataSource.password = "test"
        }

        private val configuration = KotlinConfiguration(dataSource = jdbcDataSource, model = Models.DEFAULT)
        val data = KotlinEntityDataStore<Any>(configuration)

        init {
            SchemaModifier(jdbcDataSource, Models.DEFAULT).createTables(TableCreationMode.CREATE_NOT_EXISTS)
        }
    }
}