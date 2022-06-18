package com.wutsi.platform.core.url

class NullUrlShortener : UrlShortener {
    override fun shorten(url: String) = url
}
