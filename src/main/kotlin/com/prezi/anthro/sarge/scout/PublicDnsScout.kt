package com.prezi.anthro.sarge.scout

import com.prezi.anthro.sarge.SargeConfig

/**
 * Useful when targeting a specific hosts.
 */
class PublicDnsScout() : Scout {
    override fun findTargets(by: String): List<String> = by.split(':').toList()
}