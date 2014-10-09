package com.prezi.fail.cli

public class Actions {
    public val cmdLineSyntax: String =
            """fail [options] ${ActionCharge.cmdLineSyntax}
                    [options] ${ActionApiTest.cmdLineSyntax}
                    [options] ${ActionScheduleFailure.cmdLineSyntax}
                    [options] ${ActionListJobs.cmdLineSyntax}
                    [options] ${ActionList.cmdLineSyntax}
                    """

    protected fun ensuringArgCount(n: Int, args: Array<String>, createAction: () -> Action): Action? =
        if (args.size < n) {
            println("Not enough arguments.")
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
            ActionList.verb            -> ActionList()
            ActionListJobs.verb        -> ActionListJobs()
            else                       -> ensuringArgCount(ActionCharge.requiredArgCount, args, { ActionCharge(args) })
        }
    }
}