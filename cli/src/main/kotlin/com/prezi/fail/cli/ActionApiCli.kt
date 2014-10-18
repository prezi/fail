package com.prezi.fail.cli

import com.prezi.fail.api.CliBuilders
import com.linkedin.data.template.StringArray
import com.linkedin.data.template.StringMap
import com.prezi.fail.api.ActionApiBase

public class ActionApiCli(val args: Array<String>) : ActionApiBase() {
    override public fun run() {
        withClient({ client ->
            val systemProperties = StringMap()
            System.getProperties()
                    ?.filter{(it.key as String).startsWith("fail.")}
                    ?.forEach{ systemProperties.set(it.key as String, it.value as String) }

            val request = CliBuilders().actionRunCli()!!
                    .paramArgs(StringArray(args.toList()))!!
                    .paramSystemProperties(systemProperties)!!
                    .build()!!
            logger.debug("Request: ${request.getInputRecord()}")

            val response = client.sendRequest(request)?.getResponseEntity()!!
            if (response.isValidCommandLine()!!) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Response: \n${response.getOutput()}")
                } else {
                    println(response.getOutput())
                }
            } else {
                println(response.getOutput())
                println()
                ActionHelp().run()
            }
            exitCode = response.getExitCode()!!
        })
    }
}
