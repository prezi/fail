package com.prezi.anthro.sarge

class Sarge(config: SargeConfig = SargeConfig()) {
    val aws = Aws(config)

    fun hello() {
        println(aws.ec2().describeInstances())
    }
}