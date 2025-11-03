package com.example.lab3.configs

import com.example.lab3.ext.Envy
import io.r2dbc.pool.ConnectionPool
import io.r2dbc.pool.ConnectionPoolConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
class R2DBCConfig {
    @Bean
    fun connectionFactory(): ConnectionFactory {
        return PostgresqlConnectionFactory(
            PostgresqlConnectionConfiguration.builder()
                .host(Envy.Db.host)
                .port(Envy.Db.port)
                .database(Envy.Db.database)
                .username(Envy.Db.user)
                .password(Envy.Db.password)
                .build()
        )
    }

    @Bean
    fun connectionPool(): ConnectionPool {
        return ConnectionPool(
            ConnectionPoolConfiguration.builder()
                .connectionFactory(connectionFactory())
                .maxIdleTime(Duration.ofMinutes(30))
                .maxSize(Envy.DbPool.maxSize)
                .initialSize(Envy.DbPool.initialSize)
                .build()
        )
    }
}