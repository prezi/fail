package com.prezi.fail.api.cli

import com.prezi.fail.cli.BaseActions
import com.prezi.fail.cli.Action
import com.linkedin.data.template.StringMap


public class ApiCliActions(val systemProperties: StringMap): BaseActions<Action>() {
    class object {
        public val cmdLineSyntax: String =
                """fail [options] ${ActionListRuns.cmdLineSyntax}
                        [options] ${ActionLog.cmdLineSyntax}
                        [options] ${ActionList.cmdLineSyntax}
                        [options] ${ActionPanic.cmdLineSyntax}
                        [options] ${ActionScheduleFailure.cmdLineSyntax}
                        [options] ${ActionUnschedule.cmdLineSyntax}
                        [options] ${ActionListPeriods.cmdLineSyntax}
                        [options] ${ActionPanic.cmdLineSyntax}
                        [options] ${ActionPanicOver.cmdLineSyntax}
                        """
    }

    override fun doParse(verb: String, args: Array<String>, tail: Array<String>): Action? = when (verb) {
        ActionListRuns.verb -> ActionListRuns(systemProperties)
        ActionLog.verb -> ensuringArgCount(ActionLog.requiredArgCount, tail, { ActionLog(tail.first()) })
        ActionList.verb -> ensuringArgCount(ActionList.requiredArgCount, tail, { ActionList(tail.firstOrNull() ?: ".*", systemProperties) })
        ActionScheduleFailure.verb -> ensuringArgCount(ActionScheduleFailure.requiredArgCount, tail, { ActionScheduleFailure(tail, systemProperties) })
        ActionUnschedule.verb -> ensuringArgCount(ActionUnschedule.requiredArgCount, tail, { ActionUnschedule(tail, systemProperties) })
        ActionListPeriods.verb -> ActionListPeriods()
        ActionPanic.verb -> ActionPanic()
        ActionPanicOver.verb -> ActionPanicOver()
        else -> null
    }
}

