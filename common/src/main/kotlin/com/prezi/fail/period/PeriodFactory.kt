package com.prezi.fail.period


object PeriodFactory {
    class InvalidPeriodDefinition: RuntimeException()

    fun build(input: String): Period = when(input) {
        "every-hour" -> EveryHour()
        else         -> throw InvalidPeriodDefinition()
    }
}
