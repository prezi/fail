package com.prezi.fail.api.cli

import com.prezi.fail.cli.BaseActions
import com.prezi.fail.cli.Action
import com.linkedin.data.template.StringMap


public class ApiCliActions(val systemProperties: StringMap): BaseActions<Action>() {
    class object {
        public val cmdLineSyntax: String =
                """fail [options] ${ActionListJobs.cmdLineSyntax}
                        [options] ${ActionList.cmdLineSyntax}
                        [options] ${ActionPanic.cmdLineSyntax}
                        [options] ${ActionScheduleFailure.cmdLineSyntax}
                        [options] ${ActionListPeriods.cmdLineSyntax}
                        """
    }

    override fun doParse(verb: String, args: Array<String>, tail: Array<String>): Action? = when (verb) {
        ActionListJobs.verb -> ActionListJobs()
        ActionList.verb -> ensuringArgCount(1, tail, { ActionList(tail[0], systemProperties) })
        ActionScheduleFailure.verb -> ensuringArgCount(ActionScheduleFailure.requiredArgCount, tail, { ActionScheduleFailure(tail, systemProperties) })
        ActionListPeriods.verb -> ActionListPeriods()
        else -> null
    }
}

