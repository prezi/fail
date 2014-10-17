package com.prezi.fail.sarge

class SargeConfigTest {
    test fun dryRunDefaultIsDifferentThanToggled() {
        assertEquals(SargeConfig.getToggledValue(SargeConfigKey.DRY_RUN), SargeConfig.DEFAULT_DRY_RUN)
    }
}