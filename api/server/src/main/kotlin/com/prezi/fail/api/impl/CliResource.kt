package com.prezi.fail.api.impl

import com.linkedin.restli.server.annotations.RestLiActions
import com.linkedin.restli.server.annotations.Action
import com.linkedin.data.template.StringArray
import com.linkedin.data.template.StringMap
import com.linkedin.restli.server.annotations.ActionParam
import com.prezi.fail.api.cli.ApiCliActions
import com.prezi.fail.api.cli.FailApiCliOptions
import com.prezi.fail.api.cli.ApiCliConfig
import com.prezi.fail.api.cli.ApiCliConfigKey
import com.prezi.fail.Config
import com.prezi.fail.extensions.*


[RestLiActions(namespace = "com.prezi.fail.api", name = "Cli")]
public class CliResource {
    [Action(name="RunCli")]
    public fun runCli([ActionParam("args")] _args: StringArray,
                      [ActionParam("systemProperties")] systemProperties: StringMap): String {
        val args = _args.copyToArray()
        val options = FailApiCliOptions()
        val cmdLine = options.parse(args)
        val cliConfig = ApiCliConfig()

        if (cmdLine == null) {
            return options.printHelp(ApiCliActions.cmdLineSyntax)
        }
        ApiCliConfigKey.values().forEach { Config.applyOptionsToSystemProperties(cmdLine, cliConfig, it, it.opt, systemProperties) }

        if (cmdLine.hasOption(options.help)) {
            return options.printHelp(ApiCliActions.cmdLineSyntax)
        }

        val action = ApiCliActions(systemProperties).parsePositionalArgs(args)
        if (action == null) {
            return options.printHelp(ApiCliActions.cmdLineSyntax)
        }

        action.run()
        return action.logger.buffer.join("\n")
    }
}
