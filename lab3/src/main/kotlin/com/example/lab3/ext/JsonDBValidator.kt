package com.example.lab3.ext

import com.example.lab3.entities.JsonDB
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono


@Component
class JsonDBValidator {
    companion object {
        private val ALLOWED_TYPES = mutableSetOf<String?>(
            "int", "integer", "bigint", "varchar", "text", "boolean", "timestamp"
        )
    }

    fun validate(request: JsonDB?): Mono<JsonDB?> {
        if (request == null) {
            return Mono.error(IllegalArgumentException("request is null"))
        }
        if (isBlank(request.table)) {
            return Mono.error(IllegalArgumentException("table name is empty"))
        }
        if (request.columns?.isEmpty() == null || request.columns.isEmpty()) {
            return Mono.error(IllegalArgumentException("columns list is empty"))
        }
        for (column in request.columns) {
            if (isBlank(column?.name)) {
                return Mono.error(IllegalArgumentException("column with empty name"))
            }
            if (!ALLOWED_TYPES.contains(column?.type?.lowercase())) {
                return Mono.error(IllegalArgumentException("unsupported type: " + column?.type))
            }
        }

        if (request.rows != null && !request.rows.isEmpty()) {
            val colNames: Set<String?> = request.columns.map { it?.name }.toSet()
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