package com.prezi.anthro.sarge

import com.prezi.anthro.Config
import java.security.InvalidParameterException

enum class SargeConfigKey(val key: String) {
    ROBOTS_TGZ_PATH : SargeConfigKey("anthro.sarge.targz")
    SCOUT_TYPE : SargeConfigKey("anthro.sarge.scoutType")
    override fun toString() = key
}

enum class ScoutType {
    IDENTITY
    TAG
}

class SargeConfig : Config<SargeConfigKey>() {
    val DEFAULT_SCOUT_TYPE = ScoutType.TAG
    val DEFAULT_ROBOTS_TGZ_PATH = "robots.tgz"

    fun getRobotsTargzPath() = getString(SargeConfigKey.ROBOTS_TGZ_PATH) ?: DEFAULT_ROBOTS_TGZ_PATH
    fun getScoutType() = ScoutType.valueOf(getString(SargeConfigKey.SCOUT_TYPE) ?: DEFAULT_SCOUT_TYPE.toString())
}
