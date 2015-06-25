/**
 * 
 */
package com.crowd.streetbuzzalgo.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.CategoryMasterDAO;
import com.crowd.streetbuzz.dao.implementation.ConversationCardDAO;
import com.crowd.streetbuzz.dao.implementation.SearchCardDAO;
import com.crowd.streetbuzz.dao.implementation.UserCategoryMapDAO;
import com.crowd.streetbuzz.dao.implementation.UserDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDAO;
import com.crowd.streetbuzz.model.CategoryMaster;
import com.crowd.streetbuzz.model.ConversationCard;
import com.crowd.streetbuzz.model.SearchCard;
import com.crowd.streetbuzz.model.User;
import com.crowd.streetbuzz.model.UserCategoryMap;
import com.crowd.streetbuzz.model.Voices;
import com.crowd.streetbuzzalgo.lingpipe.LingPipe;
import com.crowd.streetbuzzalgo.parser.TweetWithSentiments;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * @author Atrijit
 * 
 */
public class SBProcessingUtils implements Constants{

	/**
	 * 
	 */
	public SBProcessingUtils() {
		// TODO Auto-generated constructor stub
	}

	private static List getMatchedUserList(SearchCard sc,
			CategoryMasterDAO categoryMasterDAO, UserDAO userDAO,
			UserCategoryMapDAO userCategoryMapDAO) {
		/*String categoryname = sc.getInteresttag();
		categoryname = categoryname.trim();
		CategoryMaster categoryMaster = (CategoryMaster) categoryMasterDAO
				.getObjectByCategory(categoryname);
		Long categoryid = categoryMaster.getId();
		List catUserList = userCategoryMapDAO
				.getAllUsersforCategory(categoryid);
		List matchedUserList = new ArrayList();
		for (int i = 0; i < catUserList.size(); i++) {
			UserCategoryMap ucm = (UserCategoryMap) catUserList.get(i);
			Long userid = ucm.getUserid();
			User user = (User) userDAO.getObjectById(userid);
			if (!matchedUserList.contains(user)) {
				matchedUserList.add(user);
			}
		}*/
		
		List matchedUserList = userDAO.getAllRecords();
		
		
		return matchedUserList;
	}
	
	private static List getMatchedUserList(ConversationCard cc,
			CategoryMasterDAO categoryMasterDAO, UserDAO userDAO,
			UserCategoryMapDAO userCategoryMapDAO) {
		String categoryname = cc.getInteresttag();
		categoryname = categoryname.trim();
		CategoryMaster categoryMaster = (CategoryMaster) categoryMasterDAO
				.getObjectByCategory(categoryname);
		Long categoryid = categoryMaster.getId();
		List catUserList = userCategoryMapDAO
				.getAllUsersforCategory(categoryid);
		List matchedUserList = new ArrayList();
		for (int i = 0; i < catUserList.size(); i++) {
			UserCategoryMap ucm = (UserCategoryMap) catUserList.get(i);
			Long userid = ucm.getUserid();
			User user = (User) userDAO.getObjectById(userid);
			if (!matchedUserList.contains(user)) {
				matchedUserList.add(user);
			}
		}

		return matchedUserList;
	}

	private static List cleanSearchList(List searchList, List twitter,
			List noLocationList) {
		List cleanSearchList = new ArrayList();
		for (int i = 0; i < searchList.size(); i++) {
			SearchCard sc = (SearchCard) searchList.get(i);
			String topic = sc.getTopic();
			String interesttag = sc.getInteresttag();
			String moreinteresttag = sc.getMoreinteresttag();
			String temp = topic + " " + interesttag + " " + moreinteresttag;
			if (StrUtil.containsOrNot(temp, twitter)) {
				if (!cleanSearchList.contains(sc)) {
					cleanSearchList.add(sc);
				}
			}
			if (StrUtil.containsOrNot(temp, noLocationList)) {
				if (!cleanSearchList.contains(sc)) {
					cleanSearchList.add(sc);
				}
			}
		}
		return cleanSearchList;
	}

	private static List cleanConvList(List convList, List twitter,
			List noLocationList) {

		List cleanConvList = new ArrayList();
		for (int i = 0; i < convList.size(); i++) {
			SearchCard sc = (SearchCard) convList.get(i);
			String topic = sc.getTopic();
			String interesttag = sc.getInteresttag();
			String moreinteresttag = sc.getMoreinteresttag();
			String temp = topic + " " + interesttag + " " + moreinteresttag;
			if (StrUtil.containsOrNot(temp, twitter)) {
				if (!cleanConvList.contains(sc)) {
					cleanConvList.add(sc);
				}
			}
			if (StrUtil.containsOrNot(temp, noLocationList)) {
				if (!cleanConvList.contains(sc)) {
					cleanConvList.add(sc);
				}
			}
		}
		return cleanConvList;
	}

	public static SearchCard processSearchNoLoc(List twitter,
			List noLocationList, SearchCard sc, UserDAO userDAO,
			UserCategoryMapDAO userCategoryMapDAO, SearchCardDAO searchCardDAO,
			CategoryMasterDAO categoryMasterDAO, VoicesDAO voicesDAO) {
		// Get the users who would have posts that can match the entered
		// category
		List matchedUserList = getMatchedUserList(sc, categoryMasterDAO,
				userDAO, userCategoryMapDAO);
		// Check if other users have Search cards where the topic has any of the
		// words in the twitter list and nonlocationList list
		// Get the pipeline
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");

		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		for (int i = 0; i < matchedUserList.size(); i++) {
			User user = (User) matchedUserList.get(i);
			Long userid = user.getId();
			List arouserList = searchCardDAO.getAllRecordsOfUser(userid);
			List cleanSearchList = cleanSearchList(arouserList, twitter,
					noLocationList);
			for (int j = 0; j < cleanSearchList.size(); j++) {
				SearchCard cleansc = (SearchCard) cleanSearchList.get(j);

				// Create representative Voices and VoicesDetails
				Voices voices = new Voices();

				String author = cleansc.getAuthor();
				voices.setAuthor(author);

				String avatar = cleansc.getAvatar();
				voices.setAvatar(avatar);

				String cid = cleansc.getCardid();
				voices.setCardid(new Long(cid));

				voices.setCardtype(SEARCHTYPE);
				
				String carduniqueid = cleansc.getUniqueid();
				voices.setCarduniqueid(carduniqueid);

				voices.setComments(new ArrayList());

				voices.setCommentscount(new Long(0));
				voices.setExtcommentscount(new Long(0));
				voices.setExtlikescount(new Long(0));
				voices.setExtviewscount(new Long(0));
				voices.setFbgroupid("");
				voices.setLikescount(new Long(0));
				voices.setNegativephrase("");
				voices.setNeutralphrase("");
				voices.setPositivephrase("");
				voices.setPostauthorid("");
				voices.setPostid("");

				String posttext = cleansc.getTopic();
				voices.setPosttext(posttext);

				voices.setPosttextauthor("");

				try {
					voices = TweetWithSentiments.analyseSentimentVoices(voices,
							pipeline,null);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String sentimentStr = voices.getSentimentrating();
				sentimentStr = sentimentStr.toLowerCase();
				if(sentimentStr.indexOf("positive")>0){
					Long p = sc.getPositivevoices();
					if(p == null){
						p = new Long(0);
					}
					p = p+1;
					sc.setPositivevoices(p);
				}
				if(sentimentStr.indexOf("negative")>0){
					Long n = sc.getNegativevoices();
					if(n == null){
						n = new Long(0);
					}
					n = n+1;
					sc.setNegativevoices(n);
				}
				if(sentimentStr.indexOf("neutral")>0){
					Long ne = sc.getNeutralvoices();
					if(ne == null){
						ne = new Long(0);
					}
					ne = ne+1;
					sc.setNeutralvoices(ne);
				}

				voices.setSource(STREETBUZZ);
				voices.setSourcelink("");
				voices.setThumbsdowncount(new Long(0));
				voices.setThumbsupcount(new Long(0));
				String uniquevoiceid = StrUtil.getUniqueId();
				voices.setUniquevoiceid(uniquevoiceid);

				voices.setUserid(userid);
				Date carddate = cleansc.getCreationdate();
				voices.setVoicesdate(carddate);
				voices.setVoicetype(STREETBUZZ);
				voices.setChannel(STREETBUZZCHANNEL);

				voicesDAO.addOrUpdateRecord(voices);
				//Set voices count
				sc.setVoicescount(new Long(1));

			}

		}

		return sc;
	}
	
	public static ConversationCard processConversation(ConversationCard cc,
			UserDAO userDAO, StanfordCoreNLP pipeline,
			ConversationCardDAO conversationCardDAO, VoicesDAO voicesDAO, LingPipe lingpipe){
		//For NOW, get all users
		List matchedUserList = userDAO.getAllRecords();
		for (int i = 0; i < matchedUserList.size(); i++) {
			User user = (User) matchedUserList.get(i);
			Long userid = user.getId();
			List arouserList = conversationCardDAO.getAllRecordsOfUser(userid);
			for (int j = 0; j < arouserList.size(); j++) {
				ConversationCard cleancc = (ConversationCard) arouserList.get(j);
//				 Create representative Voices and VoicesDetails
				Voices voices = new Voices();

				String author = cleancc.getAuthor();
				voices.setAuthor(author);

				String avatar = cleancc.getAvatar();
				voices.setAvatar(avatar);
				
				String additionalviews = cleancc.getAdditionalviews();
				
				String cid = cleancc.getCardid();
				voices.setCardid(new Long(cid));

				voices.setCardtype(CONVERSATIONTYPE);

				String carduniqueid = cleancc.getUniqueid();
				voices.setCarduniqueid(carduniqueid);

				voices.setComments(new ArrayList());

				voices.setCommentscount(new Long(0));
				voices.setExtcommentscount(new Long(0));
				voices.setExtlikescount(new Long(0));
				voices.setExtviewscount(new Long(0));
				voices.setFbgroupid("");
				voices.setLikescount(new Long(0));
				voices.setNegativephrase("");
				voices.setNeutralphrase("");
				voices.setPositivephrase("");
				voices.setPostauthorid("");
				voices.setPostid("");

				String posttext = cleancc.getTopic();
				voices.setPosttext(posttext);

				voices.setPosttextauthor("");
				
				try {
					/*voices = TweetWithSentiments.analyseSentimentVoices(voices,
							pipeline);*/
					voices = TweetWithSentiments.analyseSentimentVoices(voices,
							null, lingpipe);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String sentimentStr = StrUtil.nonNull(voices.getSentimentrating());
				sentimentStr = sentimentStr.toLowerCase();
				if(sentimentStr.indexOf("positive")>0){
					Long p = cc.getPositivevoices();
					if(p == null){
						p = new Long(0);
					}
					p = p+1;
					cc.setPositivevoices(p);
				}
				if(sentimentStr.indexOf("negative")>0){
					Long n = cc.getNegativevoices();
					if(n == null){
						n = new Long(0);
					}
					n = n+1;
					cc.setNegativevoices(n);
				}
				if(sentimentStr.indexOf("neutral")>0){
					Long ne = cc.getNeutralvoices();
					if(ne == null){
						ne = new Long(0);
					}
					ne = ne+1;
					cc.setNeutralvoices(ne);
				}
				voices.setSource(STREETBUZZ);
				voices.setSourcelink("");
				voices.setThumbsdowncount(new Long(0));
				voices.setThumbsupcount(new Long(0));
				String uniquevoiceid = StrUtil.getUniqueId();
				voices.setUniquevoiceid(uniquevoiceid);

				voices.setUserid(userid);
				Date carddate = cleancc.getCreationdate();
				voices.setVoicesdate(carddate);
				voices.setVoicetype("SB");
				voices.setImageurl("");

				voicesDAO.addOrUpdateRecord(voices);
			}
		}
		return cc;
	}

	public static ConversationCard processConversationNoLoc(List twitter,
			List noLocationList, ConversationCard cc, UserDAO userDAO,
			UserCategoryMapDAO userCategoryMapDAO,
			ConversationCardDAO conversationCardDAO,
			CategoryMasterDAO categoryMasterDAO, VoicesDAO voicesDAO) {
//		 Get the users who would have posts that can match the entered
		// category
		List matchedUserList = getMatchedUserList(cc, categoryMasterDAO,
				userDAO, userCategoryMapDAO);
		// Check if other users have Search cards where the topic has any of the
		// words in the twitter list and nonlocationList list
		// Get the pipeline
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");

		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		for (int i = 0; i < matchedUserList.size(); i++) {
			User user = (User) matchedUserList.get(i);
			Long userid = user.getId();
			List arouserList = conversationCardDAO.getAllRecordsOfUser(userid);
			List cleanConvList = cleanConvList(arouserList, twitter,
					noLocationList);
			for (int j = 0; j < cleanConvList.size(); j++) {
				ConversationCard cleancc = (ConversationCard) cleanConvList.get(j);

				// Create representative Voices and VoicesDetails
				Voices voices = new Voices();

				String author = cleancc.getAuthor();
				voices.setAuthor(author);

				String avatar = cleancc.getAvatar();
				voices.setAvatar(avatar);

				String cid = cleancc.getCardid();
				voices.setCardid(new Long(cid));

				voices.setCardtype(CONVERSATIONTYPE);

				String carduniqueid = cleancc.getUniqueid();
				voices.setCarduniqueid(carduniqueid);

				voices.setComments(new ArrayList());

				voices.setCommentscount(new Long(0));
				voices.setExtcommentscount(new Long(0));
				voices.setExtlikescount(new Long(0));
				voices.setExtviewscount(new Long(0));
				voices.setFbgroupid("");
				voices.setLikescount(new Long(0));
				voices.setNegativephrase("");
				voices.setNeutralphrase("");
				voices.setPositivephrase("");
				voices.setPostauthorid("");
				voices.setPostid("");

				String posttext = cleancc.getTopic();
				voices.setPosttext(posttext);

				voices.setPosttextauthor("");

				try {
					voices = TweetWithSentiments.analyseSentimentVoices(voices,
							pipeline,null);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String sentimentStr = voices.getSentimentrating();
				sentimentStr = sentimentStr.toLowerCase();
				if(sentimentStr.indexOf("positive")>0){
					Long p = cc.getPositivevoices();
					if(p == null){
						p = new Long(0);
					}
					p = p+1;
					cc.setPositivevoices(p);
				}
				if(sentimentStr.indexOf("negative")>0){
					Long n = cc.getNegativevoices();
					if(n == null){
						n = new Long(0);
					}
					n = n+1;
					cc.setNegativevoices(n);
				}
				if(sentimentStr.indexOf("neutral")>0){
					Long ne = cc.getNeutralvoices();
					if(ne == null){
						ne = new Long(0);
					}
					ne = ne+1;
					cc.setNeutralvoices(ne);
				}

				voices.setSource(STREETBUZZ);
				voices.setSourcelink("");
				voices.setThumbsdowncount(new Long(0));
				voices.setThumbsupcount(new Long(0));
				String uniquevoiceid = StrUtil.getUniqueId();
				voices.setUniquevoiceid(uniquevoiceid);

				voices.setUserid(userid);
				Date carddate = cleancc.getCreationdate();
				voices.setVoicesdate(carddate);
				voices.setVoicetype("SB");

				voicesDAO.addOrUpdateRecord(voices);

			}

		}
		return cc;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
