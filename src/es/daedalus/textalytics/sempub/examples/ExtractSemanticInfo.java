/**
 * ExtractSemanticInfo.java
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
import es.daedalus.textalytics.sempub.domain.Document.Language;

/**
 * This script analyzes a text with the Semantic tagging service. 
 * The semantic tagging service identifies relevant information in your content and helps you to annotate it.
 * This service analyzes the text with semantic annotations like: 
 *   - Entity extraction
 *   - Concept extraction
 *   - Document categorization
 *   - Linked Open Data
 *   - Other relevant data (money expressions, url, phones and e-mails)
 *
 * To analyze the text there are two options
 *   1. Analyze with a Document object
 *   2. Analyze directly with the text
 * In this example it used the option 2
 * 
 * In order to run this example, the license key obtained for the Semantic Publishing API must be included
 * in the KEY variable  in 'src/main/resources/config.properties' file. If you don't know your key, check your personal area at
 * Textalytics (https://textalytics.com/personal_area)
 *
 * @author     Textalytics
 * @version    1.0 -- 02/2014
 */
public class ExtractSemanticInfo {

	public static void main(String[] args) 
			throws TextalyticsClientException, IOException, ClientProtocolException {
		// Creates SempubClient object
		SempubClient textalytics = new SempubClient();
		
		// the text to analyze
		String text = "On Tuesday, author George R.R. Martin stated that his novels take " + 
				"place in an universe meant to be a completely alternate and separate world not linked " +
				"to our own in any way . It's easy to tell the difference when the plot concerns direwolves " +
				"or dragons, but the power games that take place in King's Landing could very well be taken " +
				"from any of the European royal families in Middle Ages, down to the abundance — and " +
				"associated taint — of children conceived out of marriage, as we see with Ned Stark's Bastard.";

		/*
		 /////////////////// OPTION 1 ///////////////////
		 // Creates a document object
		 Document doc = new Document("1", textSemtag, Language.en);
		 
		 // Analyzes with this document
		 Result result = textalytics.analyze(doc);
		
		 System.out.println(result); 
		*/
		
		/////////////////// OPTION 2 ///////////////////
		System.out.println("\n ******** Analyzing the text... ********\n");
		
		// Analyzes with a text
		Result result = textalytics.analyze(text, Language.en);
		
		System.out.println(result);
	}
}
