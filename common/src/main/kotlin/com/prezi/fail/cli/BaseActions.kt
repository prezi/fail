package com.prezi.fail.cli

import org.slf4j.LoggerFactory

abstract public class BaseActions <T: Action> {
    private val logger = LoggerFactory.getLogger(javaClass)!!

    protected fun ensuringArgCount(n: Int, args: Array<String>, createT: () -> T, msg: String = "Not enough arguments."): T? =
        if (args.size < n) {
            logger.error(msg)
            null
        } else {
            createT()
        }

    abstract protected fun doParse(verb: String, args: Array<String>, tail: Array<String>): T?

    public fun parsePositionalArgs(args: Array<String>): T? {
        if (args.size == 0) {
            return null
        }
        val verb = args[0]
        val tail = args.drop(1).copyToArray()
        return doParse(verb, args, tail)
    }
}