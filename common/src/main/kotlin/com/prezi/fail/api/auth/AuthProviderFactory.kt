package com.prezi.fail.api.auth

import com.linkedin.restli.client.AbstractRequestBuilder
import com.linkedin.restli.client.Request


class AuthProviderFactory(val config: AuthProviderConfig = AuthProviderConfig()) {
    fun build(): AuthProvider = when(config.getAuthProviderType()) {
        AuthProviderType.NOOP            -> NoopAuthProvider()
        AuthProviderType.HTTP_BASIC_AUTH -> HttpBasicAuthProvider()
    }
}

