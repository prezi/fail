package com.prezi.fail.api.auth

import com.linkedin.restli.client.Request
import com.linkedin.restli.client.AbstractRequestBuilder

class NoopAuthProvider: AuthProvider {
    override fun authenticate(requestBuilder: AbstractRequestBuilder<*, *, *>) {}
}
