package com.prezi.changelog;

import org.apache.http.client.methods.HttpPost;

public class ChangelogAuthNoop implements ChangelogAuthProvider {
    @Override
    public void authorize(HttpPost request) {}
}
