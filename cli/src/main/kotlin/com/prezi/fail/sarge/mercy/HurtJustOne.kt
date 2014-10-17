package com.prezi.fail.sarge.mercy

import java.util.Collections

public class HurtJustOne : Mercy {
    override fun deny(targets: List<String>): List<String> = if (targets.empty) {
        listOf()
    } else {
        val targetsCopy: MutableList<String> = targets.toArrayList()
        Collections.shuffle(targetsCopy)
        listOf(targetsCopy[0])
    }
}