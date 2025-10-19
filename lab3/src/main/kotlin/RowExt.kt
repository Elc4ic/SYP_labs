package com.example

fun io.r2dbc.spi.Row.getLongSafe(column: String): Long? {
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

fun io.r2dbc.spi.Row.getStringSafe(column: String): String {
    return try {
        this.get(column, String::class.java) ?: ""
    } catch (e: Exception) {
        ""
    }
}