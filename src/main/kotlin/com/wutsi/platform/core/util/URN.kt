package com.wutsi.platform.core.util

import java.text.ParseException

data class URN(val value: String) {
    companion object {
        fun of(type: String, name: String): URN =
            URN("urn:$type:wutsi:$name".toLowerCase())

        fun of(type: String, domain: String, name: String): URN =
            URN("urn:$type:wutsi:$domain:$name".toLowerCase())

        fun parse(text: String): URN {
            val tokens = text.trim().toLowerCase().split(":")
            if (tokens[0] == "urn" && (tokens.size == 4 || tokens.size == 5))
                return URN(text.toLowerCase())
            else
                throw ParseException("Invalid format", 0)
        }
    }

    override fun toString(): String {
        return value
    }
}
