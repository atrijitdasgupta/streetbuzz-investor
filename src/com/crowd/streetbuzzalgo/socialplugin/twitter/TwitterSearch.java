/**
 * 
 */
package com.crowd.streetbuzzalgo.socialplugin.twitter;

import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * @author Atrijit
 *
 */
public class TwitterSearch {

	/**
	 * 
	 */
	public TwitterSearch() {
		// TODO Auto-generated constructor stub
	}
	
	public static void search(String querystr){
		 Twitter twitter = new TwitterFactory().getInstance();
		 
		 try {
	            Query query = new Query(querystr);
	            QueryResult result;
	            do {
	                result = twitter.search(query);
	                List<Status> tweets = result.getTweets();
	                for (Status tweet : tweets) {
	                    System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
	                }
	            } while ((query = result.nextQuery()) != null);
	            System.exit(0);
	        } catch (TwitterException te) {
	            te.printStackTrace();
	            System.out.println("Failed to search tweets: " + te.getMessage());
	            System.exit(-1);
	        }
	    }
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		search("Jack Reacher Personal");

	}

}
