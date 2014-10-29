package com.prezi.fail.cli

import com.prezi.fail.api.CliBuilders
import com.linkedin.data.template.StringArray
import com.linkedin.data.template.StringMap
import com.prezi.fail.api.Api
import org.slf4j.LoggerFactory
import com.linkedin.restli.client.base.BuilderBase
import com.prezi.fail.api.CliResult
import com.linkedin.restli.client.ActionRequest
import com.linkedin.restli.client.AbstractRequestBuilder
import com.linkedin.restli.client.Request
import com.prezi.fail.config.FailConfigKey
import com.prezi.fail.api.auth.HttpBasicAuthProviderConfigKey

public class ActionApiCli(val args: Array<String>) : Action() {
    val api = Api()
    val logger = LoggerFactory.getLogger(javaClass)
    val neverPropagate = setOf(
            HttpBasicAuthProviderConfigKey.PASSWORD.key,
            HttpBasicAuthProviderConfigKey.USERNAME.key
    )

    override public fun run() {
        val systemProperties = StringMap()
        System.getProperties()
                ?.filter { (it.key as String).startsWith("fail.") }
                ?.filterNot { neverPropagate.contains(it.key) }
                ?.forEach { systemProperties.set(it.key as String, it.value as String) }
        systemProperties.set("user.name", System.getProperty("user.name"))

        val request = CliBuilders().actionRunCli()!!
                .paramArgs(StringArray(args.toList()))!!
                .paramSystemProperties(systemProperties)!!
        api.authenticate(request)

        val response = api.sendRequest(request.build())!!
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
    }
}
