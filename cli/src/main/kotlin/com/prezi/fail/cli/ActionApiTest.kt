package com.prezi.fail.cli

import com.linkedin.r2.transport.http.client.HttpClientFactory
import com.linkedin.r2.transport.common.bridge.client.TransportClientAdapter
import com.linkedin.restli.client.RestClient
import com.prezi.fail.api.HealthcheckBuilders
import com.prezi.fail.endingWith
import com.linkedin.common.util.None
import com.linkedin.common.callback.FutureCallback


public class ActionApiTest(val config: CliConfig = CliConfig()) : Action {
    override fun run() {
        val http = HttpClientFactory()
        val r2Client = TransportClientAdapter(http.getClient(mapOf()))
        val urlPrefix = config.getApiEndpoint().endingWith('/')
        val restClient = RestClient(r2Client, urlPrefix)
        println("Checking if API is running at ${urlPrefix}")
        try {
            println(
                    "Healthcheck.isRunning(): " +
                            restClient.sendRequest(HealthcheckBuilders().get()?.build())?.getResponse()?.getEntity()?.isRunning()
            )
        } catch (e: Exception) {
            println("Healthcheck request failed, the API is probably not running / healthy")
            println("The exception was: ${e.getMessage()}")
        } finally {
            restClient.shutdown(FutureCallback<None>())
            http.shutdown(FutureCallback<None>())
        }
    }
}
