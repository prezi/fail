package com.prezi.fail.api.auth

import com.prezi.fail.config.Config

enum class AuthProviderType {
    NOOP
    HTTP_BASIC_AUTH
}

enum class AuthProviderConfigKey(val key: String) {
    AUTH_PROVIDER_TYPE: AuthProviderConfigKey("fail.cli.auth.provider")
    override fun toString(): String = key
}

public class AuthProviderConfig: Config<AuthProviderConfigKey>() {
    val DEFAULT_AUTH_PROVIDER_TYPE = AuthProviderType.NOOP.toString()

    fun getAuthProviderType(): AuthProviderType = AuthProviderType.valueOf(getString(AuthProviderConfigKey.AUTH_PROVIDER_TYPE) ?: DEFAULT_AUTH_PROVIDER_TYPE)

    override fun getToggledValue(key: AuthProviderConfigKey): String {
        throw UnsupportedOperationException()
    }
}
