package com.valensas.util.serializer

import tools.jackson.core.JsonGenerator
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueSerializer
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder

class InstantSerializer : ValueSerializer<Instant>() {
    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatterBuilder().appendInstant(3).toFormatter()

    override fun serialize(
        value: Instant,
        gen: JsonGenerator,
        ctxt: SerializationContext
    ) {
        gen.writeString(dateTimeFormatter.format(value))
    }
}
