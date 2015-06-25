/**
 * 
 */
package com.crowd.streetbuzzalgo.socialplugin.twitter;

import twitter4j.Location;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * @author Atrijit
 *
 */
public class TwitterTrends {

	/**
	 * 
	 */
	public TwitterTrends() {
		// TODO Auto-generated constructor stub
	}
	
	public static void trends(){
		 Twitter twitter = new TwitterFactory().getInstance();
         ResponseList<Location> locations = null;
         try {
			locations = twitter.getAvailableTrends();
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Location location : locations) {
            System.out.println(location.getName() + " (woeid:" + location.getWoeid() + ")");
        }
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
