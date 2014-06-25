package com.prezi.anthro.sarge

import java.io.File
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.auth.profile.ProfileCredentialsProvider

class Aws(val config: SargeConfig) {
    protected fun buildCredentials() : AWSCredentials? = ProfileCredentialsProvider().getCredentials()
    fun ec2() : AmazonEC2 = AmazonEC2Client(buildCredentials())
}
