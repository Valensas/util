package com.valensas.util.autoconfigure

import com.valensas.util.serializer.BigDecimalMoneyDeserializer
import com.valensas.util.serializer.InstantSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tools.jackson.databind.DeserializationFeature
import tools.jackson.databind.cfg.DateTimeFeature
import tools.jackson.databind.cfg.EnumFeature
import tools.jackson.databind.module.SimpleModule
import java.math.BigDecimal
import java.time.Instant

@Configuration
class JacksonConfiguration {
    @Bean
    fun valensasJsonMapperBuilderCustomizer(
        @Value("\${valensas.server.bigdecimal.scale:8}")
        scale: Int
    ): JsonMapperBuilderCustomizer = JsonMapperBuilderCustomizer { builder ->
        val module = SimpleModule()
            .addDeserializer(BigDecimal::class.java, BigDecimalMoneyDeserializer(scale))
            .addSerializer(Instant::class.java, InstantSerializer())
        builder
            .addModule(module)
            .disable(
                DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS,
                DateTimeFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS,
                DateTimeFeature.WRITE_DURATIONS_AS_TIMESTAMPS
            ).enable(EnumFeature.WRITE_ENUMS_USING_TO_STRING)
            .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
    }
}
