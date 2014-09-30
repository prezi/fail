package com.prezi.fail.sarge.mercy

import java.util.Collections
import java.util.ArrayList

public class HurtJustOne : Mercy {
    override fun deny(targets: List<String>): List<String> {
        val targetsCopy: MutableList<String> = targets.toArrayList()
        Collections.shuffle(targetsCopy)
        return listOf(targetsCopy[0])
    }
}