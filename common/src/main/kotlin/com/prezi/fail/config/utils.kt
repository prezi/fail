package com.prezi.fail.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ch.qos.logback.classic.Level
import java.io.File
import java.util.Properties
import java.io.FileInputStream

public fun userPropertiesFile(default: String? = null): String? = System.getProperty("fail.propertiesFile", default)

public fun loadUserProperties(defaultPropertiesFilePath: String? = null) {
    val logger = LoggerFactory.getLogger("main")!!

    val propertiesFilePath = userPropertiesFile(defaultPropertiesFilePath)
    if (propertiesFilePath == null) {
        logger.debug("fail.propertiesFile system property not set, and loadUserProperties didn't get a default file path. Not loading any properties files.")
        return
    }

    val file = File(propertiesFilePath)
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
    } else if (defaultPropertiesFilePath != propertiesFilePath) {
        logger.warn("Tried to load properties file ${propertiesFilePath} (specified in fail.propertiesFile), but it doesn't exist")
    }
}

private fun getLogger(it: String) = (LoggerFactory.getLogger(it) as ch.qos.logback.classic.Logger)

public fun getLoggerLevel(name: String): Level = getLogger(name).getLevel()
public fun getRootLogLevel(): Level = getLoggerLevel(Logger.ROOT_LOGGER_NAME)

public fun setLogLevel(level: Level, loggers: List<String>) {
    loggers.map(::getLogger).forEach{it.setLevel(level)}
}
public fun setRootLogLevel(l: Level) {
    setLogLevel(l, listOf(Logger.ROOT_LOGGER_NAME))
}

public fun updateLoggerLevels(config: FailConfig, vararg additionalLoggers: String = array()) {
    var finalLoggers = listOf(Logger.ROOT_LOGGER_NAME) + additionalLoggers
    if (config.isDebug()) { setLogLevel(Level.DEBUG, finalLoggers) }
    else if (config.isTrace()) { setLogLevel(Level.TRACE, finalLoggers) }
    else { setLogLevel(Level.INFO, finalLoggers) }
}


