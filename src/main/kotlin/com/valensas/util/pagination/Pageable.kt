package com.valensas.util.pagination

import com.valensas.util.exception.InvalidPageNumber
import com.valensas.util.exception.InvalidPageSize
import com.valensas.util.exception.InvalidSortDirection
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.util.UriBuilder
import java.io.Serializable
import java.util.Optional

class Pageable(
    @RequestParam(required = false) var page: Int?,
    @RequestParam(required = false) var size: Int?,
    @RequestParam(required = false) val sort: String?
) : Serializable {
    companion object {
        var defaultPageSize = 20
        var maxPageSize = 1000
    }

    init {
        size = size ?: defaultPageSize
        size?.let {
            if (it < 0) throw InvalidPageSize(maxPageSize)
            if (it > maxPageSize) throw InvalidPageSize(maxPageSize)
        }
        page = page ?: 0

        page?.let { if (it < 0) throw InvalidPageNumber() }
    }
}

fun Pageable.toJavaPageable(): PageRequest {
    val sortParam = parseParameterIntoSort(sort)
    val pageParam = this.page ?: 0
    return PageRequest.of(pageParam, this.size ?: Pageable.defaultPageSize, sortParam)
}

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

fun UriBuilder.pageable(pageable: Pageable) = this
    .queryParamIfPresent("page", Optional.ofNullable(pageable.page))
    .queryParamIfPresent("sort", Optional.ofNullable(pageable.sort))
    .queryParamIfPresent("size", Optional.ofNullable(pageable.size))
