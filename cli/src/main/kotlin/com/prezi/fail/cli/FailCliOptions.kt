package com.prezi.fail.cli

import org.apache.commons.cli.Options
import org.apache.commons.cli.Option
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.GnuParser
import com.prezi.fail.sarge.SargeConfigKey
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.ParseException

public class FailCliOptions : Options() {
    public val help: Option = Option("h", "help", false, "Display this help message");

    {
        addOption(help)
        SargeConfigKey.values().forEach { addOption(it.opt) }
        CliConfigKey.values().forEach { addOption(it.opt) }
    }

    public fun parse(args: Array<String>): CommandLine? =
        try {
            GnuParser().parse(this, args)!!
        } catch (e: ParseException) {
            println(e.getMessage())
            null
        }

    public fun printHelp(cmdLineSyntax: String) {
        val formatter = HelpFormatter()
        formatter.setWidth(120)
        formatter.printHelp(cmdLineSyntax, this)
    }
}