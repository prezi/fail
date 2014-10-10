package com.prezi.fail.cli

import com.linkedin.r2.transport.http.client.HttpClientFactory
import com.linkedin.r2.transport.common.bridge.client.TransportClientAdapter
import com.linkedin.restli.client.RestClient
import com.prezi.fail.extensions.*
import com.linkedin.common.util.None
import com.linkedin.common.callback.FutureCallback
import javax.net.ssl.SSLContext
import org.slf4j.LoggerFactory
import com.linkedin.restli.client.RestLiResponseException


public abstract class ActionApiBase(val config: CliConfig = CliConfig()) : Action {
    open val logger = LoggerFactory.getLogger(javaClass)!!
    val urlPrefix = config.getApiEndpoint().endingWith('/')

    protected fun withClient(f: (RestClient) -> Unit) {
        val http = HttpClientFactory()
        val r2Client = TransportClientAdapter(http.getClient(mapOf(
                HttpClientFactory.HTTP_SSL_CONTEXT to SSLContext.getDefault()
        )))
        val restClient = RestClient(r2Client, urlPrefix)

        try {
            f(restClient)
        } catch (e: Exception) {
            if (config.isDebug() || config.isTrace()) {
                logger.error("API call failed.", e)
            } else {
                logger.error("API call failed: ${e.getMessage()}. Run with -v to get more details.")
            }
        } finally {
            restClient.shutdown(FutureCallback<None>())
            http.shutdown(FutureCallback<None>())
        }
    }
}
