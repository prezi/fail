package com.prezi.fail.sarge.mercy

public class NoMercy : Mercy {
    override fun deny(targets: List<String>): List<String> = targets
}