package com.prezi.fail

import com.prezi.fail.cli.CliOptions
import com.prezi.fail.cli.CliActions
import com.prezi.fail.extensions.*
import com.prezi.fail.cli.Action
import com.prezi.fail.cli.ActionApiCli
import com.prezi.fail.cli.ActionHelp
import com.prezi.fail.sarge.SargeConfigKey
import com.prezi.fail.sarge.SargeConfig
import com.prezi.fail.config.loadUserProperties
import com.prezi.fail.config.FailConfig
import com.prezi.fail.config.FailConfigKey
import com.prezi.fail.config.updateLoggerLevels


fun main(args: Array<String>) {
    val options = CliOptions()
    val actions = CliActions()
    var action: Action
    val commandLine = options.parse(args)
    val cliConfig = FailConfig()
    val sargeConfig = SargeConfig()

    if (commandLine != null) {
        SargeConfigKey.values().forEach { sargeConfig.applyOptionsToSystemProperties(commandLine, it, it.opt) }
        FailConfigKey.values().forEach { cliConfig.applyOptionsToSystemProperties(commandLine, it, it.opt) }
    }

    if (args.size == 0) {
        action = ActionHelp()
    } else if (commandLine == null) {
        action = ActionApiCli(args)
    } else if (commandLine.hasOption(options.help)) {
        action = ActionHelp()
    } else {
        action = actions.parsePositionalArgs(commandLine.getArgs()!!) ?: ActionApiCli(args)
    }

    updateLoggerLevels(cliConfig, "com.linkedin")
    loadUserProperties("${System.getenv("HOME")}/.fail.properties")
    updateLoggerLevels(cliConfig, "com.linkedin")

    action.run()
    System.exit(action.exitCode)
}

