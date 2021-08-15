package com.wutsi.platform.core.tracing.servlet

import com.wutsi.platform.core.tracing.DeviceIdProvider
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

open class DeviceIdProviderCookie(private val cookieName: String) : DeviceIdProvider {
    override fun get(request: HttpServletRequest): String? {
        val cookie = getCookie(request)
        return cookie?.value ?: request.getAttribute(cookieName)?.toString()
    }

    override fun set(duid: String, request: HttpServletRequest, response: HttpServletResponse) {
        request.setAttribute(cookieName, duid)

        var cookie = getCookie(request)
        if (cookie == null) {
            cookie = Cookie(cookieName, duid)
            response.addCookie(cookie)
        } else {
            cookie.value = duid
        }
    }

    private fun getCookie(request: HttpServletRequest) = request.cookies?.find { it.name == cookieName }
}
