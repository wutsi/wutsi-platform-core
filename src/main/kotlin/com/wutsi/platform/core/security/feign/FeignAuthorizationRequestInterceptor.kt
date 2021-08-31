package com.wutsi.platform.core.security.feign

import com.wutsi.platform.core.security.TokenProvider
import feign.RequestInterceptor
import feign.RequestTemplate

class FeignAuthorizationRequestInterceptor(
    private val tokenProvider: TokenProvider
) : RequestInterceptor {
    override fun apply(request: RequestTemplate) {
        val jwt = tokenProvider.geToken()
        if (jwt != null)
            request.header("Authorization", "Bearer $jwt")
    }
}
