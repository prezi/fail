package com.prezi.fail.config

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


