package com.valensas.util.exception

import com.valensas.exception.BadRequest

class InvalidPageSize :
    BadRequest(
        "Page size must not be less than one",
        "INVALID_PAGE_SIZE"
    )

class InvalidSortDirection(
    direction: String
) : BadRequest(
        "Invalid value:$direction, has to be either 'desc' or 'asc' (case insensitive)",
        "INVALID_SORT_DIRECTION"
    )
