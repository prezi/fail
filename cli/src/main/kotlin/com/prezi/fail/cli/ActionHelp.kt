package com.prezi.fail.cli

import com.linkedin.r2.RemoteInvocationException
import com.prezi.fail.config.FailConfig


public class ActionHelp: Action() {
    override fun run() {
        println("Offline")
        println("-------")
        CliOptions().printHelp(CliActions().cmdLineSyntax)
        println()
        println("Online")
        println("------")
        try {
            ActionApiCli(array("--help")).run()
        } catch (e: RemoteInvocationException) {
            val config = FailConfig()
            if (config.isDebug() || config.isTrace()) {
                println("Failed to contact API at ${config.getApiEndpoint()}.")
                throw e
            } else {
                println("Failed to contact API at ${config.getApiEndpoint()}, online help not available. Run with -v to get details.")
            }
        }
    }
}
