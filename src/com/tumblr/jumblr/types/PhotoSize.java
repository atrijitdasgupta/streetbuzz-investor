package com.tumblr.jumblr.types;

import java.io.Serializable;

/**
 * This class represents a photo at a given size
 * @author jc
 */
public class PhotoSize implements Serializable{

    private int width, height;
    private String url;

    /**
     * Get the URL of this photo at this size
     * @return the URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Get the width of this photo
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the height of this photo
     * @return height
     */
    public int getHeight() {
        return height;
    }

}
