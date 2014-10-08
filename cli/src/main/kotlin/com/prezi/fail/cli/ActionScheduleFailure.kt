package com.prezi.fail.cli

import com.linkedin.restli.client.RestClient
import com.prezi.fail.api.ScheduledFailureBuilders
import com.prezi.fail.api.ScheduledFailure

public class ActionScheduleFailure : ActionApiBase() {
    override fun doApiCallAndProcessResponse(client: RestClient) {
        println("Note: this is just a placeholder for now, no failures are actually being scheduled.")
        val request = ScheduledFailureBuilders().create()?.input(
                ScheduledFailure().setScheduledBy("abesto")
        )
        val response = client.sendRequest(request)?.getResponse()!!
        println("Response code: ${response.getStatus()}")
        println("Location: ${response.getLocation()}")
    }
}