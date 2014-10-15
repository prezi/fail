package com.prezi.fail

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Option
import com.prezi.fail.extensions.*
import org.slf4j.LoggerFactory
import java.io.File
import java.util.Properties
import java.io.FileInputStream


public fun loadUserProperties(defaultPropertiesFilePath: String?) {
    val logger = LoggerFactory.getLogger("main")!!

    val propertiesFilePath = System.getProperty("fail.propertiesFile", null) ?: defaultPropertiesFilePath
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


abstract class Config<KeyType> {
    public fun applyOptionsToSystemProperties(
            commandLine: CommandLine, key: KeyType, opt: Option,
            properties: MutableMap<String, String> = System.getProperties() as MutableMap<String, String>)
    {
        if (commandLine.hasOption(opt)) {
            val commandLineValue = commandLine.getOptionValue(opt) ?: getToggledValue(key)
            properties.set(key.toString(), commandLineValue)
        }
    }

    public var configMap: MutableMap<String, String>? = null

    fun isTrue(x: String?) = array("true", "TRUE").contains(x)
    fun isTrue(x: Boolean) = x

    fun getString(key: KeyType) = configMap?.get(key.toString()) ?: System.getProperty(key.toString())
    fun getBool(key: KeyType) = isTrue(getString(key))
    fun getBool(key: KeyType, default: Boolean) = isTrue(getString(key) ?: default.toString())

    public abstract fun getToggledValue(key: KeyType): String
}