package com.wutsi.platform.core.tracing.feign

import com.wutsi.platform.core.tracing.TracingContext
import feign.RequestInterceptor
import feign.RequestTemplate

class FeignTracingRequestInterceptor(
    private val clientId: String,
    private val tracingContext: TracingContext
) : RequestInterceptor {
    override fun apply(template: RequestTemplate) {
        template.header(TracingContext.HEADER_CLIENT_ID, clientId)
        template.header(TracingContext.HEADER_DEVICE_ID, tracingContext.deviceId())
        template.header(TracingContext.HEADER_TRACE_ID, tracingContext.traceId())

        /**
         * For Heroku integration. See https://devcenter.heroku.com/articles/http-request-id
         */
        template.header(TracingContext.HEADER_HEROKU_REQUEST_ID, tracingContext.traceId())
    }
}
