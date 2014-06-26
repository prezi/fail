package com.prezi.anthro.sarge

import com.prezi.anthro.Config
import java.security.InvalidParameterException

enum class SargeConfigKey(val key: String) {
    ROBOTS_TGZ_PATH : SargeConfigKey("anthro.sarge.targz")
    SCOUT_TYPE      : SargeConfigKey("anthro.sarge.scoutType")
    MERCY_TYPE      : SargeConfigKey("anthro.sarge.mercyType")
    override fun toString() = key
}

enum class ScoutType {
    PUBLIC_DNS
    TAG
}

enum class MercyType {
    NO_MERCY
    HURT_JUST_ONE
}

class SargeConfig : Config<SargeConfigKey>() {
    val DEFAULT_SCOUT_TYPE = ScoutType.TAG
    val DEFAULT_ROBOTS_TGZ_PATH = "robots.tgz"
    val DEFAULT_MERCY_TYPE = MercyType.HURT_JUST_ONE

    fun getRobotsTargzPath() = getString(SargeConfigKey.ROBOTS_TGZ_PATH) ?: DEFAULT_ROBOTS_TGZ_PATH
    fun getScoutType() = ScoutType.valueOf(getString(SargeConfigKey.SCOUT_TYPE) ?: DEFAULT_SCOUT_TYPE.toString())
    fun getMercyType() = MercyType.valueOf(getString(SargeConfigKey.MERCY_TYPE) ?: DEFAULT_MERCY_TYPE.toString())
}
