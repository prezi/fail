package com.prezi.fail.cli

import com.linkedin.r2.transport.http.client.HttpClientFactory
import com.linkedin.r2.transport.common.bridge.client.TransportClientAdapter
import com.linkedin.restli.client.RestClient
import com.prezi.fail.api.HealthcheckBuilders
import com.prezi.fail.endingWith
import com.linkedin.common.util.None
import com.linkedin.common.callback.FutureCallback


public abstract class ActionApiBase(val config: CliConfig = CliConfig()) : Action {
    val urlPrefix = config.getApiEndpoint().endingWith('/')

    protected abstract fun doApiCallAndProcessResponse(client: RestClient)

    override fun run() {
        val http = HttpClientFactory()
        val r2Client = TransportClientAdapter(http.getClient(mapOf()))
        val restClient = RestClient(r2Client, urlPrefix)

        try {
            doApiCallAndProcessResponse(restClient)
        } catch (e: Exception) {
            println("API call failed. The exception was: ${e.getMessage()}")
        } finally {
            restClient.shutdown(FutureCallback<None>())
            http.shutdown(FutureCallback<None>())
        }
    }
}
