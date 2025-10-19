package com.example

import io.ktor.http.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(CIO, port = 9090, host = "0.0.0.0") {
        val pool = createDbPool()
        val cityService = CityService(pool)
        cityService.createTable()

        routing {

//            post("/cities") {
//                val city = call.receive<City>()
//                val id = cityService.create(city)
//                call.respond(HttpStatusCode.Created, id)
//            }

            get("/cities") {
                val cities = cityService.findAll()
                call.respond(HttpStatusCode.Created, cities.joinToString(" | "))
            }

            get("/create/city/{id}/{name}/{n}") {
                val id = call.parameters["id"]?.toLong() ?: 0L
                val name = call.parameters["name"] ?: "NULL"
                val n = call.parameters["n"]?.toLong() ?: 0L
                val city = City(id, name, n)
                cityService.add(city)
                call.respond(HttpStatusCode.Created, id)
            }

//            get("/cities/{id}") {
//                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
//                try {
//                    val city = cityService.read(id)
//                    call.respond(HttpStatusCode.OK, city)
//                } catch (e: Exception) {
//                    call.respond(HttpStatusCode.NotFound)
//                }
//            }

//            put("/cities/{id}") {
//                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
//                val user = call.receive<City>()
//                cityService.update(id, user)
//                call.respond(HttpStatusCode.OK)
//            }
//
//            delete("/cities/{id}") {
//                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
//                cityService.delete(id)
//                call.respond(HttpStatusCode.OK)
//            }
        }
    }.start(wait = true)
}
