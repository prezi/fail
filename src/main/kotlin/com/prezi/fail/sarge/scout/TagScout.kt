package com.prezi.fail.sarge.scout

import com.prezi.fail.sarge.SargeConfig
import com.amazonaws.services.ec2.model.Filter

/**
 * Target AWS instances that have the tag `by` set.
 */
public class TagScout(config: SargeConfig) : AwsScout(config) {
    override fun buildFilters(by: String): List<Filter> = listOf(Filter("tag-key", listOf(by)))
}