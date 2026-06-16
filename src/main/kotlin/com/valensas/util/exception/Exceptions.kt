package com.valensas.util.exception

import com.valensas.exception.BadRequest

class InvalidPageSize(
    max: Int
) : BadRequest(
        "Page size must be between 0 and $max",
        "INVALID_PAGE_SIZE"
    )

class InvalidPageNumber :
    BadRequest(
        "Page number must not be less than zero",
        "INVALID_PAGE_NUMBER"
    )

class InvalidSortDirection(
    direction: String
) : BadRequest(
        "Invalid value:$direction, has to be either 'desc' or 'asc' (case insensitive)",
        "INVALID_SORT_DIRECTION"
    )
