package com.prezi.fail.cli

import org.apache.commons.cli.Options
import org.apache.commons.cli.Option
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.GnuParser
import com.prezi.fail.sarge.SargeConfigKey
import org.apache.commons.cli.HelpFormatter

public class FailCliOptions : Options() {
    public val help: Option = Option("h", "help", false, "Display this help message");

    {
        addOption(help)
        SargeConfigKey.values().forEach { addOption(it.opt) }
        CliConfigKey.values().forEach { addOption(it.opt) }
    }

    public fun parse(args: Array<String>): CommandLine {
        val parser = GnuParser()
        return parser.parse(this, args)!!
    }

    public fun printHelp(cmdLineSyntax: String) {
        val formatter = HelpFormatter()
        formatter.printHelp(cmdLineSyntax, this)
    }
}