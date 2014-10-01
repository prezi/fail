package com.prezi.fail

import java.util.Properties
import java.io.File
import java.io.FileInputStream

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ch.qos.logback.classic.Level

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import org.apache.commons.cli.GnuParser
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.HelpFormatter

import com.prezi.fail.sarge.Sarge
import com.prezi.changelog.ChangelogClient
import com.prezi.fail.sarge.SargeConfig
import com.prezi.fail.sarge.SargeConfigKey


private fun usage(exitCode: Int = 0) {
    val formatter = HelpFormatter()
    formatter.printHelp("fail tag sapper duration-seconds [sapper-arg [sapper-arg ...]]", Cli.options)
    System.exit(exitCode)
}

object Cli {
    public val help:   Option = Option("h", "help", false, "Display this help message")
    public val debug:  Option = Option("v", "debug", false, "Set root logger to DEBUG level")
    public val trace:  Option = Option("vv", "trace", false, "Set root logger to TRACE level")
    public val az:     Option = Option("z", "availability-zone", true, "Target only nodes from this availability zone")
    public val dryRun: Option = Option("n", "dryrun", false, "Skip running sappers")

    public val options: Options = Options();

    {
        options.addOption(help)
        options.addOption(debug)
        options.addOption(trace)
        options.addOption(az)
        options.addOption(dryRun)
    }

    public fun parseCliArguments(args: Array<String>): CommandLine {
        val parser = GnuParser()
        return parser.parse(options, args)!!
    }
}

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
        logger.info("Loaded properties file ${file.canonicalPath}")
        appliedProperties.forEach { entry -> logger.debug("${file.canonicalPath}: ${entry.key} = ${entry.value}") }
    }
}

fun main(args: Array<String>) {
    val commandLine = Cli.parseCliArguments(args)
    if (commandLine.hasOption(Cli.help.getOpt())) {
        usage()
    }
    if (commandLine.hasOption(Cli.debug.getOpt())) {
        (LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as ch.qos.logback.classic.Logger).setLevel(Level.DEBUG)
    }
    if (commandLine.hasOption(Cli.trace.getOpt())) {
        (LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as ch.qos.logback.classic.Logger).setLevel(Level.TRACE)
    }
    if (commandLine.hasOption(Cli.az.getOpt())) {
        System.setProperty(SargeConfigKey.AVAILABILITY_ZONE.key, commandLine.getOptionValue(Cli.az.getOpt())!!)
    }
    if (commandLine.hasOption(Cli.dryRun.getOpt())) {
        System.setProperty(SargeConfigKey.DRY_RUN.key, "true")
    }

    val positionalArgs = commandLine.getArgs()!!
    if (positionalArgs.count() < 3) {
        println("Not enough arguments.")
        usage(1)
    }

    loadUserProperties()
    // TODO: add logic for choosing action based on args[0] here
    Sarge().charge(positionalArgs[0], positionalArgs[1], positionalArgs[2], positionalArgs.drop(3))
}

