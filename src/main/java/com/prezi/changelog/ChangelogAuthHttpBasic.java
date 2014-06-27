package com.prezi.changelog;

import org.apache.http.client.methods.HttpPost;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChangelogAuthHttpBasic implements ChangelogAuthProvider {
    public static final String USERNAME_PROPERTY = "changelog.auth.httpBasic.username";
    public static final String PASSWORD_PROPERTY = "changelog.auth.httpBasic.password";

    protected Logger logger;

    public ChangelogAuthHttpBasic() {
        logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public void authorize(HttpPost request) {
        final String username = System.getProperty(USERNAME_PROPERTY);
        final String password = System.getProperty(PASSWORD_PROPERTY);
        final String header = "Basic " + Base64.encodeBase64String((username + ":" + password).getBytes());
        logger.debug(String.format("Authorizing with username=%s password=%s header=%s", username, password, header));
        request.setHeader("Authorization", header);
    }
}
