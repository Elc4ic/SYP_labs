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
                .host(Envy.Db.host)
                .port(Envy.Db.port)
                .database(Envy.Db.database)
                .username(Envy.Db.user)
                .password(Envy.Db.password)
                .build()
        )

    val poolConfig = ConnectionPoolConfiguration.builder()
        .connectionFactory(connectionFactory)
        .maxIdleTime(Duration.ofMinutes(30))
        .maxSize(Envy.DbPool.maxSize)
        .initialSize(Envy.DbPool.initialSize)
        .build()

    return ConnectionPool(poolConfig)
}


