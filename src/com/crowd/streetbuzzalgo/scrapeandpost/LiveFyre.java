/**
 * 
 */
package com.crowd.streetbuzzalgo.scrapeandpost;

import com.livefyre.Livefyre;
import com.livefyre.core.Collection;
import com.livefyre.core.Network;
import com.livefyre.core.Site;
import com.livefyre.model.SiteData;

/**
 * @author Atrijit
 *
 */
public class LiveFyre {

	/**
	 * 
	 */
	public LiveFyre() {
		// TODO Auto-generated constructor stub
	}
	
	public void scrape(){
		Network network = Livefyre.getNetwork("www.tennis.com/", "exampleprodbase64key");
		Site site = network.getSite("291399", "examplesite100base64key");
		Collection commentsCollection = site.buildCommentsCollection("", "", "");
		SiteData sd = site.getData();
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
