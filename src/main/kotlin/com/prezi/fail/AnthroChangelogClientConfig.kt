package com.prezi.fail

import com.prezi.changelog.DefaultChangelogClientConfig

public class AnthroChangelogClientConfig() : DefaultChangelogClientConfig() {
    override fun defaultCriticality(): String {
        return "5"
    }

    override fun defaultCategory(): String {
        return "chaos"
    }
}