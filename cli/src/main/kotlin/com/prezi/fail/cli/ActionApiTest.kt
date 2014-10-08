package com.prezi.fail.cli

import com.linkedin.r2.transport.http.client.HttpClientFactory
import com.linkedin.r2.transport.common.bridge.client.TransportClientAdapter
import com.linkedin.restli.client.RestClient
import com.prezi.fail.api.HealthcheckBuilders
import com.prezi.fail.endingWith
import com.linkedin.common.util.None
import com.linkedin.common.callback.FutureCallback


public class ActionApiTest(config: CliConfig = CliConfig()) : ActionApiBase(config) {
    override fun doApiCallAndProcessResponse(client: RestClient) {
        println("Checking if API is running at ${urlPrefix}")
        println(
                "Healthcheck.isRunning(): " +
                        client.sendRequest(HealthcheckBuilders().get()?.build())?.getResponse()?.getEntity()?.isRunning()
        )
    }
}
