package com.prezi.anthro.sarge

import com.prezi.anthro.Config

enum class SargeConfigKey(val key: String) {
    AWS_CREDENTIALS_FILE: SargeConfigKey("anthro.sarge.aws.credentials_file")
    AWS_ACCESS_KEY:       SargeConfigKey("anthro.sarge.aws.access_key")
    AWS_SECRET_KEY:       SargeConfigKey("anthro.sarge.aws.secret_key")

    override fun toString() = key
}

class SargeConfig : Config<SargeConfigKey>() {
    fun getAwsCredentialsFile() = getString(SargeConfigKey.AWS_CREDENTIALS_FILE)
    fun getAwsAccessKey() = getString(SargeConfigKey.AWS_ACCESS_KEY)
    fun getAwsSecretKey() = getString(SargeConfigKey.AWS_SECRET_KEY)
}
