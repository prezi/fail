package com.prezi.fail.sarge

import kotlin.test.assertFalse
import kotlin.test.assertEquals

class SargeConfigTest {
    test fun dryRunDefaultIsDifferentThanToggled() {
        assertEquals(SargeConfig.getToggledValue(SargeConfigKey.DRY_RUN), SargeConfig.DEFAULT_DRY_RUN)
    }
}