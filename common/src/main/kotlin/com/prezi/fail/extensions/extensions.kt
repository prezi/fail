package com.prezi.fail.extensions

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Option


public fun CommandLine.hasOption(option: Option): Boolean =
        (if (option.getOpt() == null) { null } else { hasOption(option.getOpt()) }) ?:
                (if (option.getLongOpt() == null) { false } else { hasOption(option.getLongOpt()) })

public fun CommandLine.getOptionValue(option: Option): String? =
        (if (option.getOpt() == null) { null } else { getOptionValue(option.getOpt()) }) ?:
                (if (option.getLongOpt() == null) { null } else { getOptionValue(option.getLongOpt()) })

public fun String.endingWith(ch: Char): String =
        if (endsWith(ch)) {
            this
        } else {
            this + '/'
        }

