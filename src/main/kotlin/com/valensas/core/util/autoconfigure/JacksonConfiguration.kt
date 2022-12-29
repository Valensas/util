package com.valensas.core.util.autoconfigure

import com.fasterxml.jackson.databind.SerializationFeature
import com.valensas.core.util.serializer.BigDecimalMoneyDeserializer
import com.valensas.core.util.serializer.InstantSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.math.BigDecimal
import java.time.Instant

@Configuration
class JacksonConfiguration {
    @Bean
    @Primary
    fun jackson2ObjectMapperBuilder(
        @Value("\${valensas.server.bigdecimal.scale:8}")
        scale: Int
    ): Jackson2ObjectMapperBuilder = Jackson2ObjectMapperBuilder()
        .deserializerByType(BigDecimal::class.java, BigDecimalMoneyDeserializer(scale))
        .serializerByType(Instant::class.java, InstantSerializer())
        .createXmlMapper(false)
        .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
        .featuresToEnable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
}
