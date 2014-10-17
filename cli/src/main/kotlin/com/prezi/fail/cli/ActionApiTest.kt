package com.prezi.fail.cli

import com.prezi.fail.api.HealthcheckBuilders


public class ActionApiTest : ActionApiBase() {
    class object {
        val verb = "api-test"
        val cmdLineSyntax = verb
    }

    override public fun run() {
        withClient({ client ->
            logger.info("Checking if API is running at ${urlPrefix}")
            logger.info(
                    "Healthcheck.isRunning(): " +
                            client.sendRequest(HealthcheckBuilders().get()?.build())?.getResponse()?.getEntity()?.isRunning()
            )
        })
    }
}
