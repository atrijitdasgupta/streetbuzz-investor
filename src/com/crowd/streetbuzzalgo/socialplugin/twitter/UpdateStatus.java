/**
 * 
 */
package com.crowd.streetbuzzalgo.socialplugin.twitter;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * @author Atrijit
 *
 */
public class UpdateStatus {

	/**
	 * 
	 */
	public UpdateStatus() {
		// TODO Auto-generated constructor stub
	}
	public static void update(String post){
		Twitter twitter = new TwitterFactory().getInstance();
		try {
			Status status = twitter.updateStatus(post);
			System.out.println("Successfully updated the status to [" + status.getText() + "].");
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		update("Our website http://www.7casp.in is up and running.");

	}

}
