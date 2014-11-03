package com.prezi.fail.sarge

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.prezi.fail.config.SargeConfig
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain

open class Aws(val config: SargeConfig) {
    protected fun buildCredentials() : AWSCredentials? = DefaultAWSCredentialsProviderChain().getCredentials()
    open fun ec2() : AmazonEC2 = AmazonEC2Client(buildCredentials())
}
