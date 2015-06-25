/**
 * 
 */
package com.crowd.streetbuzzalgo.parser;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.crowd.streetbuzzalgo.constants.SystemConstants;

import es.daedalus.textalytics.sma.SmaClient;
import es.daedalus.textalytics.sma.domain.Result;
import es.daedalus.textalytics.sma.domain.Sentiment;

/**
 * @author Atrijit
 *
 */
public class TextalyticsClient implements SystemConstants {

	/**
	 * 
	 */
	public TextalyticsClient() {
		// TODO Auto-generated constructor stub
	}
	public static void analyseSentiment(String phrase){
		
	}
	private static void analyse(String phrase){
		SmaClient smaclient = new SmaClient(TEXTALYTICS_KEY);
		try {
			Result result = smaclient.analyze(phrase);
			Sentiment sentiment = result.getSentiment();
			System.out.println(sentiment.name());
		} catch (ClientProtocolException e) {
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
		// TODO Auto-generated method stub
		analyse("Narendra Modi and BJP is going to win in Delhi and AAP fail.");
	}

}
