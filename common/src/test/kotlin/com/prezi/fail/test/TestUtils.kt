package com.prezi.fail.test

import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when` as _when
import org.mockito.stubbing.OngoingStubbing
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs
import org.mockito.verification.VerificationMode

public fun <T> givenAny(cls: Class<T>): T = mock(cls, ReturnsDeepStubs())!!
public fun <T> When(methodCall: T): OngoingStubbing<T> = _when(methodCall)!!

public fun <T> Verify(methodCall: T): T = org.mockito.Mockito.verify(methodCall)!!
public fun <T> Verify(methodCall: T, mode: VerificationMode): T = verify(methodCall, mode)!!
