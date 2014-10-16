package com.prezi.fail.period


public class EveryHour: Period {
    override fun nextRun(after: Long): Long = (after / 3600 + 1) * 3600
}
