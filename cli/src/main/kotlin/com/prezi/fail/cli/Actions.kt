package com.prezi.fail.cli

public class Actions {
    public val cmdLineSyntax: String =
            """fail [options] ${ActionCharge.cmdLineSyntax}
                    [options] ${ActionApiTest.cmdLineSyntax}
                    [options] ${ActionScheduleFailure.cmdLineSyntax}
                    [options] ${ActionListJobs.cmdLineSyntax}
                    [options] ${ActionList.cmdLineSyntax}
                    [options] ${ActionPanic.cmdLineSyntax}
                    """

    protected fun ensuringArgCount(n: Int, args: Array<String>, createAction: () -> Action, msg: String = "Not enough arguments."): Action? =
        if (args.size < n) {
            println(msg)
            null
        } else {
            createAction()
        }

    public fun parsePositionalArgs(args: Array<String>): Action? {
        if (args.size == 0) {
            return null
        }
        val verb = args[0]
        val tail = args.drop(1).copyToArray()
        return when (verb) {
            ActionApiTest.verb         -> ActionApiTest()
            ActionScheduleFailure.verb -> ensuringArgCount(ActionScheduleFailure.requiredArgCount, tail, { ActionScheduleFailure(tail) })
            ActionList.verb            -> ensuringArgCount(1, tail, { ActionList(tail[0]) })
            ActionListJobs.verb        -> ActionListJobs()
            ActionPanic.verb           -> ActionPanic()
            else                       -> ensuringArgCount(
                    ActionCharge.requiredArgCount, args, { ActionCharge(args) },
                    "Not enough arguments or unknown action."
            )
        }
    }
}