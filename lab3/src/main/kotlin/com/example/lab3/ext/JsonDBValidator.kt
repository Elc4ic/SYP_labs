package com.example.lab3.ext

import com.example.lab3.entities.JsonDB
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.lang.Exception


@Component
class JsonDBValidator {
    companion object {
        private val ALLOWED_TYPES = mutableSetOf<String?>(
            "int", "integer", "bigint", "varchar(n)", "text", "boolean", "timestamp"
        )
        val TYPE_PATTERNS = mapOf(
            "varchar" to Regex("varchar\\((\\d+)\\)"), "decimal" to Regex("decimal\\((\\d+),(\\d+)\\)")
        )
    }

    fun validate(request: JsonDB?): Mono<JsonDB?> {
        try {
            require(request != null)
            require(isBlank(request.table))
            require(request.columns?.isNotEmpty() == true)
            for (column in request.columns) {
                require(!isBlank(column?.name))
                val lowerType = column?.type?.lowercase()!!
                require(
                    ALLOWED_TYPES.contains(lowerType) || TYPE_PATTERNS.any { (baseType, pattern) ->
                        pattern.matches(lowerType) || lowerType.startsWith("$baseType(")
                    })
            }
        } catch (e: Exception) {
            return Mono.error(IllegalArgumentException("not valid json"))
        }

        if (request.rows != null && !request.rows.isEmpty()) {
            val colNames: Set<String?> = request.columns?.map { it?.name }!!.toSet()
            for (row in request.rows) {
                if (colNames != row?.keys) {
                    return Mono.error(IllegalArgumentException("row keys do not match columns"))
                }
            }
        }
        return Mono.just(request)
    }

    private fun isBlank(s: String?): Boolean {
        return s == null || s.trim { it <= ' ' }.isEmpty()
    }
}


fun main() {
    val ALLOWED = mutableSetOf<String?>(
        "int", "integer", "bigint", "varchar", "text", "boolean", "timestamp"
    )
    val column1 = JsonDB.Column("dd", "text")
    val column2 = JsonDB.Column("ss", "varchar(n)")
    if (ALLOWED.contains(column1.type?.lowercase())) {
        print("allow1")
    }
    if (ALLOWED.contains(column2.type?.lowercase())) {
        print("allow2")
    }
}