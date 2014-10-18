package com.prezi.fail.cli

import com.prezi.fail.api.HealthcheckBuilders
import com.prezi.fail.api.Api
import org.slf4j.LoggerFactory


public class ActionApiTest : Action() {
    val logger = LoggerFactory.getLogger(javaClass)!!
    class object {
        val verb = "api-test"
        val cmdLineSyntax = verb
    }

    protected val api: Api = Api()

    override public fun run() {
        api.withClient({ client ->
            logger.info("Checking if API is running at ${api.urlPrefix}")
            logger.info(
                    "Healthcheck.isRunning(): " +
                            client.sendRequest(HealthcheckBuilders().get()?.build())?.getResponse()?.getEntity()?.isRunning()
            )
        })
    }
}
