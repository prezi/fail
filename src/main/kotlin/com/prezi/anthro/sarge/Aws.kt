package com.prezi.anthro.sarge

import java.io.File
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.PropertiesCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.ec2.AmazonEC2Client

class Aws(val config: SargeConfig) {

    fun buildCredentials() : AWSCredentials {
        val fileName = config.getAwsCredentialsFile()
        if (fileName != null) {
            return PropertiesCredentials(File(
                    fileName.replaceFirst("^~", System.getProperty("user.home")!!)
            ))
        } else {
            return BasicAWSCredentials(
                    config.getAwsAccessKey() ?: throw Exception("One of ${SargeConfigKey.AWS_CREDENTIALS_FILE} or ${SargeConfigKey.AWS_ACCESS_KEY} must be set!"),
                    config.getAwsSecretKey() ?: throw Exception("One of ${SargeConfigKey.AWS_CREDENTIALS_FILE} or ${SargeConfigKey.AWS_SECRET_KEY} must be set!")
            )
        }
    }

    fun ec2() : AmazonEC2 = AmazonEC2Client(buildCredentials())
}
