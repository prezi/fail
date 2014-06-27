package com.prezi.changelog;

import org.apache.http.client.methods.HttpPost;

public interface ChangelogAuthProvider {
    public void authorize(HttpPost request);
}
