package com.prezi.fail.cli


import com.linkedin.restli.client.RestClient
import com.prezi.fail.api.HealthcheckBuilders
import com.prezi.fail.api.ScheduledFailureBuilders


public class ActionPanic : ActionApiBase() {
    class object {
        val verb = "panic"
        val cmdLineSyntax = verb
    }

    override fun doApiCallAndProcessResponse(client: RestClient) {
        println("Panicking. Telling the API to cancel all running charges and pause any further schedules.")
        println(
                client.sendRequest(ScheduledFailureBuilders().actionPanic())!!.getResponseEntity()
        )
    }
}
