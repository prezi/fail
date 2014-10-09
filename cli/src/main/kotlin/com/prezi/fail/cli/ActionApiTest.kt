package com.prezi.fail.cli

import com.linkedin.restli.client.RestClient
import com.prezi.fail.api.HealthcheckBuilders


public class ActionApiTest(config: CliConfig = CliConfig()) : ActionApiBase(config) {
    class object {
        val verb = "api-test"
        val cmdLineSyntax = verb
    }

    override fun doApiCallAndProcessResponse(client: RestClient) {
        println("Checking if API is running at ${urlPrefix}")
        println(
                "Healthcheck.isRunning(): " +
                        client.sendRequest(HealthcheckBuilders().get()?.build())?.getResponse()?.getEntity()?.isRunning()
        )
    }
}
