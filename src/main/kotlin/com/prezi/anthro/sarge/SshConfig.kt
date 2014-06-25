package com.prezi.anthro.sarge

import com.prezi.anthro.Config

enum class SshConfigKey(key: String) {
    AUTH_TYPE: SshConfigKey("anthro.sarge.ssh.auth_type")
    DISABLE_STRICT_HOST_KEY_CHECKING: SshConfigKey("anthro.sarge.ssh.disable_strict_host_key_checking")
}

enum class AuthType {
    NONE
    SSH_AGENT
}

class SshConfig : Config<SshConfigKey>() {
    fun getAuthType() =  AuthType.valueOf(getString(SshConfigKey.AUTH_TYPE) ?: AuthType.SSH_AGENT.toString())
    fun shouldDisableHostKeyChecking() = getBool(SshConfigKey.DISABLE_STRICT_HOST_KEY_CHECKING, true)
}
