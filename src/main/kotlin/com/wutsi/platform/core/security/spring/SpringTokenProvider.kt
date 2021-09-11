package com.wutsi.platform.core.security.spring

import com.wutsi.platform.core.security.TokenProvider
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import javax.servlet.http.HttpServletRequest

class SpringTokenProvider(
    private val context: ApplicationContext
) : TokenProvider {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(SpringTokenProvider::class.java)
    }

    override fun geToken(): String? {
        val request = getHttpServletRequest()
        if (request == null) {
            LOGGER.debug("No request available in Spring context")
            return null
        } else {
            val value = request.getHeader("Authorization") ?: return null
            return if (value.startsWith("Bearer ", ignoreCase = true))
                value.substring(7)
            else
                null
        }
    }

    private fun getHttpServletRequest(): HttpServletRequest? =
        try {
            context.getBean(HttpServletRequest::class.java)
        } catch (ex: Exception) {
            null
        }
}
