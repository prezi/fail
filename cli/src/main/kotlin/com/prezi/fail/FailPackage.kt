package com.prezi.fail

import java.util.Properties
import java.io.File
import java.io.FileInputStream

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ch.qos.logback.classic.Level

import org.apache.commons.cli.Option
import org.apache.commons.cli.CommandLine

import com.prezi.fail.sarge.SargeConfig
import com.prezi.fail.sarge.SargeConfigKey
import com.prezi.fail.cli.FailCliOptions
import com.prezi.fail.cli.Actions
import com.prezi.fail.cli.CliConfig
import com.prezi.fail.config.Config
import com.prezi.fail.cli.CliConfigKey


private fun loadUserProperties() {
    val logger = LoggerFactory.getLogger("main")!!
    val file = File("${System.getenv("HOME")}/.fail.properties")
    if (file.exists()) {
        val appliedProperties: MutableMap<String, String> = hashMapOf()
        val properties = Properties()
        val inputStream = FileInputStream(file)
        properties.load(inputStream)
        inputStream.close()
        properties.forEach { _entry ->
            [suppress("UNCHECKED_CAST")] val entry = _entry as Map.Entry<String, String>
            if (System.getProperty(entry.key) == null) {
                System.setProperty(entry.key, entry.value)
                appliedProperties.put(entry.key, entry.value)
            }
        }
        logger.debug("Loaded properties file ${file.canonicalPath}")
        appliedProperties.forEach { entry -> logger.debug("${file.canonicalPath}: ${entry.key} = ${entry.value}") }
    }
}

private fun verifySappersTgzExists() {
    val path = SargeConfig().getSappersTargzPath()
    if (path == null) {
        println("${SargeConfigKey.SAPPERS_TGZ_PATH.key} is null. This probably means I'm running in some strange environment.")
        println("Please specify the path to sappers.tgz explicitly.")
        System.exit(1)
    } else {
        if (!File(path).canRead()) {
            println("Failed to open ${path} for reading, bailing out.")
            System.exit(1)
        }
    }
}


private fun setLogLevel(level: Level) {
    (LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as ch.qos.logback.classic.Logger).setLevel(level)
    (LoggerFactory.getLogger("com.linkedin") as ch.qos.logback.classic.Logger).setLevel(level)
}
private fun updateRootLoggerLevel(config: CliConfig) {
    if (config.isDebug()) { setLogLevel(Level.DEBUG) }
    if (config.isTrace()) { setLogLevel(Level.TRACE) }
}


private fun <T> applyOptionsToSystemProperties(commandLine: CommandLine, config: Config<T>, key: T, opt: Option) {
    if (commandLine.hasOption(opt)) {
        val commandLineValue = commandLine.getOptionValue(opt) ?: config.getToggledValue(key)
        System.setProperty(key.toString(), commandLineValue)
    }
}


fun main(args: Array<String>) {
    val options = FailCliOptions()
    val actions = Actions()

    val commandLine = options.parse(args)
    if (commandLine == null) {
        options.printHelp(actions.cmdLineSyntax)
        System.exit(1)

    } else {
        val cliConfig = CliConfig()
        val sargeConfig = SargeConfig()

        if (commandLine.hasOption(options.help)) {
            options.printHelp(actions.cmdLineSyntax)
            System.exit(0)
        }

        SargeConfigKey.values().forEach { applyOptionsToSystemProperties(commandLine, sargeConfig, it, it.opt) }
        CliConfigKey.values().forEach { applyOptionsToSystemProperties(commandLine, cliConfig, it, it.opt) }
        updateRootLoggerLevel(cliConfig)
        loadUserProperties()
        updateRootLoggerLevel(cliConfig)

        val action = Actions().parsePositionalArgs(commandLine.getArgs()!!)
        if (action == null) {
            options.printHelp(actions.cmdLineSyntax)
            System.exit(1)
        } else {
            verifySappersTgzExists()
            action.run()
        }
    }
}

