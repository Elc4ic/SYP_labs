package com.example

import io.r2dbc.pool.ConnectionPool
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitLast
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactive.collect

class CityService(private val connectionPool: ConnectionPool) {

    companion object {
        val INIT_DB_IF_NOT_EXISTS = """
                CREATE TABLE IF NOT EXISTS cities (
                    id BIGSERIAL PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    population INTEGER NOT NULL
                );
                ALTER SEQUENCE cities_id_seq RESTART WITH 1;
            """.trimIndent()

        val SELECT_ALL = "SELECT id, name, population FROM cities ORDER BY id"

        val INSERT_CITY =
            "INSERT INTO cities (name, population) VALUES (\$1, \$2) RETURNING id, name, population"

        val EXIST_BY_ID = "SELECT 1 FROM cities WHERE id = \$1"
    }

    suspend fun createTable() = withConnection { connection ->
        connection.createStatement(INIT_DB_IF_NOT_EXISTS)
            .execute()
            .awaitLast()
    }

    suspend fun findAll(): List<City> = withConnection { connection ->
        val cities = mutableListOf<City>()
        connection.createStatement(SELECT_ALL)
            .execute().awaitSingle()
            .map { row, _ ->
                City(
                    id = row.getLongSafe("id"),
                    name = row.getStringSafe("name"),
                    population = row.getLongSafe("population")
                )
            }.collect { user -> cities.add(user) }
        cities
    }

    suspend fun add(city: City): City = withConnection { connection ->
        var savedUser: City? = null
        connection.createStatement(INSERT_CITY)
            .bind(0, city.name)
            .bind(1, city.population ?: 0)
            .execute().awaitFirst()
            .map { row, _ ->
                City(
                    id = row.getLongSafe("id"),
                    name = row.getStringSafe("name"),
                    population = row.getLongSafe("population")
                )
            }.collect { savedUser = it }

        savedUser ?: throw RuntimeException("Failed to save user")
    }

    suspend fun existById(id: Long): Boolean = withConnection { connection ->
        connection.createStatement(EXIST_BY_ID)
            .bind(0, id)
            .execute().awaitFirst() != null
    }

    private suspend fun <T> withConnection(block: suspend (io.r2dbc.spi.Connection) -> T): T {
        val connection = connectionPool.create().awaitSingle()
        try {
            return block(connection)
        } finally {
            connection.close()
        }
    }
}

