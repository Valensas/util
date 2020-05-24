package com.valensas.common.util.autoconfigure

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.valensas.common.util.serializer.BigDecimalMoneyDeserializer
import com.valensas.common.util.serializer.InstantSerializer
import java.math.BigDecimal
import java.time.Instant
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

@Configuration
@ConditionalOnMissingBean(ObjectMapper::class)
class Jackson2ObjectConfiguration {
    @Bean
    @ConditionalOnMissingBean
    fun objectMapper(
        builder: Jackson2ObjectMapperBuilder,
        @Value("\${valensas.server.bigdecimal.scale:8}")
        scale: Int
    ): ObjectMapper {

        val objectMapper: ObjectMapper = builder.createXmlMapper(false).build()

        objectMapper.registerModule(KotlinModule())
        objectMapper.registerModule(JavaTimeModule())

        val bigDecimalModule = SimpleModule("big-decimal-mappers")
            .addDeserializer(BigDecimal::class.java, BigDecimalMoneyDeserializer(scale))

        val instantModule = SimpleModule("instant-module")
            .addSerializer(Instant::class.java, InstantSerializer())

        objectMapper.registerModule(instantModule)
        objectMapper.registerModule(bigDecimalModule)

        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)

        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        objectMapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)

        return objectMapper
    }
}
