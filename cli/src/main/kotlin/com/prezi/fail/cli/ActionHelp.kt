package com.prezi.fail.cli


public class ActionHelp: Action {
    override fun run() {
        println("Offline")
        println("-------")
        CliOptions().printHelp(CliActions().cmdLineSyntax)
        println()
        println("Online")
        println("------")
        ActionApiCli(array("--help")).run()
    }
}
