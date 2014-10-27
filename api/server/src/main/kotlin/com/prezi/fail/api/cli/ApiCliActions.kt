package com.prezi.fail.api.cli

import com.prezi.fail.cli.BaseActions
import com.prezi.fail.cli.Action
import com.linkedin.data.template.StringMap


public class ApiCliActions(val systemProperties: StringMap): BaseActions<Action>() {
    class object {
        public val cmdLineSyntax: String =
                """fail [options] ${ActionListRuns.cmdLineSyntax}
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
        ActionList.verb -> ensuringArgCount(1, tail, { ActionList(tail[0], systemProperties) })
        ActionScheduleFailure.verb -> ensuringArgCount(ActionScheduleFailure.requiredArgCount, tail, { ActionScheduleFailure(tail, systemProperties) })
        ActionUnschedule.verb -> ensuringArgCount(1, tail, { ActionUnschedule(tail, systemProperties) })
        ActionListPeriods.verb -> ActionListPeriods()
        ActionPanic.verb -> ActionPanic()
        ActionPanicOver.verb -> ActionPanicOver()
        else -> null
    }
}

