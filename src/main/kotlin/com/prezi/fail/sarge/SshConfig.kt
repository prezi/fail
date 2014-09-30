package com.prezi.fail.sarge

import com.prezi.fail.Config

enum class SshConfigKey(key: String) {
    AUTH_TYPE: SshConfigKey("fail.sarge.ssh.auth_type")
    DISABLE_STRICT_HOST_KEY_CHECKING: SshConfigKey("fail.sarge.ssh.disable_strict_host_key_checking")
}

enum class AuthType {
    NONE
    SSH_AGENT
}

class SshConfig : Config<SshConfigKey>() {
    val DEFAULT_AUTH_TYPE = AuthType.SSH_AGENT
    val DEFAULT_DISABLE_STRICT_HOST_KEY_CHECKING = true

    fun getAuthType() =  AuthType.valueOf(getString(SshConfigKey.AUTH_TYPE) ?: DEFAULT_AUTH_TYPE.toString())
    fun shouldDisableHostKeyChecking() = getBool(SshConfigKey.DISABLE_STRICT_HOST_KEY_CHECKING, DEFAULT_DISABLE_STRICT_HOST_KEY_CHECKING)
}
