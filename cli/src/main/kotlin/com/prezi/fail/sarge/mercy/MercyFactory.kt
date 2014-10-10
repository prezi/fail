package com.prezi.fail.sarge.mercy

import com.prezi.fail.MercyType
import com.prezi.fail.SargeConfig

class MercyFactory {
    [suppress("UNUSED_PARAMETER")]
    fun build(t: MercyType, c: SargeConfig): Mercy = when(t) {
        MercyType.NO_MERCY -> NoMercy()
        MercyType.HURT_JUST_ONE -> HurtJustOne()
        else               -> throw Exception("Unknown mercy type ${t}")
    }

    fun build(c: SargeConfig) = build(c.getMercyType(), c)
}
