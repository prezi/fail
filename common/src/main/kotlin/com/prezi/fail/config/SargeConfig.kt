package com.prezi.fail.config

import com.prezi.fail.config.Config
import org.apache.commons.cli.Option

enum class SargeConfigKey(val key: String, val opt: Option) {
    USE_CHANGELOG     : SargeConfigKey("fail.useChangelog", Option("c", "use-changelog", false, "Post events to changelog"))
    SAPPERS_TGZ_PATH  : SargeConfigKey("fail.sarge.targz", Option("p", "sappers", true, "Path to sappers tarball"))
    SCOUT_TYPE        : SargeConfigKey("fail.sarge.scoutType", Option("s", "scout-type", true, "How to find targets. DNS or TAG"))
    MERCY_TYPE        : SargeConfigKey("fail.sarge.mercyType", Option("a", "all", false, "Hurt all targets (no mercy mode)"))
    AVAILABILITY_ZONE : SargeConfigKey("fail.awsScout.availabilityZone", Option("Z", "availability-zone", true, "Limit targets to an availability zone. Only works with TAG scout type."))
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

    open fun getSappersTargzPath() = getString(SargeConfigKey.SAPPERS_TGZ_PATH) ?: DEFAULT_SAPPERS_TGZ_PATH
    open fun getScoutType() = ScoutType.valueOf(getString(SargeConfigKey.SCOUT_TYPE) ?: DEFAULT_SCOUT_TYPE.toString())
    open fun getMercyType() = MercyType.valueOf(getString(SargeConfigKey.MERCY_TYPE) ?: DEFAULT_MERCY_TYPE.toString())
    open fun useChangelog() = getBool(SargeConfigKey.USE_CHANGELOG, DEFAULT_USE_CHANGELOG)
    open fun getAvailabilityZone() = getString(SargeConfigKey.AVAILABILITY_ZONE) ?: DEFAULT_AVAILABILITY_ZONE

    override public fun getToggledValue(key: SargeConfigKey): String {
        return when (key) {
            SargeConfigKey.MERCY_TYPE -> MercyType.NO_MERCY.toString()
            SargeConfigKey.USE_CHANGELOG -> (!DEFAULT_USE_CHANGELOG).toString()
            else -> throw RuntimeException("SargeConfig.getToggledValue called with invalid key ${key}")
        }
    }
}