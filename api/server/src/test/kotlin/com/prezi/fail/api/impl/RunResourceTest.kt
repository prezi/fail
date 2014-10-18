package com.prezi.fail.api.impl

import org.junit.Test
import org.mockito.Matchers.*
import com.prezi.fail.test.givenAny
import com.prezi.fail.api.db.DB
import com.prezi.fail.api.Run
import com.prezi.fail.api.RunStatus
import com.prezi.fail.api.ScheduledFailure
import com.prezi.fail.test.When
import kotlin.test.assertEquals
import com.prezi.fail.api.db.DBRun
import com.prezi.fail.api.db.DBScheduledFailure
import org.junit.Ignore

class RunResourceTest {
    [Ignore]
    Test fun getPopulatesScheduledFailure() {
        val db = givenAny(javaClass<DB>())
        val resource = RunResource(db)
        val dbrun = DBRun().setId("1")?.setAt(0)?.setLog("testlog")?.setStatus(RunStatus.DONE)
        val scheduled = DBScheduledFailure()
        When(db.mapper.load<DBRun>(any())).thenReturn(dbrun)
        When(db.mapper.load<DBScheduledFailure>(any())).thenReturn(scheduled)
        val run = resource.get(dbrun?.getId())
        assertEquals(scheduled.model, run?.getScheduledFailure())
    }
}