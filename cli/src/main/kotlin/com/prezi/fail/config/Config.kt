package com.prezi.fail.config

abstract class Config<KeyType> {
    fun isTrue(x: String?) = array("true", "TRUE").contains(x)
    fun isTrue(x: Boolean) = x

    fun getString(key: KeyType) = System.getProperty(key.toString())
    fun getBool(key: KeyType) = isTrue(System.getProperty(key.toString()))
    fun getBool(key: KeyType, default: Boolean) = isTrue(System.getProperty(key.toString(), default.toString()))

    public abstract fun getToggledValue(key: KeyType): String
}