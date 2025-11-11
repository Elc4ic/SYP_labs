package com.example.lab3.entities


data class JsonDB(
    val table: String?,
    val columns: MutableList<Column?>?,
    val rows: MutableList<MutableMap<String?, Any?>?>?
) {
    data class Column(val name: String?, val type: String?)

    fun toCreateSql(): String {
        require(columns != null)
        val cols = columns.joinToString(", ") { c -> c?.name + " " + c?.type }
        return "CREATE TABLE IF NOT EXISTS $table ($cols)"
    }

    fun toInsertSql(): List<String> {
        require(rows != null)
        return rows.map { row ->
            val cols = row?.keys?.joinToString(",")
            val vals = row?.values?.joinToString(",")
            "INSERT INTO $table ($cols) VALUES ($vals)"
        }
    }
}