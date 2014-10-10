package com.prezi.fail

import org.apache.commons.cli.Option
import org.apache.commons.cli.CommandLine

import com.prezi.fail.extensions.*


abstract class Config<KeyType> {
    class object {
        public fun <T> applyOptionsToSystemProperties(commandLine: CommandLine, config: Config<T>, key: T, opt: Option,
                                                      properties: MutableMap<String, String> = System.getProperties() as MutableMap<String, String>) {
            if (commandLine.hasOption(opt)) {
                val commandLineValue = commandLine.getOptionValue(opt) ?: config.getToggledValue(key)
                properties.set(key.toString(), commandLineValue)
            }
        }
    }

    public var configMap: MutableMap<String, String> = System.getProperties() as MutableMap<String, String>

    fun isTrue(x: String?) = array("true", "TRUE").contains(x)
    fun isTrue(x: Boolean) = x

    fun getString(key: KeyType) = configMap.get(key.toString())
    fun getBool(key: KeyType) = isTrue(configMap.get(key.toString()))
    fun getBool(key: KeyType, default: Boolean) = isTrue(configMap.get(key.toString()) ?: default.toString())

    public abstract fun getToggledValue(key: KeyType): String
}