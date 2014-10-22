package com.prezi.fail.api.auth

import com.prezi.fail.config.Config


enum class HttpBasicAuthProviderConfigKey(val key: String) {
    USERNAME: HttpBasicAuthProviderConfigKey("fail.cli.auth.basicHTTP.username")
    PASSWORD: HttpBasicAuthProviderConfigKey("fail.cli.auth.basicHTTP.password")
    override fun toString(): String = key
}


class HttpBasicAuthProviderConfig: Config<HttpBasicAuthProviderConfigKey>() {
    fun getUsername(): String = getString(HttpBasicAuthProviderConfigKey.USERNAME)
            ?: throw RuntimeException("HTTP Basic Auth provider is used, ${HttpBasicAuthProviderConfigKey.USERNAME} must be set")

    fun getPassword(): String = getString(HttpBasicAuthProviderConfigKey.PASSWORD)
            ?: throw RuntimeException("HTTP Basic Auth provider is used, ${HttpBasicAuthProviderConfigKey.PASSWORD} must be set")

    override fun getToggledValue(key: HttpBasicAuthProviderConfigKey): String {
        throw UnsupportedOperationException()
    }
}