package com.prezi.changelog;

import java.io.*;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.json.JSONObject;


public class ChangelogClient {
    protected ChangelogClientConfig config;
    protected ChangelogAuthProvider authProvider;
    protected Logger logger;

    public ChangelogClient(ChangelogClientConfig config) {
        this.config = config;
        this.authProvider = new ChangelogAuthProviderFactory().build(config);
        this.logger = LoggerFactory.getLogger(this.getClass());
        if (config.getEndpoint() == null) {
            throw new RuntimeException("changelog endpoint not set");
        }
    }

    public ChangelogClient() {
        this(new DefaultChangelogClientConfig());
    }

    public void send(Integer criticality, String description) throws IOException {
        send(criticality, config.getCategory(), description);
    }

    public void send(String category, String description) throws IOException {
        send(config.getCriticality(), category, description);
    }

    public void send(String description) throws IOException {
        send(config.getCriticality(), config.getCategory(), description);
    }

    public void send(Integer criticality, String category, String description) throws IOException {
        logger.info(String.format(
                "Sending changelog event endpoint=%s criticality=%s category=%s description=%s",
                config.getEndpoint(), criticality, category, description));
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try {
            HttpPost request = new HttpPost(config.getEndpoint());
            setJsonData(criticality, category, description, request);
            authProvider.authorize(request);
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() != 201) {
                logger.error(formatResponse(response));
                throw new RuntimeException("Sending failed, see log above for response.");
            } else {
                logger.debug(formatResponse(response));
            }
        } finally {
            httpClient.close();
        }
    }

    protected void setJsonData(Integer criticality, String category, String description, HttpPost request) throws UnsupportedEncodingException {
        JSONObject json = buildJson(criticality, category, description);
        request.setEntity(new StringEntity(json.toString()));
        request.addHeader("content-type", "application/json");
    }

    protected JSONObject buildJson(Integer criticality, String category, String description) {
        JSONObject json = new JSONObject();
        json.put("criticality", criticality);
        json.put("category", category);
        json.put("description", description);
        json.put("unix_timestamp", new Date().getTime() / 1000);
        return json;
    }

    protected String formatResponse(HttpResponse response) throws IOException {
        return String.format(
                "Response: status=%s body=%s",
                response.getStatusLine().getStatusCode(),
                inputStreamToString(response.getEntity().getContent()));
    }

    protected String inputStreamToString(InputStream stream) throws IOException {
        String line = null;
        StringBuilder builder = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(stream));
        while ((line = rd.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }
}
