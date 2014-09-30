package com.prezi.fail.sarge.scout

import com.prezi.fail.sarge.SargeConfig

/**
 * Useful when targeting a specific hosts.
 */
class DnsScout() : Scout {
    override fun findTargets(by: String): List<String> = by.split(':').toList()
}