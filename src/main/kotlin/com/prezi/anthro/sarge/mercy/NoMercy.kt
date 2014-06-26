package com.prezi.anthro.sarge.mercy

public class NoMercy : Mercy {
    override fun spare(targets: List<String>): List<String> = targets
}