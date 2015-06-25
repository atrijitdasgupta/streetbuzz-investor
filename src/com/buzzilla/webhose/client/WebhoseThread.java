package com.buzzilla.webhose.client;

import com.google.api.client.util.Key;

/**
 * @author Israel Tsadok
 */
public class WebhoseThread {

    @Key
    public String url;

    @Key("site_full")
    public String siteFull;

    @Key
    public String site;

    @Key("site_section")
    public String siteSection;

    @Key("section_title")
    public String sectionTitle;

    @Key
    public String title;

    @Key("title_full")
    public String titleFull;

    @Key
    public String published;

    @Key("replies_count")
    public int repliesCount;

    @Key("participants_count")
    public int participantsCount;

    @Key("site_type")
    public String siteType;

    @Key
    public String country;

    @Key("spam_score")
    public double spamScore;

    @Key("main_image")
    public String mainImage;
}
