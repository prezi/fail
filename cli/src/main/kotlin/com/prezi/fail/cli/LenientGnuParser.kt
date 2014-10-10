package com.prezi.fail.cli

import org.apache.commons.cli.GnuParser

public class LenientGnuParser: GnuParser() {
    override fun processOption(arg: String?, iter: ListIterator<Any?>?) {
        if (getOptions()!!.hasOption(arg)) {
            super.processOption(arg, iter);
        }
    }
}