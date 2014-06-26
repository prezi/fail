package com.prezi.anthro.sarge

import com.prezi.anthro.Config

enum class SargeConfigKey(val key: String) {
    TGZ_PATH : SargeConfigKey("anthro.sarge.targz")
    override fun toString() = key
}

class SargeConfig : Config<SargeConfigKey>() {
    fun getTargzPath() = getString(SargeConfigKey.TGZ_PATH) ?: "robots.tgz"
}
