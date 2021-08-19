package com.wutsi.platform.core.security.spring

import com.wutsi.platform.core.security.TokenProvider
import org.springframework.context.ApplicationContext
import javax.servlet.http.HttpServletRequest

class SpringTokenProvider(
    private val context: ApplicationContext,
    private val appTokenProvider: TokenProvider
) : TokenProvider {
    override fun geToken(): String? {
        val request = getHttpServletRequest()
        if (request != null) {
            val value = request.getHeader("Authorization") ?: return null
            return if (value.startsWith("Bearer ", ignoreCase = true))
                value.substring(7)
            else
                null
        } else {
            return appTokenProvider.geToken()
        }
    }

    private fun getHttpServletRequest(): HttpServletRequest? =
        try {
            context.getBean(HttpServletRequest::class.java)
        } catch (ex: Exception) {
            null
        }
}
