package com.prezi.anthro

val home = System.getProperty("user.home")
fun inHome(path: String) = "${home}/${path}"
