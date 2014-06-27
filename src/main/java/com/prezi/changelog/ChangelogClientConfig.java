package com.prezi.changelog;

public interface ChangelogClientConfig {
    public String getEndpoint();
    public Integer getCriticality();
    public String getCategory();
    public ChangelogAuthProviderType getAuthProviderType();
}
