package com.prezi.fail.cli

abstract public class Action {
    abstract public fun run()
    public var exitCode: Int = 0
}