package com.prezi.fail.test

import org.mockito.Mockito.mock
import org.mockito.Mockito.`when` as _when
import com.prezi.fail.sarge.SargeConfig
import org.mockito.Mockito.verify
import org.mockito.verification.VerificationMode

fun <T> givenAny(cls: Class<T>) = mock(cls)!!
fun <T> When(methodCall: T) = _when(methodCall)!!

fun <T> Verify(methodCall: T) = verify(methodCall)!!
fun <T> Verify(methodCall: T, mode: VerificationMode) = verify(methodCall, mode)!!

fun givenAnySargeConfig() = givenAny(javaClass<SargeConfig>())
