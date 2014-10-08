package com.prezi.fail.cli

public class Actions {
    val apiTest = "api-test"
    val schedule = "schedule"

    public val cmdLineSyntax: String =
            """fail [options] tag sapper duration-seconds [sapper-arg ...]
                    [options] schedule tag sapper duration-seconds [sapper-arg ...]
                    [options] ${apiTest}"""

    public fun parsePositionalArgs(args: Array<String>): Action? {
        if (args.size == 0) { return null }
        val name = args[0]

        if (name == apiTest) {
            return ActionApiTest()
        }

        if (name == schedule) {
            return ActionScheduleFailure()
        }

        if (args.count() < 3) {
            println("Not enough arguments for starting a sapper, or unknown action.")
            return null
        }

        return ActionCharge(args[0], args[1], args[2], args.drop(3))
    }
}