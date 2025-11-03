package com.example.lab3.ext

import io.r2dbc.spi.Row


fun Row.getLongSafe(column: String): Long? {
    return try {
        when {
            this.get(column) == null -> null
            this.get(column) is Number -> (this.get(column) as Number).toLong()
            else -> this.get(column, String::class.java)?.toLongOrNull()
        }
    } catch (e: Exception) {
        null
    }
}

fun Row.getStringSafe(column: String): String {
    return try {
        this.get(column, String::class.java) ?: ""
    } catch (e: Exception) {
        ""
    }
}

