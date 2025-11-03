package com.example.lab3.ext

data object Envy {

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
}