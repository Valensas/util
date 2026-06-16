package com.valensas.util.pagination

import com.valensas.util.autoconfigure.PaginationAutoConfiguration
import com.valensas.util.exception.InvalidPageSize
import com.valensas.util.exception.InvalidSortDirection
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.util.UriBuilder
import java.io.Serializable
import kotlin.math.min

class Pageable(
    @RequestParam(required = false) val page: Int?,
    @RequestParam(required = false) val size: Int?,
    @RequestParam(required = false) val sort: String?
) : Serializable

fun Pageable.toJavaPageable(): PageRequest {
    val sortParam = parseParameterIntoSort(sort)
    val pageParam = this.page ?: 0
    val sizeParam = calculateSize(this.size)
    return PageRequest.of(pageParam, sizeParam, sortParam)
}

private fun calculateSize(size: Int?) =
    size
        .run { this ?: PaginationAutoConfiguration.defaultPageSize }
        .also { if (it < 1) throw InvalidPageSize() }
        .let { min(PaginationAutoConfiguration.maxPageSize, it) }

private fun parseParameterIntoSort(sort: String?): Sort {
    return sort
        ?.replace(" ", "")
        ?.split(";")
        ?.mapNotNull { part ->
            val element = part.split(",")
            val property = element.getOrNull(0) ?: return@mapNotNull null
            val direction =
                element.getOrNull(1)?.let {
                    try {
                        Sort.Direction.fromString(it)
                    } catch (ex: IllegalArgumentException) {
                        throw InvalidSortDirection(it)
                    }
                }

            if (!StringUtils.hasText(property)) {
                null
            } else {
                if (direction != null) Sort.Order(direction, property) else Sort.Order.by(property)
            }
        }?.takeIf { it.isNotEmpty() }
        ?.let { Sort.by(it) }
        ?: Sort.unsorted()
}

fun UriBuilder.pageable(pageable: Pageable) =
    this
        .queryParam("page", pageable.page)
        .queryParam("sort", pageable.sort)
        .queryParam("size", pageable.size)
