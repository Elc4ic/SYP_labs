package com.example

import kotlinx.serialization.Serializable

@Serializable
data class City(
    val id: Long? = null,
    val name: String,
    val population: Long? = 0
)