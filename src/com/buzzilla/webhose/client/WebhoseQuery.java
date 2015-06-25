package com.buzzilla.webhose.client;

import com.google.api.client.repackaged.com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Israel Tsadok
 */
public class WebhoseQuery {
    public final List<String> allTerms = new ArrayList<String>();
    public final List<String> someTerms = new ArrayList<String>();
    public String phrase;
    public String exclude;
    public final List<SiteType> siteTypes = new ArrayList<SiteType>();
    public final List<String> language = new ArrayList<String>();
    public final List<String> sites = new ArrayList<String>();
    public final List<String> persons = new ArrayList<String>();
    public final List<String> locations = new ArrayList<String>();
    public final List<String> organizations = new ArrayList<String>();
    public String title;
    public String bodyText;

    @Override
    public String toString() {
        List<String> terms = new ArrayList<String>();

        addTerm(terms, allTerms, "AND", null);
        addTerm(terms, someTerms, "OR", null);
        if (phrase != null) {
            terms.add("\"" + phrase + "\"");
        }
        if (exclude != null) {
            terms.add("-(" + exclude + ")");
        }
        addTerm(terms, siteTypes, "OR", "site_type");
        addTerm(terms, language, "OR", "language");
        addTerm(terms, sites, "OR", "site");
        addTerm(terms, persons, "OR", "person");
        addTerm(terms, locations, "OR", "location");
        addTerm(terms, organizations, "OR", "organization");
        if (title != null) {
            terms.add("title:(" + title + ")");
        }
        if (bodyText != null) {
            terms.add("text:(" + bodyText+ ")");
        }
        return Joiner.on(" AND ").join(terms);
    }

    private void addTerm(List<String> terms, List<?> parts, String boolOp, String fieldName) {
        if(parts.isEmpty()) return;

        StringBuilder sb = new StringBuilder();
        sb.append("(");
        boolean first = true;
        for(Object part : parts) {
            if(first) {
                first = false;
            } else {
                sb.append(" ").append(boolOp).append(" ");
            }
            if(fieldName != null) {
                sb.append(fieldName).append(":");
            }
            if(part.toString().contains(" ")) {
                sb.append('"').append(part).append('"');
            } else {
                sb.append(part);
            }
        }
        sb.append(")");
        terms.add(sb.toString());
    }

    public static enum SiteType {
        news, blogs, discussions
    }
}
