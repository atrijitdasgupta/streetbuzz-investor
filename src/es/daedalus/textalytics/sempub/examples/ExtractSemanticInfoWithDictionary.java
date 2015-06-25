/**
 * ExtractSemanticInfoWithDictionary.java
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
import es.daedalus.textalytics.sempub.domain.Concept;
import es.daedalus.textalytics.sempub.domain.Dictionary;
import es.daedalus.textalytics.sempub.domain.Document.Language;
import es.daedalus.textalytics.sempub.domain.Entity;

/**
 * This script runs an instance of SemPubClient in order to use Textalytics Semantic Publishing API 
 * services to tag semantic information in a text using a user-defined dictionary. Before the analysis, 
 * the script will create a dictionary and then it will add three entries: two entities and a concept. 
 * Once this has been done, the text will be tagged. There are two ways of tagging a text, either
 * by sending a json Document object (option 1) or just sending the text to analyze (option 2). Once this
 * has been done, the dictionary will be deleted.
 *
 * In order to run this example, the license key obtained for the Semantic Publishing API must be included
 * in the KEY variable  in 'src/main/resources/config.properties' file. If you don't know your key, check your personal area at
 * Textalytics (https://textalytics.com/personal_area)
 * 
 * @author     Textalytics
 * @version    1.0 -- 02/2014
 */
public class ExtractSemanticInfoWithDictionary {
	
	public static void main(String[] args) 
			throws ClientProtocolException, TextalyticsClientException, IOException {
		// Creates SempubClient object
		SempubClient textalytics = new SempubClient();
		
		System.out.println("\n ******** Creating the user-defined dictionary... ********\n");
		
		// Creates a Dictionary object
		Dictionary dict = new Dictionary("got", Language.en, "Entities and concepts from A Song of Ice and Fire");
		
		// Creates a user-defined dictionary
		textalytics.createDictionary(dict);
		
		System.out.println("User-defined dictionary created!");
		System.out.println(dict);
		
		// Adds entities to the dictionary
		System.out.println("\n ******** Creating the entity \"King's Landing\"... ********\n");
		
		Entity entity = new Entity("01", "King's Landing");
		entity.setType("Top>Location>GeoPoliticalEntity>City");
		entity.addTheme("Top>Society>Military");
		entity.addTheme("Top>Society>Politics");
		
		textalytics.createEntity(entity, dict);
		
		System.out.println("Entity created!");
		System.out.println(entity);
		
		System.out.println("\n ******** Creating the entity \"Jon Snow\"... ********\n");
		
		entity = new Entity("02", "Jon Snow");
		entity.addAlias("Ned Stark's bastard");
		entity.addAlias("Bastard of Winterfell");
		entity.addTheme("Top>Arts>Cinema");
		entity.addTheme("Top>Society>Politics");
		entity.setType("Top>Person>FullName");
		
		textalytics.createEntity(entity, dict);
		
		System.out.println("Entity created!");
		System.out.println(entity);
		
		// Adds concepts to the dictionary
		System.out.println("\n ******** Creating the concept \"direwolf\"... ********\n");
		
		Concept concept = new Concept("03", "direwolf");
		concept.addAlias("direwolves");
		concept.setType("Top>LivingThing>Animal>Vertebrate>Mammal");
		
		textalytics.createConcept(concept, dict);
		
		System.out.println("Concept created!");
		System.out.println(concept);
		
		////////////////////Analyzes the text ////////////////////
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
		 
		 textalytics.setDictionary(dict);
		 
		 // Analyzes with this document
		 Result result = textalytics.analyze(doc);
		
		 System.out.println(result); 
		*/
		
		/////////////////// OPTION 2 ///////////////////
		System.out.println("\n ******** Analyzing the text... ********\n");
		
		// Sets the user-defined dictionary
		textalytics.setDictionary(dict);
		
		// Analyzes with a text
		Result result = textalytics.analyze(text, Language.en);
		
		System.out.println(result);
		
		////////////////////Manage a user-defined dictionary ////////////////////
		// Deletes the user-defined dictionary
		System.out.println("\n ******** Deleting the user-defined dictionary... ********\n");
		
		textalytics.deleteDictionary(dict);
		
		System.out.println("User-defined dictionary deleted!");
	}	
}
