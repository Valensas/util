package com.valensas.common.util.extension

import java.math.BigDecimal

fun BigDecimal.isZero() = this.compareTo(BigDecimal.ZERO) == 0
