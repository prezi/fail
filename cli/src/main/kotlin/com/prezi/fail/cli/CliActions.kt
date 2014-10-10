package com.prezi.fail.cli

public class CliActions: BaseActions<Action>() {
    public val cmdLineSyntax: String =
            """fail [options] ${ActionCharge.cmdLineSyntax}
                    [options] ${ActionApiTest.cmdLineSyntax}
            """

    override fun doParse(verb: String, args: Array<String>, tail: Array<String>): Action? = when (verb) {
        ActionCharge.verb  -> ensuringArgCount(ActionCharge.requiredArgCount, tail, { com.prezi.fail.cli.ActionCharge(tail) })
        ActionApiTest.verb -> com.prezi.fail.cli.ActionApiTest()
        else               -> ActionApiCli(args)
    }
}
