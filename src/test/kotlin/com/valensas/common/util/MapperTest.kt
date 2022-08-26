package com.valensas.common.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.valensas.common.util.autoconfigure.JacksonConfiguration
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal

@ExtendWith(MockitoExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MapperTest {
    private val mapper = JacksonConfiguration()
        .jackson2ObjectMapperBuilder(8)
        .build<ObjectMapper>()

    @Test
    fun `serialize and deserialize bigdecimal`() {
        val decimalString = "123.3214324123412349127840932"
        val decimal = BigDecimal(decimalString)

        val serializedDecimal = mapper.writeValueAsString(decimal)
        Assertions.assertEquals(decimalString, serializedDecimal)

        val deserializedDecimal = mapper.readValue(serializedDecimal, BigDecimal::class.java)
        Assertions.assertEquals(BigDecimal("123.32143241"), deserializedDecimal)
    }
}
