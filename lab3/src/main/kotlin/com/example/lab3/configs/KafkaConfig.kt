package com.example.lab3.configs

import com.example.lab3.entities.JsonDB
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer
import org.springframework.util.backoff.ExponentialBackOff

@Configuration
class KafkaConfiguration {

    @Bean
    fun kafkaTemplate(producerFactory: ProducerFactory<String, JsonDB>): KafkaTemplate<String, JsonDB> {
        return KafkaTemplate(producerFactory)
    }

    @Bean
    fun errorHandler(
        kafkaTemplate: KafkaTemplate<String, JsonDB>
    ): DefaultErrorHandler {
        val backoff = ExponentialBackOff().apply {
            initialInterval = 1000L
            multiplier = 2.0
            maxInterval = 5000L
            maxElapsedTime = 10000L
            maxAttempts = 3
        }

        val recoverer = DeadLetterPublishingRecoverer(kafkaTemplate)

        val handler = DefaultErrorHandler(recoverer, backoff)
        handler.addNotRetryableExceptions(IllegalArgumentException::class.java)
        handler.setRetryListeners({ record, ex, deliveryAttempt ->
            logger.warn("Retrying message: ${record.value()} due to ${ex.message} (attempt $deliveryAttempt)")
        })
        return handler
    }

    @Bean
    fun consumerFactory(): ConsumerFactory<String, JsonDB> {
        val props = mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "kafka:9092",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JsonDeserializer::class.java,
            JsonDeserializer.TRUSTED_PACKAGES to "*"
        )

        return DefaultKafkaConsumerFactory(
            props,
            StringDeserializer(),
            JsonDeserializer(JsonDB::class.java)
        )
    }

    @Bean
    fun kafkaListenerContainerFactory(
        consumerFactory: ConsumerFactory<String, JsonDB>,
        errorHandler: DefaultErrorHandler
    ): ConcurrentKafkaListenerContainerFactory<String, JsonDB> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, JsonDB>()
        factory.consumerFactory = consumerFactory
        factory.containerProperties.ackMode = ContainerProperties.AckMode.MANUAL
        factory.setCommonErrorHandler(errorHandler)

        return factory
    }

    companion object {
        private val logger = LoggerFactory.getLogger(KafkaConfiguration::class.java)
    }
}