package com.wutsi.platform.core.security.spring.wutsi

import com.wutsi.platform.core.security.KeyProvider
import com.wutsi.platform.security.WutsiSecurityApi
import org.slf4j.LoggerFactory
import java.security.Key
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import java.util.Collections

class WutsiKeyProvider(
    private val securityApi: WutsiSecurityApi,
    private val cache: MutableMap<String, Key> = Collections.synchronizedMap(mutableMapOf<String, Key>())
) : KeyProvider {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(WutsiKeyProvider::class.java)
    }

    override fun getKey(keyId: String): Key {
        var key = cache[keyId]
        if (key != null)
            return key

        key = loadKeyFromServer(keyId)
        cache[keyId] = key
        return key
    }

    private fun loadKeyFromServer(keyId: String): PublicKey {
        LOGGER.info("Loading Key#$keyId from server...")
        val key = securityApi.getKey(keyId.toLong()).key
        if (key.algorithm == "RSA") {
            val byteKey: ByteArray = Base64.getDecoder().decode(key.content.toByteArray())
            val x509publicKey = X509EncodedKeySpec(byteKey)
            val kf = KeyFactory.getInstance(key.algorithm)
            return kf.generatePublic(x509publicKey)
        } else {
            throw IllegalStateException("Algorithm not supported: ${key.algorithm}")
        }
    }
}
