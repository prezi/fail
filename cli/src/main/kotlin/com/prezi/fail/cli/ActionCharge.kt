package com.prezi.fail.cli

import com.prezi.fail.sarge.Sarge

public class ActionCharge(val searchTerm: String, val sapper: String, val duration: String, val args: List<String>) : Action {
    override fun run() {
        Sarge().charge(searchTerm, sapper, duration, args)
    }
}
