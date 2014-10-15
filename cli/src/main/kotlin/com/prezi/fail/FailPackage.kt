package com.prezi.fail

import java.util.Properties
import java.io.File
import java.io.FileInputStream

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ch.qos.logback.classic.Level

import com.prezi.fail.cli.CliConfig
import com.prezi.fail.cli.CliConfigKey
import com.prezi.fail.cli.CliOptions
import com.prezi.fail.cli.CliActions
import com.prezi.fail.extensions.*
import com.prezi.fail.cli.Action
import com.prezi.fail.cli.ActionApiCli
import com.prezi.fail.cli.ActionHelp
import com.prezi.fail.sarge.SargeConfigKey
import com.prezi.fail.sarge.SargeConfig




private fun setLogLevel(level: Level) {
    (LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as ch.qos.logback.classic.Logger).setLevel(level)
    (LoggerFactory.getLogger("com.linkedin") as ch.qos.logback.classic.Logger).setLevel(level)
}
private fun updateRootLoggerLevel(config: CliConfig) {
    if (config.isDebug()) { setLogLevel(Level.DEBUG) }
    if (config.isTrace()) { setLogLevel(Level.TRACE) }
}

fun main(args: Array<String>) {
    val options = CliOptions()
    val actions = CliActions()
    var action: Action
    val commandLine = options.parse(args)
    val cliConfig = CliConfig()
    val sargeConfig = SargeConfig()

    if (commandLine != null) {
        SargeConfigKey.values().forEach { sargeConfig.applyOptionsToSystemProperties(commandLine, it, it.opt) }
        CliConfigKey.values().forEach { cliConfig.applyOptionsToSystemProperties(commandLine, it, it.opt) }
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

    updateRootLoggerLevel(cliConfig)
    loadUserProperties("${System.getenv("HOME")}/.fail.properties")
    updateRootLoggerLevel(cliConfig)

    action.run()
    System.exit(action.exitCode)
}

