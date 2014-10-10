package com.prezi.fail.api.cli

import com.prezi.fail.cli.Action


abstract class LoggingApiAction: Action {
    val logger = CliActionLogger(javaClass)
}
