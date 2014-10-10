package com.prezi.fail

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Option

val home = System.getProperty("user.home")
fun inHome(path: String) = "${home}/${path}"

