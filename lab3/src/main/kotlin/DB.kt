package com.example

import io.r2dbc.pool.ConnectionPool
import io.r2dbc.pool.ConnectionPoolConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import java.time.Duration

fun createDbPool(): ConnectionPool {
    val connectionFactory =
        PostgresqlConnectionFactory(
            PostgresqlConnectionConfiguration.builder()
                .host(Config.Db.host)
                .port(Config.Db.port)
                .database(Config.Db.database)
                .username(Config.Db.user)
                .password(Config.Db.password)
                .build()
        )

    val poolConfig = ConnectionPoolConfiguration.builder()
        .connectionFactory(connectionFactory)
        .maxIdleTime(Duration.ofMinutes(30))
        .maxSize(Config.DbPool.maxSize)
        .initialSize(Config.DbPool.initialSize)
        .build()

    return ConnectionPool(poolConfig)
}


