package com.wutsi.platform.core.logging

import com.wutsi.platform.core.tracing.TracingContext
import java.io.IOException
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class KVLoggerFilter(
    private val kv: KVLogger,
    private val tracingContext: TracingContext
) : Filter {
    @Throws(ServletException::class)
    override fun init(filterConfig: FilterConfig) {
        // Empty
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {

        val startTime = System.currentTimeMillis()
        try {

            filterChain.doFilter(servletRequest, servletResponse)
            log(startTime, (servletResponse as HttpServletResponse).status, servletRequest as HttpServletRequest, kv)
            kv.log()
        } catch (e: Exception) {
            log(startTime, 500, servletRequest as HttpServletRequest, kv)
            kv.log(e)
            throw e
        }
    }

    override fun destroy() {
        // Empty
    }

    private fun log(
        startTime: Long,
        status: Int,
        request: HttpServletRequest,
        kv: KVLogger
    ) {
        val latencyMillis = System.currentTimeMillis() - startTime

        kv.add("success", status / 100 == 2)
        kv.add("latency_millis", latencyMillis)

        kv.add("http_status", status.toLong())
        kv.add("http_endpoint", request.requestURI)
        kv.add("http_method", request.method)
        kv.add("trace_id", tracingContext.traceId())
        kv.add("client_id", tracingContext.clientId())
        kv.add("device_id", tracingContext.deviceId())

        val params = request.parameterMap
        params.keys.forEach { kv.add("http_param_$it", params[it]?.toList()) }
    }
}
