package com.prezi.anthro.sarge

class ScoutFactory {
    fun build(t: ScoutType, c: SargeConfig): Scout = when (t) {
        ScoutType.TAG -> TagScout(c)
        else          -> throw Exception("Unknown scout type ${t}")
    }

    fun build(c: SargeConfig): Scout = build(c.getScoutType(), c)
}
