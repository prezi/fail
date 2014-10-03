package com.prezi.fail.sarge

import com.prezi.fail.Config
import java.security.InvalidParameterException

enum class SargeConfigKey(val key: String) {
    USE_CHANGELOG     : SargeConfigKey("fail.useChangelog")
    DRY_RUN           : SargeConfigKey("fail.dryRun")
    SAPPERS_TGZ_PATH  : SargeConfigKey("fail.sarge.targz")
    SCOUT_TYPE        : SargeConfigKey("fail.sarge.scoutType")
    MERCY_TYPE        : SargeConfigKey("fail.sarge.mercyType")
    AVAILABILITY_ZONE : SargeConfigKey("fail.awsScout.availabilityZone")
    override fun toString() = key
}

enum class ScoutType {
    DNS
    TAG
}

enum class MercyType {
    NO_MERCY
    HURT_JUST_ONE
}

open class SargeConfig : Config<SargeConfigKey>() {
    val DEFAULT_SCOUT_TYPE = ScoutType.TAG
    val DEFAULT_SAPPERS_TGZ_PATH = javaClass.getProtectionDomain()?.getCodeSource()?.getLocation()?.toURI()?.resolve("../sappers.tgz")?.getPath()
    val DEFAULT_MERCY_TYPE = MercyType.HURT_JUST_ONE
    val DEFAULT_USE_CHANGELOG = false
    val DEFAULT_AVAILABILITY_ZONE = null
    val DEFAULT_DRY_RUN = false

    open fun getSappersTargzPath() = getString(SargeConfigKey.SAPPERS_TGZ_PATH) ?: DEFAULT_SAPPERS_TGZ_PATH
    open fun getScoutType() = ScoutType.valueOf(getString(SargeConfigKey.SCOUT_TYPE) ?: DEFAULT_SCOUT_TYPE.toString())
    open fun getMercyType() = MercyType.valueOf(getString(SargeConfigKey.MERCY_TYPE) ?: DEFAULT_MERCY_TYPE.toString())
    open fun useChangelog() = getBool(SargeConfigKey.USE_CHANGELOG, DEFAULT_USE_CHANGELOG)
    open fun getAvailabilityZone() = getString(SargeConfigKey.AVAILABILITY_ZONE) ?: DEFAULT_AVAILABILITY_ZONE
    open fun isDryRun() = getBool(SargeConfigKey.DRY_RUN, DEFAULT_DRY_RUN)
}
