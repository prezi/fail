package com.prezi.fail.api

import com.prezi.fail.extensions.endingWith
import com.prezi.fail.cli
import com.prezi.fail.cli.Action
import com.linkedin.restli.client
import com.linkedin.common.util
import com.linkedin.common.util
import com.prezi.fail
import org.slf4j.LoggerFactory
import com.linkedin.r2.transport.http
import com.linkedin.r2.transport.common.bridge
import com.linkedin.r2.transport
import com.linkedin.common.callback
import com.prezi.fail.config.FailConfig

public final class Api(val config: FailConfig = FailConfig()) {
    open val logger = LoggerFactory.getLogger(javaClass)!!
    val urlPrefix = config.getApiEndpoint().endingWith('/')

    public fun withClient<T : Any>(f: (client.RestClient) -> T): T? {
        val http = http.client.HttpClientFactory()
        val r2Client = bridge.client.TransportClientAdapter(http.getClient(mapOf(
                transport.http.client.HttpClientFactory.HTTP_SSL_CONTEXT to javax.net.ssl.SSLContext.getDefault()
        )))
        val restClient = client.RestClient(r2Client, urlPrefix)

        try {
            return f(restClient)
        } catch (e: Exception) {
            if (config.isDebug() || config.isTrace()) {
                logger.error("API call failed.", e)
            } else {
                logger.error("API call failed: ${e.getMessage()}. Run with -v to get more details.")
            }
        } finally {
            restClient.shutdown(callback.FutureCallback<util.None>())
            http.shutdown(callback.FutureCallback<util.None>())
        }
        return null
    }
}