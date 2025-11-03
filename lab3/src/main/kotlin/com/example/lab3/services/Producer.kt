package com.example.lab3.services

import com.example.lab3.entities.JsonDB
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.concurrent.TimeUnit

@Component
class Producer {

    private val prop = mutableMapOf(
        "key.serializer" to "org.apache.kafka.common.serialization.StringSerializer",
        "value.serializer" to "org.springframework.kafka.support.serializer.JsonSerializer",
        "bootstrap.servers" to "kafka:9092"
    )
    private val producer = KafkaProducer<String, JsonDB>(prop as Map<String, Any>?)
    fun send(payload: JsonDB): Mono<Void?> {
        val record: ProducerRecord<String, JsonDB> = ProducerRecord("test-topic", payload)
        return Mono.from<RecordMetadata> { producer.send(record) }.then()
    }
}