package com.valensas.common.util.autoconfigure

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class PaginationAutoConfiguration(
    @Value("\${spring.data.rest.max-page-size:50}") private val maxPageSize: Int,
    @Value("\${spring.data.rest.default-page-size:20}") private val defaultPageSize: Int
) {
    init {
        Companion.defaultPageSize = defaultPageSize
        Companion.maxPageSize = maxPageSize
    }

    companion object {
        var defaultPageSize = 20
        var maxPageSize = 50
    }
}
