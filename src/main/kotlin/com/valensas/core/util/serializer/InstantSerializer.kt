package com.valensas.core.util.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder

class InstantSerializer : JsonSerializer<Instant>() {
    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatterBuilder().appendInstant(3).toFormatter()

    override fun serialize(value: Instant?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.writeString(dateTimeFormatter.format(value))
    }
}
