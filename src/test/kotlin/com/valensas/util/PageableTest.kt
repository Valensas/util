package com.valensas.util

import com.valensas.util.exception.InvalidPageNumber
import com.valensas.util.exception.InvalidPageSize
import com.valensas.util.exception.InvalidSortDirection
import com.valensas.util.pagination.Pageable
import com.valensas.util.pagination.toJavaPageable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
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
        assertNotNull(page)
        assertEquals(Sort.unsorted(), page.sort)
        assertEquals(0, page.pageNumber)
        assertEquals(Pageable.defaultPageSize, page.pageSize)
    }

    @Test
    fun `initiate pageable with correct values`() {
        val pageable =
            Pageable(1, 5, "created_date,desc")
        val page = pageable.toJavaPageable()
        assertNotNull(page)
        assertNotNull(page.sort)
        assertNotNull(page.sort.getOrderFor("created_date"))
        assertEquals(Sort.Direction.DESC, page.sort.getOrderFor("created_date")!!.direction)
        assertEquals(1, page.pageNumber)
        assertEquals(min(pageable.size!!, Pageable.defaultPageSize), page.pageSize)
    }

    @Test
    fun `try to initiate javaPageable with invalid size`() {
        assertThrows<InvalidPageSize> {
            Pageable(null, -1, null)
        }
    }

    @Test
    fun `try to initiate javaPageable with invalid page number`() {
        assertThrows<InvalidPageNumber> {
            Pageable(-1, null, null)
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
