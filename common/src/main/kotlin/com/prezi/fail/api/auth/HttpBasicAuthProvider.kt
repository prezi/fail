package com.prezi.fail.api.auth

import com.linkedin.restli.client.Request
import org.apache.commons.codec.binary.Base64
import org.slf4j.LoggerFactory
import com.linkedin.restli.client.AbstractRequestBuilder

public class HttpBasicAuthProvider(val config: HttpBasicAuthProviderConfig = HttpBasicAuthProviderConfig()): AuthProvider {
    val logger = LoggerFactory.getLogger(javaClass)

    override fun authenticate(requestBuilder: AbstractRequestBuilder<*, *, *>) {
        logger.debug("${javaClass} adding ${headerName()} header to request")
        requestBuilder.addHeader(headerName(), headerValue())
    }

    fun headerName(): String = "Authorization"

    fun headerValue(): String = "Basic " + Base64.encodeBase64("${config.getUsername()}:${config.getPassword()}".toByteArray()).toString("ASCII")
}
