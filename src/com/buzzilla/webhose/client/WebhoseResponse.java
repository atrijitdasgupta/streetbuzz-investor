package com.buzzilla.webhose.client;

import com.google.api.client.util.Key;

import java.util.List;

/**
 * @author Israel Tsadok
 */
public class WebhoseResponse {
    @Key
    public int totalResults;

    @Key
    public String next;

    @Key
    public int requestsLeft;

    @Key
    public int moreResultsAvailable;

    @Key
    public List<WebhosePost> posts;
}
