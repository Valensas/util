package com.valensas.util

import com.valensas.util.autoconfigure.PaginationAutoConfiguration
import com.valensas.util.exception.InvalidPageSize
import com.valensas.util.exception.InvalidSortDirection
import com.valensas.util.pagination.Pageable
import com.valensas.util.pagination.toJavaPageable
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.Sort
import kotlin.math.min

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PageableTest {
    @Test
    fun `initiate pageable with null values`() {
        val pageable =
            Pageable(null, null, null)
        val page = pageable.toJavaPageable()
        Assertions.assertNotNull(page)
        Assertions.assertEquals(Sort.unsorted(), page.sort)
        Assertions.assertEquals(0, page.pageNumber)
        Assertions.assertEquals(PaginationAutoConfiguration.defaultPageSize, page.pageSize)
    }

    @Test
    fun `initiate pageable with correct values`() {
        val pageable =
            Pageable(1, 5, "created_date,desc")
        val page = pageable.toJavaPageable()
        Assertions.assertNotNull(page)
        Assertions.assertNotNull(page.sort)
        Assertions.assertNotNull(page.sort.getOrderFor("created_date"))
        Assertions.assertEquals(Sort.Direction.DESC, page.sort.getOrderFor("created_date")!!.direction)
        Assertions.assertEquals(1, page.pageNumber)
        Assertions.assertEquals(min(pageable.size!!, PaginationAutoConfiguration.defaultPageSize), page.pageSize)
    }

    @Test
    fun `try to initiate javaPageable with invalid size`() {
        val pageable =
            Pageable(null, 0, null)
        assertThrows<InvalidPageSize> {
            pageable.toJavaPageable()
        }
    }

    @Test
    fun `try to initiate javaPageable with invalid direction`() {
        val pageable =
            Pageable(null, 1, "created_date,descx")
        assertThrows<InvalidSortDirection> {
            pageable.toJavaPageable()
        }
    }
}
