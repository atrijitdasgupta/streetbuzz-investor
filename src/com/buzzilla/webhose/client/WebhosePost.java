package com.buzzilla.webhose.client;

import com.google.api.client.util.Key;

import java.util.List;

/**
 * @author Israel Tsadok
 */
public class WebhosePost {

    @Key
    public String url;

    @Key
    public String title;

    @Key
    public String author;

    @Key
    public String text;

    @Key
    public String published;

    @Key
    public String crawled;

    @Key("ord_in_thread")
    public int ordInThread;

    @Key
    public String language;

    @Key("external_links")
    public List<String> externalLinks;

    @Key
    public List<String> persons;

    @Key
    public List<String> locations;

    @Key
    public List<String> organizations;

    @Key
    public WebhoseThread thread;
}
