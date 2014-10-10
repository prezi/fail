package com.prezi.fail.sarge.scout

import com.prezi.fail.ScoutType
import com.prezi.fail.SargeConfig

class ScoutFactory {
    fun build(t: ScoutType, c: SargeConfig): Scout = when (t) {
        ScoutType.TAG      -> TagScout(c)
        ScoutType.DNS -> DnsScout()
        else               -> throw Exception("Unknown scout type ${t}")
    }

    fun build(c: SargeConfig): Scout = build(c.getScoutType(), c)
}