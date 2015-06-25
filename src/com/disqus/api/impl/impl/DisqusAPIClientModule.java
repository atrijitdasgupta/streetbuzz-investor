/**
 * 
 */
package com.disqus.api.impl.impl;

import com.crowd.streetbuzzalgo.scrapeandpost.ScrapeConstants;
import com.disqus.api.conf.AbstractDisqusAPIClientModule;

/**
 * @author Atrijit
 *
 */
public class DisqusAPIClientModule extends AbstractDisqusAPIClientModule implements ScrapeConstants{
	/**
     * Override this method and return yor Disqus App ID
     *
     * @return Disqus App ID
     */
	private String forumShortName;
	public DisqusAPIClientModule(String forumShortName){
		this.forumShortName = forumShortName;
	}
    @Override
    protected String forumShortName() {
       // return "ilikeplaces";
    	//return "disqus";
    	//return "crunchify-com";
    	return forumShortName;
    }
    
    /**
     * Override this method and return yor Disqus App ID
     *
     * @return Disqus App ID
     */
    @Override
    protected String appSecret() {
        return disqusapisecret;
    }
}
