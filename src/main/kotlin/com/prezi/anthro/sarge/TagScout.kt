package com.prezi.anthro.sarge

import com.amazonaws.services.ec2.model.Instance
import com.amazonaws.services.ec2.model.Filter

public class TagScout(config: SargeConfig) : Scout(config) {
    override fun buildFilters(by: String): List<Filter> = listOf(Filter("tag-key", listOf(by)))
}