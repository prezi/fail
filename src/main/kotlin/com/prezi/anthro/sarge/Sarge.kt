package com.prezi.anthro.sarge

class Sarge(config: SargeConfig = SargeConfig()) {
    val aws = Aws(config)
    val ssh = Ssh()

    fun hello() {
//        println(aws.ec2().describeInstances())
        ssh.exec("oam3.us.prezi.private", "echo hello from \$HOSTNAME")
    }
}