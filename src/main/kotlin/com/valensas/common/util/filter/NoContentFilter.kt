package com.valensas.common.util.filter

import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.web.filter.OncePerRequestFilter

class NoContentFilter : OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        filterChain: FilterChain
    ) {
        filterChain.doFilter(httpServletRequest, httpServletResponse)
        if ((httpServletResponse.contentType == null || httpServletResponse.contentType == "") && httpServletResponse.status == HttpStatus.OK.value()) {
            httpServletResponse.status = HttpStatus.NO_CONTENT.value()
        }
    }
}
