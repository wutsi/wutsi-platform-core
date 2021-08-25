package com.wutsi.platform.core.tracing.servlet

import com.wutsi.platform.core.tracing.DeviceIdProvider
import com.wutsi.platform.core.tracing.TracingContext
import javax.servlet.http.HttpServletRequest

class HttpTracingContext {
    fun traceId(request: HttpServletRequest): String? =
        request.getHeader(TracingContext.HEADER_TRACE_ID)
            ?: request.getHeader(TracingContext.HEADER_HEROKU_REQUEST_ID)

    fun clientId(request: HttpServletRequest): String? =
        request.getHeader(TracingContext.HEADER_CLIENT_ID)

    fun deviceId(request: HttpServletRequest, deviceIdProvider: DeviceIdProvider): String? =
        deviceIdProvider.get(request)
}
