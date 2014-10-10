package com.prezi.fail

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Option
import com.prezi.fail.extensions.*

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