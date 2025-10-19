package com.example

import io.r2dbc.postgresql.client.SSLMode


data object Config {
    val schema: String = System.getenv("SCHEMA_NAME")?.validSqlName ?: "shurl"
    val tableUrls: String = System.getenv("TABLE_NAME_URLS")?.validSqlName ?: "urls"
    val tableVisits: String = System.getenv("TABLE_NAME_VISITS")?.validSqlName ?: "visits"
    val originalUrlMaxSize: Int = System.getenv("ORIGINAL_URL_MAX_SIZE")?.toInt() ?: 512
    val urlIdSize: Int = System.getenv("URL_ID_SIZE")?.toInt() ?: 6
    val urlCharSet: Set<Char> = System.getenv("URL_CHARSET")?.replace("'", "''")?.toSet()
        ?: "23456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz".toSet()

    object Db {
        val host = System.getenv("DB_HOST") ?: "db"
        val port = System.getenv("DB_PORT")?.toInt() ?: 5432
        val database = System.getenv("DB_DATABASE") ?: "lab3db"
        val user = System.getenv("DB_USER") ?: "boarduser"
        val password = System.getenv("DB_PASSWORD") ?: "boardpass"
    }

    object DbPool {
        val initialSize = System.getenv("DB_POOL_INITIAL_SIZE")?.toInt() ?: 1
        val maxSize = System.getenv("DB_POOL_MAX_SIZE")?.toInt() ?: 5
    }

    private val String.validSqlName: String
        get() {
            if (!matches(Regex("^[a-zA-Z0-9_]+$"))) error("Invalid SQL name: '$this'")
            return this
        }
}