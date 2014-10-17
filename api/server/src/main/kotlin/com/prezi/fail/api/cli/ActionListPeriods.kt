package com.prezi.fail.api.cli

import com.prezi.fail.cli.Action
import org.slf4j.LoggerFactory
import com.prezi.fail.api.period.PeriodFactory


public class ActionListPeriods: Action() {
    val logger = LoggerFactory.getLogger(javaClass)!!

    class object {
        val verb = "list-periods"
        val cmdLineSyntax = verb
    }

    override fun run() {
        val nameColWidth: Int = PeriodFactory.periods.keySet().map{it.length}.max()!! + 4
        PeriodFactory.periods.forEach {
            logger.info(
                    it.key.toString() +
                            " ".repeat(nameColWidth - it.key.toString().length) +
                            it.value.description
            )
        }
    }
}
