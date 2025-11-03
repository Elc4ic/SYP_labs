package com.example.lab3.controllers

import com.example.lab3.entities.JsonDB
import com.example.lab3.ext.JsonDBValidator
import com.example.lab3.services.Producer
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono


@RestController
@RequestMapping("lab3")
class SimpleController(
    private val producer: Producer,
    private val validator: JsonDBValidator,
) {

    @GetMapping
    fun test():String {
        return "Controller working"
    }

    @PostMapping("/import")
    fun importJson(@RequestBody json: JsonDB?): Mono<Void> {
        return validator.validate(json)
            .flatMap { validJson -> validJson?.let { producer.send(it) } }
    }
}