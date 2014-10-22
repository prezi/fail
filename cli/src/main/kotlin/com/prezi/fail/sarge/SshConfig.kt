package com.prezi.fail.sarge

import com.prezi.fail.config.Config

enum class SshConfigKey(val key: String) {
    USERNAME: SshConfigKey("fail.sarge.ssh.username")
    AUTH_TYPE: SshConfigKey("fail.sarge.ssh.auth_type")
    DISABLE_STRICT_HOST_KEY_CHECKING: SshConfigKey("fail.sarge.ssh.disable_strict_host_key_checking")
    PRIVATE_KEY_FILE_PATH: SshConfigKey("fail.sarge.ssh.private_key_file")  // Only used when auth_type == PRIVATE_KEY_FILE
    override public fun toString(): String = key
}

enum class AuthType {
    NONE
    SSH_AGENT
    PRIVATE_KEY_FILE
}

class SshConfig : Config<SshConfigKey>() {
    val DEFAULT_AUTH_TYPE = AuthType.SSH_AGENT
    val DEFAULT_DISABLE_STRICT_HOST_KEY_CHECKING = true
    val DEFAULT_USERNAME = "root"

    fun getUsername() = getString(SshConfigKey.USERNAME) ?: DEFAULT_USERNAME
    fun getAuthType() =  AuthType.valueOf(getString(SshConfigKey.AUTH_TYPE) ?: DEFAULT_AUTH_TYPE.toString())
    fun shouldDisableHostKeyChecking() = getBool(SshConfigKey.DISABLE_STRICT_HOST_KEY_CHECKING, DEFAULT_DISABLE_STRICT_HOST_KEY_CHECKING)
    fun getPrivateKeyFilePath() = getString(SshConfigKey.PRIVATE_KEY_FILE_PATH)

    override fun getToggledValue(key: SshConfigKey): String {
        throw UnsupportedOperationException()
    }
}
