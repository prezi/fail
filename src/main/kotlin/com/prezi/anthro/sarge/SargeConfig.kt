package com.prezi.anthro.sarge

import com.prezi.anthro.Config

enum class SargeConfigKey(val key: String) {
    override fun toString() = key
}

class SargeConfig : Config<SargeConfigKey>()
