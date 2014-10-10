package com.prezi.fail.api.cli

import org.apache.commons.cli.Options
import org.apache.commons.cli.Option
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.GnuParser
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.ParseException
import java.io.PrintWriter
import java.io.ByteArrayOutputStream

public class FailApiCliOptions : Options() {
    public val help: Option = Option("h", "help", false, "Display this help message");

    {
        addOption(help)
        ApiCliConfigKey.values().forEach { addOption(it.opt) }
    }

    public fun parse(args: Array<String>): CommandLine? =
        try {
            GnuParser().parse(this, args)!!
        } catch (e: ParseException) {
            println(e.getMessage())
            null
        }

    public fun printHelp(cmdLineSyntax: String): String {
        val ostream = ByteArrayOutputStream()
        val pw = PrintWriter(ostream)
        val formatter = HelpFormatter()
        formatter.printHelp(pw, 120, cmdLineSyntax, "", this, 0, 0, "", false)
        pw.flush()
        return ostream.toString().trim()
    }
}