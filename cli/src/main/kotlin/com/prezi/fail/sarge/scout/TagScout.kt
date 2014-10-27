package com.prezi.fail.sarge.scout

import com.prezi.fail.config.SargeConfig
import com.amazonaws.services.ec2.model.Filter

/**
 * Target AWS instances by tag. `by` can be:
 *  - a string containing a single '='. In this case we match by both tag key and value.
 *  - any other string, matched against tag keys only.
 */
public class TagScout(config: SargeConfig) : AwsScout(config) {
    val separator = '='
    override fun buildFilters(by: String): List<Filter> = if (by.count({it == separator}) == 1) {
        val parts = by.split(separator)
        val key = parts[0]
        val value = parts[1]
        listOf(Filter("tag-key", listOf(key)), Filter("tag-value", listOf(value)))
    } else {
        listOf(Filter("tag-key", listOf(by)))
    }
}
