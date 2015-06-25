/**
 * ExtractSemanticInfoWithModel.java
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
import es.daedalus.textalytics.sempub.domain.Category;
import es.daedalus.textalytics.sempub.domain.Document.Language;
import es.daedalus.textalytics.sempub.domain.Model;

/**
 * This script runs an instance of SemPubClient in order to use Textalytics Semantic Publishing API 
 * services to tag semantic information in a text using a user-defined model. Before the analysis, 
 * the script will create a model and then it will add two categories. 
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
public class ExtractSemanticInfoWithModel {

	public static void main(String[] args)
			throws ClientProtocolException, TextalyticsClientException, IOException {
		
		// Creates SempubClient object
		SempubClient textalytics = new SempubClient();
		
		////////////////////Manage a user-defined text classification model ////////////////////
		System.out.println("\n ******** Creating the user-defined text classification model... ********\n");
		
		// Creates a Model object
		Model model = new Model("got_geography_model", Language.en, "A Song of Ice and Fire geography classification model");
		
		// Creates a user-defined model
		textalytics.createModel(model);

		System.out.println("User-defined model created!");
		System.out.println(model);
		
		// Adds categories to the model
		System.out.println("\n ******** Creating the category \"Westeros - far west of the known world\"... ********\n");
		
		Category category = new Category("01", "Westeros - far west of the known world");
		category.setPositive("beyond+the+wall|the+iron+islands|the+vale+of+arryn|the+north|the+riverlands|the+westerlands|the+crownlands|the+reach|the+stormlands|the+wall|dorne|the+seven+kingdoms|free+folk|iron+throne|faith+of+the+seven|old+gods|drowned+god|king+landing|oldtown|lannisport|gulltown|white+harbor");
		category.setRelevant("house_baratheon~baelish~martell~greyjoy~arryn~stark~tyrell~lannister|harrenhall|strom+end|eyrie|casterly+rock|children+of+the+forest|giant|giants|others|white+walkers|white+walker|direwolf|direwolves|lizard-lion|lizard-lions|mammoth|mammoths|raven|ravens|shadowcat|shadowcats");
		category.setIrrelevant("beyond+the+narrow+sea|eastern+continent|the+east|free+cities|dothraki+sea|shivering+sea|valyrian+peninsula|slaver+bay|ghiscar|lhazar|qarth|eastern+essos|essos|dothraki|ghiscari|lhazareen|qartheen|ibbenese|jogos+nhai");
		category.setText("The continent of Westeros is long and relatively narrow, extending from Dorne in the south to the Lands of Always Winter in the far north, where a large amount of land remains uncharted, due to the extremely cold temperatures and hostile inhabitants known as wildlings. Although no scale appears on the maps in the books themselves, George R. R. Martin has stated that the Wall is a hundred leagues long, or three hundred miles. Thus the continent stretches for about 3,000 miles from north to south and for some 900 miles at its widest point east to west. Its eastern coast borders on the narrow sea; across those waters lies the eastern continent of Essos and the island chain known as the Stepstones. To the south is located the Summer Sea, and within it the Summer Islands. The northern lands of Westeros are less densely populated than the south despite their roughly equivalent size. The five major cities of Westeros are, in order of size: King's Landing, Oldtown, Lannisport, Gulltown, and White Harbor. Westeros was originally divided into several independent kingdoms before the consolidation of the War of Conquest. After this war and the eventual incorporation of Dorne, all the regions south of the Wall were united under the rule of House Targaryen into a nation that is known as the Seven Kingdoms.");
		
		textalytics.createCategory(category, model);
		
		System.out.println("User-defined category created!");
		System.out.println(category);
		
		System.out.println("\n ******** Creating the category \"Essos - beyond the narrow sea\"... ********\n");
		
		category = new Category("02", "Essos - beyond the narrow sea");
		category.setPositive("the+free+cities|the+dothraki+sea|the+shivering+sea|valyrian+peninsula|slaver+bay|ghiscar|lhazar|qarth|eastern+essos");
		category.setRelevant("dothraki|ghiscari|lhazareen|qartheen|ibbenese|jogos+nhai");
		
		textalytics.createCategory(category, model);
		
		System.out.println("User-defined category created!");
		System.out.println(category);
		
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
		 
		 // Analyzes with this document
		 Result result = textalytics.analyze(doc);
		
		 System.out.println(result); 
		*/
		
		/////////////////// OPTION 2 ///////////////////
		System.out.println("\n ******** Analyzing the text... ********\n");
		
		// Sets the user-defined model
		textalytics.setModels(model.getName());
		
		// Analyzes with a text
		Result result = textalytics.analyze(text, Language.en);
		
		System.out.println(result);
		
		////////////////////Manage a user-defined model ////////////////////
		// Deletes the user-defined model
		System.out.println("\n ******** Deleting the user-defined model... ********\n");
		
		textalytics.deleteModel(model);
		
		System.out.println("User-defined model deleted!");
	}
}
