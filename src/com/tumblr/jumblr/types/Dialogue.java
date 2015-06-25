package com.tumblr.jumblr.types;

import java.io.Serializable;

/**
 * This class represents an individual statement in a ChatPost
 * @author jc
 */
public class Dialogue implements Serializable{

    private String name;
    private String label;
    private String phrase;

    /**
     * Get the name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the label
     * @return label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Get the phrase
     * @return the phrase
     */
    public String getPhrase() {
        return phrase;
    }

}
