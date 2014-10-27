package com.prezi.fail.sarge.scout

// JUnit, test helpers
import kotlin.test.*
import org.junit.Test as test
import com.prezi.fail.test.*
// Mockito
import org.mockito.Mockito.spy
import org.mockito.Matchers.any
// AWS
import com.amazonaws.services.ec2.model.Filter
import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.ec2.model.DescribeInstancesResult
import com.amazonaws.services.ec2.model.Reservation
import com.amazonaws.services.ec2.model.DescribeInstancesRequest
// System under test
import com.prezi.fail.sarge.Aws
import com.prezi.fail.config.SargeConfig


class AwsScoutTest  {
    open class TestAwsScout(config: SargeConfig) : AwsScout(config: SargeConfig) {
        override val aws = givenAny(javaClass<Aws>())
        val ec2 = givenAny(javaClass<AmazonEC2>())
        val describeInstancesResult = givenAny(javaClass<DescribeInstancesResult>())
        val reservations: MutableList<Reservation> = arrayListOf();
        val filters: List<Filter> = listOf(givenAny(javaClass<Filter>()))

        override fun buildFilters(by: String): List<Filter> = filters

        {
            When(aws.ec2()).thenReturn(ec2)
            When(ec2.describeInstances(any<DescribeInstancesRequest>())).thenReturn(describeInstancesResult)
            When(describeInstancesResult.getReservations()).thenReturn(reservations)
        }
    }

    fun givenAnyTestScout(): TestAwsScout = spy(TestAwsScout(givenAnySargeConfig()))!!

    test fun noAvailabilityZoneFilter() {
        val scout = givenAnyTestScout()
        When(scout.config.getAvailabilityZone()).thenReturn(null)
        assertTrue(scout.availabilityZoneFilter().empty)
    }

    test fun availabilityZoneFilter() {
        val az = "us-east-1c"
        val scout = givenAnyTestScout()
        When(scout.config.getAvailabilityZone()).thenReturn(az)
        assertEquals(scout.availabilityZoneFilter(), listOf(Filter("availability-zone", listOf(az))))
    }

    test fun findTargets() {
        val filterBy = "foobar"
        val scout = givenAnyTestScout()
        scout.findTargets(filterBy)
        Verify(scout).buildFilters(filterBy)
        Verify(scout.ec2).describeInstances(
                DescribeInstancesRequest().withFilters(scout.buildFilters(filterBy))
        )
    }
}

