package com.prezi.anthro.sarge.scout

import com.prezi.anthro.sarge.SargeConfig

/**
 * Just return the input passed in. Useful when targeting a single host.
 */
class IdentityScout(config: SargeConfig) : Scout(config) {
    override fun findTargets(by: String): List<String> = listOf(by)
}