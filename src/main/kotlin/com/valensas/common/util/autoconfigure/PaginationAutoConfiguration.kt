package com.valensas.common.util.autoconfigure

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.util.UriBuilder
import kotlin.math.min

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

class Pageable(
    @RequestParam(required = false) val page: Int?,
    @RequestParam(required = false) val size: Int?,
    @RequestParam(required = false) val sort: String?
)

fun Pageable.toJavaPageable(): PageRequest {
    val sortParam = parseParameterIntoSort(sort)
    val pageParam = this.page ?: 0
    val sizeParam = calculateSize(this.size)
    return PageRequest.of(pageParam, sizeParam, sortParam)
}

private fun calculateSize(size: Int?) = size
    .run { this ?: PaginationAutoConfiguration.defaultPageSize }
    .let { if (it < 1) PaginationAutoConfiguration.defaultPageSize else it }
    .let { min(PaginationAutoConfiguration.maxPageSize, it) }

private fun parseParameterIntoSort(
    sort: String?
): Sort {
    return sort?.replace(" ", "")
        ?.split(";")
        ?.mapNotNull { part ->
            val element = part.split(",")
            val property = element.getOrNull(0) ?: return@mapNotNull null
            val direction = element.getOrNull(1)?.let(Sort.Direction::fromString)

            if (!StringUtils.hasText(property)) {
                null
            } else {
                if (direction != null) Sort.Order(direction, property) else Sort.Order.by(property)
            }
        }
        ?.takeIf { it.isNotEmpty() }
        ?.let { Sort.by(it) }
        ?: Sort.unsorted()
}

fun UriBuilder.pageable(pageable: Pageable) = this.queryParam("page", pageable.page)
    .queryParam("sort", pageable.sort)
    .queryParam("size", pageable.size)
