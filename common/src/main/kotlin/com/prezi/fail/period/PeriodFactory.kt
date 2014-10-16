package com.prezi.fail.period

import org.joda.time.Period
import java.util.Random
import org.joda.time.Duration


/**
 * Room for extension: special fallback period type with possible values stored in the database, the values
 * being managed via the CLI.
 */
object PeriodFactory {
    fun randomPeriodOffset(p: Period) = Duration(Random().nextInt(p.getMillis()))

    class InvalidPeriodDefinition: RuntimeException()

    fun build(input: String): FailPeriod = when(input) {
        "every-hour" -> EveryHour{randomPeriodOffset(it)}
        else         -> throw InvalidPeriodDefinition()
    }
}
