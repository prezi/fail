package com.prezi.anthro.sarge.mercy

public class NoMercy : Mercy {
    override fun deny(targets: List<String>): List<String> = targets
}