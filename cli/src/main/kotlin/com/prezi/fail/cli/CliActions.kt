package com.prezi.fail.cli

public class CliActions: BaseActions<Action>() {
    public val cmdLineSyntax: String =
            """fail [options] ${ActionRun.cmdLineSyntax}
                    [options] ${ActionApiTest.cmdLineSyntax}
            """

    override fun doParse(verb: String, args: Array<String>, tail: Array<String>): Action? = when (verb) {
        ActionRun.verb  -> ensuringArgCount(ActionRun.requiredArgCount, tail, { com.prezi.fail.cli.ActionRun(tail) })
        ActionApiTest.verb -> com.prezi.fail.cli.ActionApiTest()
        else               -> ActionApiCli(args)
    }
}
