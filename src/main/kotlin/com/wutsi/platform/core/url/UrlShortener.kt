package com.wutsi.platform.core.url

interface UrlShortener {
    fun shorten(url: String): String
}
