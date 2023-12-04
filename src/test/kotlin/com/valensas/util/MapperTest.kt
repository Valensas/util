package com.valensas.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.valensas.util.autoconfigure.JacksonConfiguration
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal

@ExtendWith(MockitoExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MapperTest {
    private val mapper =
        JacksonConfiguration()
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

    @Test
    fun `Fail to create limit order with zero limit`() {
        val bodyWithNoNullValue = "{ \"id1\": 1, \"id2\": 1 }"
        val objectWithNoNullValue = mapper.readValue(bodyWithNoNullValue, NullPrimitiveTest::class.java)
        Assertions.assertEquals(1, objectWithNoNullValue.id1)
        Assertions.assertEquals(1, objectWithNoNullValue.id2)

        val bodyWithNullableFieldNull = "{ \"id1\": null, \"id2\": 1 }"
        val objectWithNullableFieldNull = mapper.readValue(bodyWithNullableFieldNull, NullPrimitiveTest::class.java)
        Assertions.assertEquals(null, objectWithNullableFieldNull.id1)
        Assertions.assertEquals(1, objectWithNullableFieldNull.id2)

        val bodyWithNonNullableFieldNull = "{ \"id1\": 2, \"id2\": null }"
        assertThrows<MismatchedInputException> {
            mapper.readValue(bodyWithNonNullableFieldNull, NullPrimitiveTest::class.java)
        }
    }
}

data class NullPrimitiveTest(
    val id1: Int?,
    val id2: Int
) {
    constructor() : this(null, 0)
}
