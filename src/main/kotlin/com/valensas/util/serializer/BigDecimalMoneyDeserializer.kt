package com.valensas.util.serializer

import tools.jackson.core.JsonParser
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.ValueDeserializer
import java.math.BigDecimal
import java.math.RoundingMode

class BigDecimalMoneyDeserializer(
    private val scale: Int
) : ValueDeserializer<BigDecimal>() {
    override fun deserialize(
        jp: JsonParser,
        ctx: DeserializationContext
    ): BigDecimal = BigDecimal(jp.valueAsString).setScale(scale, RoundingMode.HALF_UP)
}
