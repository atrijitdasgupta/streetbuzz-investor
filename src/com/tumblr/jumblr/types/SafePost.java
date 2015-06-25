package com.tumblr.jumblr.types;

import java.io.IOException;
import java.io.Serializable;

/**
 * Save Post and not need to raise IOException
 * @author jc
 */
class SafePost extends Post implements Serializable{

    /**
     * save swallowing IOException (only for audio, video, photo)
     */
    @Override
    public void save() {
        try {
            super.save();
        } catch (IOException ex) {
            // No files involved, no IOException
        }
    }

}
