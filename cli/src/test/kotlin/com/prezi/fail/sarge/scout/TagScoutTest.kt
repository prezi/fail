package com.prezi.fail.sarge.scout

// JUnit, test helpers
import kotlin.test.*
import org.junit.Test as test
import com.prezi.fail.test.*
// AWS
import com.amazonaws.services.ec2.model.Filter


public class TagScoutTest {
    fun buildFilters(by: String) = TagScout(givenAnySargeConfig()).buildFilters(by)

    test fun keyValue() {
        val key = "TestTagKey"
        val value = "TestTagValue"
        assertEquals(
                buildFilters("${key}=${value}"),
                listOf(Filter("tag-key", listOf(key)), Filter("tag-value", listOf(value)))
        )
    }

    test fun twoEqualSigns() {
        val input = "a=b=c"
        assertEquals(
                buildFilters(input),
                listOf(Filter("tag-key", listOf(input)))
        )
    }

    test fun noEqualSigns() {
        val input = "abc"
        assertEquals(
                buildFilters(input),
                listOf(Filter("tag-key", listOf(input)))
        )
    }
}
