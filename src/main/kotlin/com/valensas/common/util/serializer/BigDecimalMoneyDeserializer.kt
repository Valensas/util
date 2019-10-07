package com.valensas.common.util.serializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.io.IOException
import java.math.BigDecimal

class BigDecimalMoneyDeserializer : JsonDeserializer<BigDecimal>() {

    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(jp: JsonParser, ctx: DeserializationContext): BigDecimal {
        return jp.decimalValue.setScale(2, BigDecimal.ROUND_HALF_UP)
    }
}