package com.prezi.changelog;

public class ChangelogAuthProviderFactory {
    public ChangelogAuthProvider build(ChangelogClientConfig config) {
        switch (config.getAuthProviderType()) {
            case HTTP_BASIC_AUTH: return new ChangelogAuthHttpBasic();
            case NOOP           : return new ChangelogAuthNoop();
            default             : throw new RuntimeException("Unknown auth provider type " + config.getAuthProviderType());
        }
    }
}