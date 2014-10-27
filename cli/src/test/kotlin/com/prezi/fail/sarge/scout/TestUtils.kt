package com.prezi.fail.sarge.scout

import com.prezi.fail.test.givenAny
import com.prezi.fail.config.SargeConfig

fun givenAnySargeConfig() = givenAny(javaClass<SargeConfig>())
