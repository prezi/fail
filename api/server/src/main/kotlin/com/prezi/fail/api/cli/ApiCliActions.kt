package com.prezi.fail.api.cli

import com.prezi.fail.cli.BaseActions
import com.prezi.fail.cli.Action
import com.linkedin.data.template.StringMap
import com.prezi.fail.Config


public class ApiCliActions(val systemProperties: StringMap): BaseActions<LoggingApiAction>() {
    class object {
        public val cmdLineSyntax: String =
                """fail ${ActionScheduleFailure.cmdLineSyntax} [options]
                        ${ActionListJobs.cmdLineSyntax} [options]
                        ${ActionList.cmdLineSyntax} [options]
                        ${ActionPanic.cmdLineSyntax} [options]
                        """
    }

    override fun doParse(verb: String, args: Array<String>, tail: Array<String>): LoggingApiAction? = when (verb) {
        ActionScheduleFailure.verb -> ensuringArgCount(ActionScheduleFailure.requiredArgCount, tail, { ActionScheduleFailure(tail, systemProperties) })
        ActionListJobs.verb -> ActionListJobs()
        ActionList.verb -> ensuringArgCount(1, tail, { ActionList(tail[0], systemProperties) })
        else -> null
    }
}

