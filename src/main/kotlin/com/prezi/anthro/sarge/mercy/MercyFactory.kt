package com.prezi.anthro.sarge.mercy

import com.prezi.anthro.sarge.SargeConfig
import com.prezi.anthro.sarge.MercyType

class MercyFactory {
    fun build(t: MercyType, c: SargeConfig): Mercy = when(t) {
        MercyType.NO_MERCY -> NoMercy()
        MercyType.HURT_JUST_ONE -> HurtJustOne()
        else               -> throw Exception("Unknown mercy type ${t}")
    }

    fun build(c: SargeConfig) = build(c.getMercyType(), c)
}
