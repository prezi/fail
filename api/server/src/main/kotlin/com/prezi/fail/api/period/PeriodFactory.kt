package com.prezi.fail.api.period

import org.joda.time.Period
import java.util.Random
import org.joda.time.Duration
import org.joda.time.DateTimeConstants

fun randomPeriodOffset(p: Period) = Duration(
        Random().nextInt(p.toStandardSeconds().getSeconds()).toLong() * DateTimeConstants.MILLIS_PER_SECOND
)

/**
 * Room for extension: special fallback period type with possible values stored in the database, the values
 * being managed via the CLI.
 */
object PeriodFactory {

    val periods = mapOf(
            "every-hour" to EveryHour(::randomPeriodOffset),
            "bp-weekday-worktime-daily" to BudapestWeekdayWorktimeDaily(::randomPeriodOffset),
            "sf-weekday-worktime-daily" to SanFranciscoWeekdayWorktimeDaily(::randomPeriodOffset)
    )

    class InvalidPeriodDefinition: RuntimeException()

    fun build(input: String): FailPeriod = periods.getOrElse(
            input, {throw InvalidPeriodDefinition()}
    )
}