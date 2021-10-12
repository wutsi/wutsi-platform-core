package com.wutsi.platform.core.security.servlet

import com.wutsi.platform.core.tracing.TracingContext
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

class CorsFilter : Filter {
    override fun doFilter(
        req: ServletRequest,
        resp: ServletResponse,
        chain: FilterChain
    ) {
        (resp as javax.servlet.http.HttpServletResponse).addHeader(
            "Access-Control-Allow-Origin",
            "*"
        )
        resp.addHeader(
            "Access-Control-Allow-Methods",
            "GET, OPTIONS, HEAD, PUT, POST, DELETE"
        )
        resp.addHeader(
            "Access-Control-Allow-Headers",
            allowHeaders().joinToString(separator = ",")
        )
        chain.doFilter(req, resp)
    }

    private fun allowHeaders() = listOf(
        "Content-Type",
        "Authorization",
        "Content-Length",
        "Accept-Language",
        "X-Api-Key",
        "X-Requested-With",
        TracingContext.HEADER_CLIENT_ID,
        TracingContext.HEADER_DEVICE_ID,
        TracingContext.HEADER_TRACE_ID
    )
}
