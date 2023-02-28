// ktlint-disable filename
package com.valensas.util.extension

import java.math.BigDecimal

fun BigDecimal.isZero() = this.compareTo(BigDecimal.ZERO) == 0
fun BigDecimal?.isNullOrZero() = (this == null) || isZero()
