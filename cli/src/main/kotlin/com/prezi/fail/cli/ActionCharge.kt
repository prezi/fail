package com.prezi.fail.cli

import com.prezi.fail.sarge.Sarge

public class ActionCharge(val args: Array<String>) : Action() {
    class object {
        val verb = "charge"
        val cmdLineSyntax = "${verb} tag sapper duration-seconds [sapper-arg ...]"
        val requiredArgCount = 3
    }

    override fun run() {
        val searchTerm = args[0]
        val sapper = args[1]
        val duration = args[2]
        Sarge().charge(searchTerm, sapper, duration, args.drop(ActionCharge.requiredArgCount))
    }
}
