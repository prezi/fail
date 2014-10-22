package com.prezi.fail.api.auth

import com.linkedin.restli.client.Request
import com.linkedin.restli.client.AbstractRequestBuilder


public trait AuthProvider {
    fun authenticate(requestBuilder: AbstractRequestBuilder<*, *, *>)
}