package com.prezi.anthro.sarge.scout

import com.prezi.anthro.sarge.ScoutType
import com.prezi.anthro.sarge.SargeConfig

class ScoutFactory {
    fun build(t: ScoutType, c: SargeConfig): Scout = when (t) {
        ScoutType.TAG      -> TagScout(c)
        ScoutType.PUBLIC_DNS -> PublicDnsScout()
        else               -> throw Exception("Unknown scout type ${t}")
    }

    fun build(c: SargeConfig): Scout = build(c.getScoutType(), c)
}