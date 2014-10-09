package com.prezi.fail.cli

import com.linkedin.restli.client.RestClient
import com.prezi.fail.api.ScheduledFailureBuilders
import com.prezi.fail.api.ScheduledFailure
import com.linkedin.data.template.StringArray
import com.linkedin.data.template.StringMap
import com.prezi.fail.sarge.SargeConfigKey
import org.slf4j.LoggerFactory

public class ActionScheduleFailure(val args: Array<String>) : ActionApiBase() {
    val logger = LoggerFactory.getLogger(javaClass)!!

    class object {
        val requiredArgCount = 4
        val verb = "schedule"
        val cmdLineSyntax = "${verb} interval ${ActionCharge.cmdLineSyntax}"
    }

    protected fun configurationSystemProperties(): StringMap {
        val conf = StringMap()
        SargeConfigKey.values().forEach { confKey ->
            val key = confKey.key
            val value = System.getProperty(key)
            if (value != null) {
                conf.set(key, value)
            }
        }
        return conf
    }

    override fun doApiCallAndProcessResponse(client: RestClient) {
        println("Note: this is just a placeholder for now, no failures are actually being scheduled.")
        val interval = args[0]
        val searchTerm = args[1]
        val sapper = args[2]
        val duration = args[3].toInt()

        val scheduledFailure = ScheduledFailure()
                        .setInterval(interval)!!
                        .setSearchTerm(searchTerm)!!
                        .setSapper(sapper)!!
                        .setDuration(duration)!!
                        .setSapperArgs(StringArray(args.drop(ActionScheduleFailure.requiredArgCount)))!!
                        .setScheduledBy(System.getenv("USER"))!!
                        .setScheduledAt(System.currentTimeMillis() / 1000)!!
                        .setConfiguration(configurationSystemProperties())
        logger.info("Scheduling failure: ${scheduledFailure.toString()}")

        val request = ScheduledFailureBuilders().create()?.input(scheduledFailure)
        val response = client.sendRequest(request)?.getResponse()!!
        println("Response code: ${response.getStatus()}")
        println("Location: ${response.getLocation()}")
    }
}