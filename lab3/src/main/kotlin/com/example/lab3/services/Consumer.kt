package com.example.lab3.services

import com.example.lab3.entities.JsonDB
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class MyConsumer(
    private val db: DBService,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {

    val handler = CoroutineExceptionHandler { _, ex ->
        logger.error("import failed", ex)
    }

    @KafkaListener(topics = ["test-topic"], groupId = "test_id",containerFactory = "kafkaListenerContainerFactory")
    fun listen(json: JsonDB) {
        scope.launch(handler) {
            db.createTable(json)
            db.fillTable(json)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(MyConsumer::class.java)
    }
}