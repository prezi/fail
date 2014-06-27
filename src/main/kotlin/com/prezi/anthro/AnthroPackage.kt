package com.prezi.anthro

import com.prezi.anthro.sarge.Sarge
import com.prezi.changelog.ChangelogClient

fun main(args: Array<String>) {
    Sarge().charge(args[0], args[1], args[2])
}
