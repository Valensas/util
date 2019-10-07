package com.valensas.common.util.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder

class InstantSerializer : JsonSerializer<Instant>() {

    override fun serialize(value: Instant?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.writeString(
            dateTimeFormatter.format(value)
        )
    }

    companion object {
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormatterBuilder().appendInstant(3).toFormatter()
    }
}