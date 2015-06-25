/**
 * 
 */
package com.crowd.streetbuzzalgo.socialplugin.twitter;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzzalgo.constants.SystemConstants;

/**
 * @author Atrijit
 *
 */
public class TwitterAuthenticate implements SystemConstants, Constants{

	/**
	 * 
	 */
	public TwitterAuthenticate() {
		// TODO Auto-generated constructor stub
	}
	
	public static void auth(){
		Properties prop = new Properties();
		//File file = new File("twitter4j.properties");
		File file = new File(BASEEXTLIBPATH+"twitter4j/twitter4j.properties");
		 prop.setProperty("oauth.consumerKey", TWITTER_KEY);
         prop.setProperty("oauth.consumerSecret", TWITTER_SECRET);
        // InputStream is = null;
         OutputStream os = null;
         try {
			os = new FileOutputStream("twitter4j.properties");
			 prop.store(os, "twitter4j.properties");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		 Twitter twitter = new TwitterFactory().getInstance();
		 RequestToken requestToken = null;
         try {
			requestToken = twitter.getOAuthRequestToken();
			System.out.println("Got request token.");
            System.out.println("Request token: " + requestToken.getToken());
            System.out.println("Request token secret: " + requestToken.getTokenSecret());
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AccessToken accessToken = null;
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
         while (null == accessToken) {
             System.out.println("Open the following URL and grant access to your account:");
             System.out.println(requestToken.getAuthorizationURL());
             try {
                 Desktop.getDesktop().browse(new URI(requestToken.getAuthorizationURL()));
             } catch (UnsupportedOperationException ignore) {
             } catch (IOException ignore) {
             } catch (URISyntaxException e) {
                 throw new AssertionError(e);
             }
             System.out.print("Enter the PIN(if available) and hit enter after you granted access.[PIN]:");
             String pin = "";
             try {
				pin = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             try {
                 if (pin.length() > 0) {
                     accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                 } else {
                     accessToken = twitter.getOAuthAccessToken(requestToken);
                 }
             } catch (TwitterException te) {
                 if (401 == te.getStatusCode()) {
                     System.out.println("Unable to get the access token.");
                 } else {
                     te.printStackTrace();
                 }
             }
         }
         
         System.out.println("Got access token.");
         System.out.println("Access token: " + accessToken.getToken());
         System.out.println("Access token secret: " + accessToken.getTokenSecret());
		
         prop.setProperty("oauth.accessToken", accessToken.getToken());
         prop.setProperty("oauth.accessTokenSecret", accessToken.getTokenSecret());
         try {
			os = new FileOutputStream(file);
			 prop.store(os, (BASEEXTLIBPATH+"twitter4j/twitter4j.properties"));
			 os.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		auth();

	}

}
