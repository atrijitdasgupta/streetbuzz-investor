/**
 * ProofreadText.java
 * 
 * @contact    http://www.textalytics.com (http://www.daedalus.es)
 * @copyright  Copyright (c) 2014, DAEDALUS S.A. All rights reserved.
 */
package es.daedalus.textalytics.sempub.examples;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import es.daedalus.textalytics.sempub.Result;
import es.daedalus.textalytics.sempub.SempubClient;
import es.daedalus.textalytics.sempub.TextalyticsClientException;
import es.daedalus.textalytics.sempub.domain.Document;
import es.daedalus.textalytics.sempub.domain.Document.Language;

/**
 * This script runs an instance of SemPubClient in order to use Textalytics Proofreading API service to
 * proofread a text. There are two ways of proofreading a text, either by sending a json Document object
 * (option 1) or just sending the text to analyze (option 2).
 *
 * In order to run this example, the license key obtained for the Semantic Publishing API must be included
 * in the KEY variable  in 'src/main/resources/config.properties' file. If you don't know your key, check 
 * your personal area at Textalytics (https://textalytics.com/personal_area)
 *
 * @author     Textalytics
 * @version    1.0 -- 02/2014
 */
public class ProofreadText {

	public static void main(String[] args) 
			throws ClientProtocolException, TextalyticsClientException, IOException {
		// Creates SempubClient object
		SempubClient textalytics = new SempubClient();
		
		// This is the text that's going to be proofread
		String text = "On Tuesday, author George R.R. Martin stated that his novels take " + 
				"place in an universe meant to be a completely alternate and separate world not linked " +
				"to our own in any way . It's easy to tell the difference when the plot concerns direwolves " +
				"or dragons, but the power games that take place in King's Landing could very well be taken " +
				"from any of the European royal families in Middle Ages, down to the abundance — and " +
				"associated taint — of children conceived out of marriage, as we see with Ned Stark's Bastard.";
		
		////////////// OPTION 1 ////////////////
		//Creates a Document object
		Document doc = new Document("1", text, Language.en);
		
		System.out.println("\n ******** Analyzing the document... ********\n");
		
		// Analayzes with a document
		Result result = textalytics.check(doc);
		
		System.out.println(result);
		
		/*
		/////////////////// OPTION 2 ///////////////////
		System.out.println("Analyzing document... \n\n");
		
		// Analyzes with a text
		Result result = textalytics.check(text, Language.en);
		
		System.out.println(result); 
		*/
	}
}
