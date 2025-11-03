package com.example.lab3.services

import com.example.lab3.entities.JsonDB
import io.r2dbc.pool.ConnectionPool
import io.r2dbc.spi.Connection
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitLast
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactive.collect
import org.springframework.stereotype.Service

@Service
class DBService(private val connectionPool: ConnectionPool) {

    suspend fun createTable(json: JsonDB) = withConnection { connection ->
        connection.createStatement(json.toCreateSql())
            .execute()
            .awaitLast()
    }

    suspend fun fillTable(json: JsonDB) = withConnection { connection ->
        json.toInsertSql().forEach {
            connection.createStatement(it)
                .execute()
                .awaitLast()
        }
    }

    private suspend fun <T> withConnection(block: suspend (Connection) -> T): T {
        val connection = connectionPool.create().awaitSingle()
        try {
            return block(connection)
        } finally {
            println("Connection close: ${connection.metadata.databaseProductName}")
            println("Connections in pool: ${connectionPool.metrics.get().allocatedSize()}")
            connection.close().awaitFirstOrNull()
        }
    }

}