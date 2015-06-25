/**
 * 
 */
package com.crowd.streetbuzzalgo.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.CardExclusionDAO;
import com.crowd.streetbuzz.dao.implementation.CategoryMasterDAO;
import com.crowd.streetbuzz.dao.implementation.ConversationCardDAO;
import com.crowd.streetbuzz.dao.implementation.DistributionDAO;
import com.crowd.streetbuzz.dao.implementation.SentimentQueueDAO;
import com.crowd.streetbuzz.dao.implementation.UserCategoryMapDAO;
import com.crowd.streetbuzz.dao.implementation.UserDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDetailsDAO;
import com.crowd.streetbuzz.helper.SiteSearchHelper;
import com.crowd.streetbuzz.model.ConversationCard;
import com.crowd.streetbuzz.model.User;
import com.crowd.streetbuzz.model.Voices;
import com.crowd.streetbuzz.model.VoicesDetails;
import com.crowd.streetbuzz.processhelperutils.DemoWordProcess;
import com.crowd.streetbuzz.processhelperutils.ImageSearch;
import com.crowd.streetbuzz.processhelperutils.NetworkSearch;
import com.crowd.streetbuzz.processhelperutils.ProcessHelperUtils;
import com.crowd.streetbuzz.processhelperutils.VideoSearch;
import com.crowd.streetbuzz.processhelperutils.WebSiteSearch;
import com.crowd.streetbuzzalgo.distribution.Distribution;
import com.crowd.streetbuzzalgo.faroosearch.vo.FarooResult;
import com.crowd.streetbuzzalgo.faroosearch.vo.FarooResultSet;
import com.crowd.streetbuzzalgo.googlesearch.vo.Result;
import com.crowd.streetbuzzalgo.instagram.InstagramSearch;
import com.crowd.streetbuzzalgo.lingpipe.LingPipe;
import com.crowd.streetbuzzalgo.parser.EntityParser;
import com.crowd.streetbuzzalgo.parser.TweetWithSentiments;
import com.crowd.streetbuzzalgo.parser.vo.StanfordNerVO;
import com.crowd.streetbuzzalgo.topsy.Otter;
import com.crowd.streetbuzzalgo.tumblrsearch.TumblrSearch;
import com.crowd.streetbuzzalgo.vo.SearchDemoVO;
import com.crowd.streetbuzzalgo.vo.ThreadObject;
import com.crowd.streetbuzzalgo.vo.ThreadObjectReturn;
import com.crowd.streetbuzzalgo.yandexsearch.YandexVO;

/**
 * @author Atrijit
 * 
 */
public class ConversationProcessingUtils_Old implements Constants {
	private static final String POSITIVEPLUS = "positiveplus";

	private static final String POSITIVE = "positive";

	private static final String NEUTRAL = "neutral";

	private static final String NEGATIVEPLUS = "negative";

	private static final String NEGATIVE = "negativeplus";
	
	private static final int MYTHREADS = 6;

	/**
	 * 
	 */
	public ConversationProcessingUtils_Old() {
		// TODO Auto-generated constructor stub
	}
	
	/*public static void processConversationCONCURRENT(ConversationCard cc,
			String fbtoken, ConversationCardDAO conversationCardDAO,
			UserDAO userDAO, UserCategoryMapDAO userCategoryMapDAO,
			CategoryMasterDAO categoryMasterDAO, VoicesDAO voicesDAO,
			VoicesDetailsDAO voicesDetailsDAO, DistributionDAO distributionDAO,
			CardExclusionDAO cardExclusionDAO,SentimentQueueDAO sentimentQueueDAO) {
		long start = System.currentTimeMillis();
		String searchtopic = StrUtil.nonNull(cc.getTopic());
		String category = StrUtil.nonNull(cc.getInteresttag());
		String actiontype = StrUtil.nonNull(cc.getActiontype());
		Map searchresultMap = runSearch(searchtopic,cc,voicesDAO);
		
		List imagesList = (List) searchresultMap.get("imagesList");
		List twitterList = (List) searchresultMap.get("twitterList");
		List YoutubeList = (List) searchresultMap.get("YoutubeList");
		List otterList = (List) searchresultMap.get("otterList");
		List tumblrList = (List) searchresultMap.get("tumblrList");
		List instagramList = (List) searchresultMap.get("instagramList");
		
//		 Set the image upfront
		String mediaid = StrUtil.nonNull(cc.getMediaid());
		String imageurl = "";
		if (!"".equalsIgnoreCase(mediaid)) {
			imageurl = uploadedimageurlprefix + "getimage.htm?mediaid="
					+ mediaid;
			cc.setImageurl(imageurl);
		} else {
			if (imagesList != null && imagesList.size() > 1) {
				imageurl = (String) imagesList.get(1);
			}
			cc.setImageurl(imageurl);
		}
		
		ExecutorService executor = Executors.newFixedThreadPool(MYTHREADS);
		//(null, searchresultMap,searchtopic);
		//(twitterList,null);
		//(otterList, null,searchtopic);
		//(YoutubeList,	null);
		//(tumblrList,null, searchtopic);
		//(instagramList, null, searchtopic);
		
		List list = new ArrayList();
		
		ThreadObject webobj = new ThreadObject();
		webobj.setType(WEBSPP);
		webobj.setMap(searchresultMap);
		list.add(webobj);
		
		SentimentQueue sqweb = new SentimentQueue();
		sqweb.setCardid(cc.getId());
		byte[] webobjbytes = null;
		try {
			webobjbytes = ByteUtils.serialize(webobj);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		sqweb.setInblob(webobjbytes);
		sqweb.setType(WEBSPP);
		sqweb.setStatus(new Long(0));
		sentimentQueueDAO.addOrUpdateRecord(sqweb);
		
		ThreadObject twitobj = new ThreadObject();
		twitobj.setType(TWITTERSPP);
		twitobj.setList(twitterList);
		list.add(twitobj);
		
		ThreadObject otterobj = new ThreadObject();
		otterobj.setType(OTTERSPP);
		otterobj.setList(otterList);
		list.add(otterobj);
		
		ThreadObject ytobj = new ThreadObject();
		ytobj.setType(YOUTUBESPP);
		ytobj.setList(YoutubeList);
		list.add(ytobj);
		
		
		ThreadObject tumblrobj = new ThreadObject();
		tumblrobj.setType(TUMBLRSPP);
		tumblrobj.setList(tumblrList);
		list.add(tumblrobj);
		
		ThreadObject instagramobj = new ThreadObject();
		instagramobj.setType(INSTAGRAMSPP);
		instagramobj.setList(instagramList);
		list.add(instagramobj);
		
		List retobjlist = new ArrayList();
		ThreadObjectReturn tor = new ThreadObjectReturn();
		for (int i=0;i<list.size();i++){
			ThreadObject processobj = (ThreadObject)list.get(i);
			Callable <ThreadObjectReturn> worker = new MyCallable(processobj,searchtopic); 
			Future <ThreadObjectReturn> future = executor.submit(worker);
			
			// Wait until all threads are finished
			while (!future.isDone()) {
				System.out.println("Task is not completed yet....");
	            try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			 System.out.println("Task is completed, let's check result");
			 try {
				 ThreadObjectReturn torReturn = future.get();
				System.out.println("Here   ...   "+torReturn.getClass().toString());
				System.out.println("Got "+torReturn);
			
				retobjlist.add(torReturn);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			
		}
		 executor.shutdown();
		
		Map analysedMap = new HashMap();
		List sentimentalTwitterList = new ArrayList();
		List sentimentalOtterList = new ArrayList();
		List sentimentalYoutubeList = new ArrayList();
		List sentimentalTumblrList = new ArrayList();
		List sentimentalInstagramList = new ArrayList();
		
		for(int i=0;i<retobjlist.size();i++){
			ThreadObjectReturn torReturn = (ThreadObjectReturn)retobjlist.get(i);
			String type = torReturn.getType();
			if(WEBSPP.equalsIgnoreCase(type)){
				analysedMap = torReturn.getMap();
			}
			if(TWITTERSPP.equalsIgnoreCase(type)){
				sentimentalTwitterList = torReturn.getList();
			}
			if(OTTERSPP.equalsIgnoreCase(type)){
				sentimentalOtterList = torReturn.getList();
			}
			if(YOUTUBESPP.equalsIgnoreCase(type)){
				sentimentalYoutubeList = torReturn.getList();
			}
			if(TUMBLRSPP.equalsIgnoreCase(type)){
				sentimentalTumblrList = torReturn.getList();
			}
			if(INSTAGRAMSPP.equalsIgnoreCase(type)){
				sentimentalInstagramList = torReturn.getList();
			}
		}
		
		if (ACTIONTYPENEEW.equalsIgnoreCase(actiontype)) {
			
			 * cc = SBProcessingUtils.processConversation(cc, userDAO, pipeline,
			 * conversationCardDAO, voicesDAO);
			 
			cc = SBProcessingUtils.processConversation(cc, userDAO, null,
					conversationCardDAO, voicesDAO,null);
		}
		if (ACTIONTYPENEEW.equalsIgnoreCase(actiontype)) {
			cc = ProcessingHelper
					.spotateLatestConversationCard(cc, sentimentalTwitterList,
							sentimentalOtterList, analysedMap, voicesDAO,
							voicesDetailsDAO, sentimentalYoutubeList,
							imagesList, sentimentalTumblrList,
							sentimentalInstagramList);
		}
		if (ACTIONTYPEREFRESH.equalsIgnoreCase(actiontype)) {
			cc = ProcessingHelper
					.spotateRefreshConversationCard(cc, sentimentalTwitterList,
							sentimentalOtterList, analysedMap, voicesDAO,
							voicesDetailsDAO, sentimentalYoutubeList,
							imagesList, sentimentalTumblrList,
							sentimentalInstagramList);
		}
		
//		 Notify the distribution list

		cc.setAction(ACTIONNO);
		// Get all Voice related details
		cc = getVoiceSpecificDetails(cc, voicesDAO, voicesDetailsDAO);
		// Set location
		String latitude = StrUtil.nonNull(cc.getLatitude());
		String longitude = StrUtil.nonNull(cc.getLongitude());
		if (!"".equalsIgnoreCase(latitude) && !"".equalsIgnoreCase(longitude)) {
			try {
				Map locationMap = GeoCodingReverseLookupUtils.reverseLookup(
						latitude, longitude);
				if (locationMap != null && locationMap.size() > 1) {
					String cardcity = (String) locationMap.get(CITY);
					String cardcountry = (String) locationMap.get(COUNTRY);
					cc.setCardcity(cardcity);
					cc.setCardcountry(cardcountry);

					User user = (User) userDAO.getObjectById(new Long(cc
							.getUserid()));
					user.setCity(cardcity);
					user.setCountry(cardcountry);
					user.setLatitude(latitude);
					user.setLongitude(longitude);
					userDAO.addOrUpdateRecord(user);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		cc.setCompletiondate(new Date());
		// At this point, we need to check if the interestid has changed in the
		// card in the DB
		Long sittingid = cc.getId();
		ConversationCard ccsitting = (ConversationCard) conversationCardDAO
				.getObjectById(sittingid);
		if (ccsitting != null) {
			Long sittinginterestid = ccsitting.getInterestid();
			long sittingsubinterestid = ccsitting.getSubinterestid();
			Long workinginterestid = cc.getInterestid();
			Long workingsubinterestid = cc.getSubinterestid();
			int sitint = sittinginterestid.intValue();
			int workint = workinginterestid.intValue();
			if (workint == 0 && sitint != 0) {
				cc.setInterestid(sittinginterestid);
				cc.setSubinterestid(sittingsubinterestid);
			}
		}

		conversationCardDAO.addOrUpdateRecord(cc);
		try {
			Distribution.distribute(cc, userDAO, userCategoryMapDAO,
					categoryMasterDAO, distributionDAO, cardExclusionDAO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out
				.println("********************************************************");
		System.out.println("DONE PROCESSING CONVERSATION CARD");
		System.out
				.println("********************************************************");
		long end = System.currentTimeMillis();
		long duration = end - start;
		System.out.println("TOOK IT CONCURRENT "+(duration/1000)+" SECONDS");
		
	}*/
	

	public static void processConversation(ConversationCard cc, String fbtoken,
			ConversationCardDAO conversationCardDAO, UserDAO userDAO,
			UserCategoryMapDAO userCategoryMapDAO,
			CategoryMasterDAO categoryMasterDAO, VoicesDAO voicesDAO,
			VoicesDetailsDAO voicesDetailsDAO, DistributionDAO distributionDAO,
			CardExclusionDAO cardExclusionDAO,SentimentQueueDAO sentimentQueueDAO) {
		long start = System.currentTimeMillis();
		String searchtopic = StrUtil.nonNull(cc.getTopic());
		String category = StrUtil.nonNull(cc.getInteresttag());
		String actiontype = StrUtil.nonNull(cc.getActiontype());
		Map searchresultMap = runSearch(searchtopic,cc,voicesDAO);
		long searchend = System.currentTimeMillis();

		// Sentiment analysis
		/*
		 * Properties props = new Properties(); props.setProperty("annotators",
		 * "tokenize, ssplit, parse, sentiment"); StanfordCoreNLP pipeline = new
		 * StanfordCoreNLP(props);
		 */
		LingPipe lingpipe = new LingPipe();
		List voiceslist = voicesDAO.getAllRecordsbyCardId(cc.getId());
		List checkList = new ArrayList();
		for (int i=0;i<voiceslist.size();i++){
			Voices v = (Voices)voiceslist.get(i);
			String sourcelink = v.getSourcelink();
			checkList.add(sourcelink);
		}
		Map analysedMap = SiteSearchHelper.siteAnalyseCheck(null, searchresultMap,
				searchtopic, checkList, lingpipe);
		long scrapesentimentend = System.currentTimeMillis();
		/*
		 * Map analysedMap = SiteSearchHelper.siteAnalyse(pipeline,
		 * searchresultMap, searchtopic);
		 */

		List imagesList = (List) searchresultMap.get("imagesList");
		List twitterList = (List) searchresultMap.get("twitterList");
		List YoutubeList = (List) searchresultMap.get("YoutubeList");
		List otterList = (List) searchresultMap.get("otterList");
		List tumblrList = (List) searchresultMap.get("tumblrList");
		List instagramList = (List) searchresultMap.get("instagramList");

		// Set the image upfront

		String mediaid = StrUtil.nonNull(cc.getMediaid());
		String imageurl = "";
		if (!"".equalsIgnoreCase(mediaid)) {
			imageurl = uploadedimageurlprefix + "getimage.htm?mediaid="
					+ mediaid;
			cc.setImageurl(imageurl);
		} else {
			if (imagesList != null && imagesList.size() > 1) {
				imageurl = (String) imagesList.get(1);
			}
			cc.setImageurl(imageurl);
		}
		/*String existingimageurl = StrUtil.nonNull(cc.getImageurl());
		if(!"".equalsIgnoreCase(existingimageurl)){
			String mediaid = StrUtil.nonNull(cc.getMediaid());
			String imageurl = "";
			if (!"".equalsIgnoreCase(mediaid)) {
				imageurl = uploadedimageurlprefix + "getimage.htm?mediaid="
						+ mediaid;
				cc.setImageurl(imageurl);
			} else {
				if (imagesList != null && imagesList.size() > 1) {
					imageurl = (String) imagesList.get(1);
				}
				
				cc.setImageurl(imageurl);
			}
		}*/
		

		TweetWithSentiments tws = new TweetWithSentiments();
		List sentimentalTwitterList = new ArrayList();
		try {
			sentimentalTwitterList = tws.analyseTweetSentiment(twitterList,
					null,lingpipe);
			/*
			 * sentimentalTwitterList = tws.analyseTweetSentiment(twitterList,
			 * pipeline);
			 */
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List sentimentalOtterList = new ArrayList();
		try {
			sentimentalOtterList = tws.analyseOtterSentiment(otterList, null,
					searchtopic,lingpipe);
			/*
			 * sentimentalOtterList = tws.analyseOtterSentiment(otterList,
			 * pipeline, searchtopic);
			 */
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List sentimentalYoutubeList = new ArrayList();
		try {
			sentimentalYoutubeList = tws.analyseYoutubeSentiment(YoutubeList,
					null, lingpipe);
			/*
			 * sentimentalYoutubeList = tws.analyseYoutubeSentiment(YoutubeList,
			 * pipeline);
			 */
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		List sentimentalTumblrList = new ArrayList();
		try {
			sentimentalTumblrList = tws.analyseTumblrSentiment(tumblrList,
					null, searchtopic, lingpipe);
			/*
			 * sentimentalTumblrList = tws.analyseTumblrSentiment(tumblrList,
			 * pipeline, searchtopic);
			 */
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// System.out.println("*********************************************");
		// System.out.println("sentimentalTumblrList size:
		// "+sentimentalTumblrList.size());

		List sentimentalInstagramList = new ArrayList();
		try {
			sentimentalInstagramList = tws.analyseInstagramSentiment(
					instagramList, null, searchtopic, lingpipe);
			/*
			 * sentimentalInstagramList = tws.analyseInstagramSentiment(
			 * instagramList, pipeline, searchtopic);
			 */
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		long othersentimentend = System.currentTimeMillis();
		// System.out.println("*********************************************");
		// System.out.println("sentimentalInstagramList size:
		// "+sentimentalInstagramList.size());

		/*
		 * cc = SBProcessingUtils.processConversation(cc, userDAO, null,
		 * conversationCardDAO, voicesDAO);
		 */
		if (ACTIONTYPENEEW.equalsIgnoreCase(actiontype)) {
			/*
			 * cc = SBProcessingUtils.processConversation(cc, userDAO, pipeline,
			 * conversationCardDAO, voicesDAO);
			 */
			cc = SBProcessingUtils.processConversation(cc, userDAO, null,
					conversationCardDAO, voicesDAO, lingpipe);
		}
		if (ACTIONTYPENEEW.equalsIgnoreCase(actiontype)) {
			cc = ProcessingHelper
					.spotateLatestConversationCard(cc, sentimentalTwitterList,
							sentimentalOtterList, analysedMap, voicesDAO,
							voicesDetailsDAO, sentimentalYoutubeList,
							imagesList, sentimentalTumblrList,
							sentimentalInstagramList, new ArrayList(), new ArrayList(),null);
		}
		if (ACTIONTYPEREFRESH.equalsIgnoreCase(actiontype)) {
			/*cc = ProcessingHelper
					.spotateRefreshConversationCard(cc, sentimentalTwitterList,
							sentimentalOtterList, analysedMap, voicesDAO,
							voicesDetailsDAO, sentimentalYoutubeList,
							imagesList, sentimentalTumblrList,
							sentimentalInstagramList);*/
			cc = ProcessingHelper
			.spotateLatestConversationCard(cc, sentimentalTwitterList,
					sentimentalOtterList, analysedMap, voicesDAO,
					voicesDetailsDAO, sentimentalYoutubeList,
					imagesList, sentimentalTumblrList,
					sentimentalInstagramList,new ArrayList(), new ArrayList(),null);
		}

		// Notify the distribution list

		cc.setAction(ACTIONNO);
		// Get all Voice related details
		cc = getVoiceSpecificDetails(cc, voicesDAO, voicesDetailsDAO);
		// Set location
		String latitude = StrUtil.nonNull(cc.getLatitude());
		String longitude = StrUtil.nonNull(cc.getLongitude());
		if (!"".equalsIgnoreCase(latitude) && !"".equalsIgnoreCase(longitude)) {
			try {
				Map locationMap = GeoCodingReverseLookupUtils.reverseLookup(
						latitude, longitude);
				if (locationMap != null && locationMap.size() > 1) {
					String cardcity = (String) locationMap.get(CITY);
					String cardcountry = (String) locationMap.get(COUNTRY);
					cc.setCardcity(cardcity);
					cc.setCardcountry(cardcountry);

					User user = (User) userDAO.getObjectById(new Long(cc
							.getUserid()));
					user.setCity(cardcity);
					user.setCountry(cardcountry);
					user.setLatitude(latitude);
					user.setLongitude(longitude);
					userDAO.addOrUpdateRecord(user);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		cc.setCompletiondate(new Date());
		// At this point, we need to check if the interestid has changed in the
		// card in the DB
		Long sittingid = cc.getId();
		ConversationCard ccsitting = (ConversationCard) conversationCardDAO
				.getObjectById(sittingid);
		if (ccsitting != null) {
			Long sittinginterestid = ccsitting.getInterestid();
			long sittingsubinterestid = ccsitting.getSubinterestid();
			Long workinginterestid = cc.getInterestid();
			Long workingsubinterestid = cc.getSubinterestid();
			int sitint = sittinginterestid.intValue();
			int workint = workinginterestid.intValue();
			if (workint == 0 && sitint != 0) {
				cc.setInterestid(sittinginterestid);
				cc.setSubinterestid(sittingsubinterestid);
			}
		}

		conversationCardDAO.addOrUpdateRecord(cc);
		try {
			Distribution.distribute(cc, userDAO, userCategoryMapDAO,
					categoryMasterDAO, distributionDAO, cardExclusionDAO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out
				.println("********************************************************");
		System.out.println("DONE PROCESSING CONVERSATION CARD");
		System.out
				.println("********************************************************");
		long end = System.currentTimeMillis();
		long duration = end - start;
		System.out.println("TOTAL LINEAR time "+(duration/1000)+" SECONDS");
		long searchtime = searchend - start;
		System.out.println("TOTAL SEARCH time "+(searchtime/1000)+" SECONDS");
		
		long websentiment = scrapesentimentend - searchend;
		System.out.println("TOTAL WEB SENTIMENT time "+(websentiment/1000)+" SECONDS");
		
		long othersentiment = othersentimentend - websentiment;
		System.out.println("TOTAL OTHER SENTIMENT time "+(othersentiment/1000)+" SECONDS");
		
		System.out.println("FINAL PROCESSING TIME "+((end - othersentimentend)/1000)+" SECONDS" );
		
		
	}

	private static ConversationCard getVoiceSpecificDetails(
			ConversationCard cc, VoicesDAO voicesDAO,
			VoicesDetailsDAO voicesDetailsDAO) {
		Long cardid = cc.getId();
		List voicesList = voicesDAO.getAllRecordsbyCardId(cardid);

		List voicesDetailsList = voicesDetailsDAO.getAllRecordsbyCardid(cardid);
		int voicescount = voicesList.size() + voicesDetailsList.size();
		Long voicesCountLong = new Long(voicescount);
		cc.setVoicescount(voicesCountLong);

		int pos = 0;
		int posplus = 0;
		int neu = 0;
		int neg = 0;
		int negplus = 0;

		Map channelMap = new HashMap();

		for (int i = 0; i < voicesList.size(); i++) {
			Voices temp = (Voices) voicesList.get(i);
			String sentimentrating = StrUtil.nonNull(temp.getSentimentrating());

			if (POSITIVEPLUS.equalsIgnoreCase(sentimentrating)) {
				posplus = posplus + 1;
			} else if (POSITIVE.equalsIgnoreCase(sentimentrating)) {
				pos = pos + 1;
			} else if (NEUTRAL.equalsIgnoreCase(sentimentrating)) {
				neu = neu + 1;
			} else if (NEGATIVEPLUS.equalsIgnoreCase(sentimentrating)) {
				negplus = negplus + 1;
			} else if (NEGATIVE.equalsIgnoreCase(sentimentrating)) {
				neg = neg + 1;
			}

			String channel = StrUtil.nonNull(temp.getChannel());
			if (channelMap.containsKey(channel)) {
				Long count = (Long) channelMap.get(channel);
				count = count + 1;
				channelMap.put(channel, count);
			} else {
				channelMap.put(channel, new Long(1));
			}

		}

		for (int i = 0; i < voicesDetailsList.size(); i++) {
			VoicesDetails temp = (VoicesDetails) voicesDetailsList.get(i);
			String sentimentrating = StrUtil.nonNull(temp.getSentimentrating());

			if (POSITIVEPLUS.equalsIgnoreCase(sentimentrating)) {
				posplus = posplus + 1;
			} else if (POSITIVE.equalsIgnoreCase(sentimentrating)) {
				pos = pos + 1;
			} else if (NEUTRAL.equalsIgnoreCase(sentimentrating)) {
				neu = neu + 1;
			} else if (NEGATIVEPLUS.equalsIgnoreCase(sentimentrating)) {
				negplus = negplus + 1;
			} else if (NEGATIVE.equalsIgnoreCase(sentimentrating)) {
				neg = neg + 1;
			}
		}
		double finalpos = 0;
		double finalneg = 0;

		finalpos = pos + 1.5 * posplus;
		finalneg = neg + 1.5 * negplus;
		Double finalposd = new Double(finalpos);
		Double finalnegd = new Double(finalneg);

		cc.setPositivevoices(new Long(finalposd.longValue()));
		cc.setNegativevoices(new Long(finalnegd.longValue()));
		cc.setNeutralvoices(new Long(neu));

		// Set channels as well
		Set keyset = channelMap.keySet();
		Iterator it = keyset.iterator();
		List<NameValuePair> channelList = new ArrayList<NameValuePair>();
		while (it.hasNext()) {
			String channel = StrUtil.nonNull((String) it.next());
			Long count = new Long(0);
			if (!"".equalsIgnoreCase(channel)) {
				count = (Long) channelMap.get(channel);
			}

			channelList.add(new BasicNameValuePair(channel, count.toString()));

		}
		cc.setChannel(channelList);

		return cc;
	}

	private static Map runSearch(String searchtopic,ConversationCard cc,VoicesDAO voicesDAO) {
		// First clean the String
		searchtopic = ProcessHelperUtils.clean(searchtopic);

		// The Stanford entity parse call to go here
		StanfordNerVO snvo = getStanfordNamedEntities(searchtopic);

		Map searchTermsMap = getSearchTerms(searchtopic, snvo);
		Map searchresultMap = doSearch(searchTermsMap, searchtopic,cc,voicesDAO);
		return searchresultMap;
	}

	public static StanfordNerVO getStanfordNamedEntities(String entry) {
		StanfordNerVO snvo = new StanfordNerVO();
		try {
			snvo = EntityParser.coreparse(entry);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return snvo;
	}

	public static Map getSearchTerms(String entry, StanfordNerVO snvo) {

		List location = snvo.getLocation();
		String locStr = "";
		if (location != null && location.size() > 0) {
			for (int i = 0; i < location.size(); i++) {
				locStr = locStr + " "
						+ StrUtil.nonNull((String) location.get(i));
				locStr = locStr.trim();
			}

		}
		List allsearchterms = DemoWordProcess.process(entry);
		// Simple Search String
		Map searchMap = new HashMap();

		/*
		 * searchMap.put("nolocation", new ArrayList());
		 * searchMap.put("nolocationblog", new ArrayList());
		 * searchMap.put("location", new ArrayList());
		 * searchMap.put("locationblog", new ArrayList());
		 */

		List noloclist = new ArrayList();
		List nolocbloglist = new ArrayList();
		List loclist = new ArrayList();
		List locbloglist = new ArrayList();

		for (int i = 0; i < allsearchterms.size(); i++) {
			String temp = (String) allsearchterms.get(i);
			noloclist.add(temp);

			String smalltemp = temp.toLowerCase();
			if (smalltemp.indexOf("blog") < 0) {
				nolocbloglist.add(temp + " blog");
			}
			if (smalltemp.indexOf("tumblr") < 0) {
				nolocbloglist.add(temp + " tumblr");
			}

			if (smalltemp.indexOf("wordpress") < 0) {
				nolocbloglist.add(temp + " wordpress");
			}

			if (smalltemp.indexOf("blogspot") < 0) {
				nolocbloglist.add(temp + " blogspot");
			}

			if (smalltemp.indexOf("blogger") < 0) {
				nolocbloglist.add(temp + " blogger");
			}

			if (smalltemp.indexOf("quora") < 0) {
				nolocbloglist.add(temp + " quora");
			}

			/*
			 * if (smalltemp.indexOf("reddit") < 0) { nolocbloglist.add(temp + "
			 * reddit"); }
			 */

			String temploc = locStr.toLowerCase();
			if (smalltemp.indexOf(temploc) < 0) {
				loclist.add(temp + " " + locStr);

				if (smalltemp.indexOf("blog") < 0) {
					locbloglist.add(temp + " " + locStr + " blog");
				}

				if (smalltemp.indexOf("tumblr") < 0) {
					locbloglist.add(temp + " " + locStr + " tumblr");
				}

				if (smalltemp.indexOf("wordpress") < 0) {
					locbloglist.add(temp + " " + locStr + " wordpress");
				}

				if (smalltemp.indexOf("blogspot") < 0) {
					locbloglist.add(temp + " " + locStr + " blogspot");
				}

				if (smalltemp.indexOf("blogger") < 0) {
					locbloglist.add(temp + " " + locStr + " blogger");
				}

				if (smalltemp.indexOf("quora") < 0) {
					locbloglist.add(temp + " " + locStr + " quora");
				}

				/*
				 * if (smalltemp.indexOf("reddit") < 0) { locbloglist.add(temp + " " +
				 * locStr + " reddit"); }
				 */
			}
		}
		searchMap.put("nolocation", noloclist);
		searchMap.put("nolocationblog", nolocbloglist);
		searchMap.put("location", loclist);
		searchMap.put("locationblog", locbloglist);

		return searchMap;
	}

	private static Map doSearch(Map searchTermsMap, String searchtopic,ConversationCard cc,VoicesDAO voicesDAO) {
		List noloclist = (List) searchTermsMap.get("nolocation");
		List nolocbloglist = (List) searchTermsMap.get("nolocationblog");
		List loclist = (List) searchTermsMap.get("location");
		List locbloglist = (List) searchTermsMap.get("locationblog");

		// Twitter Search
		List twitvlist = voicesDAO.getAllRecordsbyCardIdChannel(cc.getId(),
				TWITTERSPP);
		List twitterList = NetworkSearch.searchTwitterCheck(noloclist, loclist,twitvlist);
		// Video Search
		// Youtube Search
		List ytvlist = voicesDAO.getAllRecordsbyCardIdChannel(cc.getId(),YOUTUBESPP);
		List YoutubeList = VideoSearch.searchYoutubeCheck(noloclist,ytvlist);
		// Vimeo Search

		// Google Searches
		List googleResultList = WebSiteSearch.googleSearch(noloclist);
		List googleBlogResultList = WebSiteSearch.googleSearch(nolocbloglist);
		List googleLocationResultList = new ArrayList();
		if (loclist != null && loclist.size() > 0) {
			googleLocationResultList = WebSiteSearch.googleSearch(loclist);
		}
		List googleBlogLocationResultList = new ArrayList();
		if (locbloglist != null && locbloglist.size() > 0) {
			googleBlogLocationResultList = WebSiteSearch
					.googleSearch(locbloglist);
		}

		// Faroo Searches
		List farooResultList = new ArrayList();
		if (RUNFAROO) {
			farooResultList = WebSiteSearch.farooSearch(noloclist);
		}
		List farooBlogResultList = new ArrayList();
		if (RUNFAROO) {
			farooBlogResultList = WebSiteSearch.farooSearch(nolocbloglist);
		}

		List farooLocationResultList = new ArrayList();
		if (RUNFAROO) {
			if (loclist != null && loclist.size() > 0) {
				farooLocationResultList = WebSiteSearch.farooSearch(loclist);
			}
		}

		List farooBlogLocationResultList = new ArrayList();
		if (RUNFAROO) {
			if (locbloglist != null && locbloglist.size() > 0) {
				farooBlogLocationResultList = WebSiteSearch
						.farooSearch(locbloglist);
			}
		}

		// Yandex Searches
		List yandexResultList = new ArrayList();
		if (RUNYANDEX) {
			try {
				yandexResultList = WebSiteSearch.yandexSearch(noloclist);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		List yandexBlogResultList = new ArrayList();
		if (RUNYANDEX) {
			try {
				yandexBlogResultList = WebSiteSearch
						.yandexSearch(nolocbloglist);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		List yandexLocationResultList = new ArrayList();
		if (RUNYANDEX) {
			try {
				if (loclist != null && loclist.size() > 0) {
					yandexLocationResultList = WebSiteSearch
							.yandexSearch(loclist);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		List yandexBlogLocationResultList = new ArrayList();
		if (RUNYANDEX) {
			try {
				if (locbloglist != null && locbloglist.size() > 0) {
					yandexBlogLocationResultList = WebSiteSearch
							.yandexSearch(locbloglist);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Mash these up
		List megaList = new ArrayList();
		List fullSearchList = new ArrayList();
		// Add any Tumblr/Instagram links to these
		List instaTemplist = new ArrayList();
		List tumblrTempList = new ArrayList();
		// Google mash
		for (int i = 0; i < googleResultList.size(); i++) {
			Result result = (Result) googleResultList.get(i);
			String title = result.getTitle();
			String url = result.getUrl();
			if (url.indexOf("tumblr") > 0) {
				tumblrTempList.add(url);
				continue;
			}
			if (url.indexOf("instagram") > 0) {
				instaTemplist.add(url);
				continue;
			}
			if (!megaList.contains(url)) {
				SearchDemoVO sdvo = new SearchDemoVO();
				sdvo.setUrl(url);
				sdvo.setTitle(title);
				sdvo.setDate(new Date());
				sdvo.setSearchsource(GOOGLE);
				String host = StrUtil.getHost(url);
				sdvo.setChannel(host);
				megaList.add(url);
				fullSearchList.add(sdvo);

			}

		}
		for (int i = 0; i < googleBlogResultList.size(); i++) {
			Result result = (Result) googleBlogResultList.get(i);
			String title = result.getTitle();
			String url = result.getUrl();
			if (url.indexOf("tumblr") > 0) {
				tumblrTempList.add(url);
				continue;
			}
			if (url.indexOf("instagram") > 0) {
				instaTemplist.add(url);
				continue;
			}
			if (!megaList.contains(url)) {
				SearchDemoVO sdvo = new SearchDemoVO();
				sdvo.setUrl(url);
				sdvo.setTitle(title);
				sdvo.setDate(new Date());
				sdvo.setSearchsource(BLOG);
				String host = StrUtil.getHost(url);
				sdvo.setChannel(host);
				megaList.add(url);
				fullSearchList.add(sdvo);
			}

		}

		for (int i = 0; i < googleLocationResultList.size(); i++) {
			Result result = (Result) googleLocationResultList.get(i);
			String title = result.getTitle();
			String url = result.getUrl();
			if (url.indexOf("tumblr") > 0) {
				tumblrTempList.add(url);
				continue;
			}
			if (url.indexOf("instagram") > 0) {
				instaTemplist.add(url);
				continue;
			}
			if (!megaList.contains(url)) {
				SearchDemoVO sdvo = new SearchDemoVO();
				sdvo.setUrl(url);
				sdvo.setTitle(title);
				sdvo.setDate(new Date());
				sdvo.setSearchsource(GOOGLE);
				megaList.add(url);
				fullSearchList.add(sdvo);
			}

		}

		for (int i = 0; i < googleBlogLocationResultList.size(); i++) {
			Result result = (Result) googleBlogLocationResultList.get(i);
			String title = result.getTitle();
			String url = result.getUrl();
			if (url.indexOf("tumblr") > 0) {
				tumblrTempList.add(url);
				continue;
			}
			if (url.indexOf("instagram") > 0) {
				instaTemplist.add(url);
				continue;
			}
			if (!megaList.contains(url)) {
				SearchDemoVO sdvo = new SearchDemoVO();
				sdvo.setUrl(url);
				sdvo.setTitle(title);
				sdvo.setDate(new Date());
				sdvo.setSearchsource(BLOG);
				megaList.add(url);
				fullSearchList.add(sdvo);
			}

		}
		// Faroo mash
		if(farooResultList!=null&& farooResultList.size()>0){
			for (int i = 0; i < farooResultList.size(); i++) {
				FarooResultSet frs = (FarooResultSet) farooResultList.get(i);
				List results = frs.getResults();
				if (results != null) {
					for (int j = 0; j < results.size(); j++) {
						FarooResult fr = (FarooResult) results.get(j);
						String title = fr.getTitle();
						String url = fr.getUrl();
						if (url.indexOf("tumblr") > 0) {
							tumblrTempList.add(url);
							continue;
						}
						if (url.indexOf("instagram") > 0) {
							instaTemplist.add(url);
							continue;
						}
						String farooDtStr = fr.getDate();
						Date date = StrUtil.getFarooDate(farooDtStr);
						if (!megaList.contains(url)) {
							SearchDemoVO sdvo = new SearchDemoVO();
							sdvo.setUrl(url);
							sdvo.setTitle(title);
							sdvo.setDate(date);
							sdvo.setSearchsource(GOOGLE);
							megaList.add(url);
							fullSearchList.add(sdvo);

						}
					}
				}

			}
		}
		
		if(farooBlogResultList!=null&& farooBlogResultList.size()>0){
			for (int i = 0; i < farooBlogResultList.size(); i++) {
				FarooResultSet frs = (FarooResultSet) farooBlogResultList.get(i);
				List results = frs.getResults();
				if (results != null) {
					for (int j = 0; j < results.size(); j++) {
						FarooResult fr = (FarooResult) results.get(j);
						String title = fr.getTitle();
						String url = fr.getUrl();
						if (url.indexOf("tumblr") > 0) {
							tumblrTempList.add(url);
							continue;
						}
						if (url.indexOf("instagram") > 0) {
							instaTemplist.add(url);
							continue;
						}
						String farooDtStr = fr.getDate();
						Date date = StrUtil.getFarooDate(farooDtStr);
						if (!megaList.contains(url)) {
							SearchDemoVO sdvo = new SearchDemoVO();
							sdvo.setUrl(url);
							sdvo.setTitle(title);
							sdvo.setDate(date);
							sdvo.setSearchsource(BLOG);
							megaList.add(url);
							fullSearchList.add(sdvo);

						}
					}
				}

			}
		}
		
		if(farooLocationResultList!=null&& farooLocationResultList.size()>0){
			for (int i = 0; i < farooLocationResultList.size(); i++) {
				FarooResultSet frs = (FarooResultSet) farooLocationResultList
						.get(i);
				List results = frs.getResults();
				if (results != null) {
					for (int j = 0; j < results.size(); j++) {
						FarooResult fr = (FarooResult) results.get(j);
						String title = fr.getTitle();
						String url = fr.getUrl();
						if (url.indexOf("tumblr") > 0) {
							tumblrTempList.add(url);
							continue;
						}
						if (url.indexOf("instagram") > 0) {
							instaTemplist.add(url);
							continue;
						}
						String farooDtStr = fr.getDate();
						Date date = StrUtil.getFarooDate(farooDtStr);
						if (!megaList.contains(url)) {
							SearchDemoVO sdvo = new SearchDemoVO();
							sdvo.setUrl(url);
							sdvo.setTitle(title);
							sdvo.setDate(date);
							sdvo.setSearchsource(GOOGLE);
							megaList.add(url);
							fullSearchList.add(sdvo);

						}
					}
				}

			}
		}
	
		if(farooBlogLocationResultList!=null&& farooBlogLocationResultList.size()>0){
			for (int i = 0; i < farooBlogLocationResultList.size(); i++) {
				FarooResultSet frs = (FarooResultSet) farooBlogLocationResultList
						.get(i);
				List results = frs.getResults();
				if (results != null) {
					for (int j = 0; j < results.size(); j++) {
						FarooResult fr = (FarooResult) results.get(j);
						String title = fr.getTitle();
						String url = fr.getUrl();
						if (url.indexOf("tumblr") > 0) {
							tumblrTempList.add(url);
							continue;
						}
						if (url.indexOf("instagram") > 0) {
							instaTemplist.add(url);
							continue;
						}
						String farooDtStr = fr.getDate();
						Date date = StrUtil.getFarooDate(farooDtStr);
						if (!megaList.contains(url)) {
							SearchDemoVO sdvo = new SearchDemoVO();
							sdvo.setUrl(url);
							sdvo.setTitle(title);
							sdvo.setDate(date);
							sdvo.setSearchsource(BLOG);
							megaList.add(url);
							fullSearchList.add(sdvo);

						}
					}
				}

			}
		}
		
		// Yandex Search
		for (int i = 0; i < yandexResultList.size(); i++) {
			YandexVO yndxvo = (YandexVO) yandexResultList.get(i);
			String title = yndxvo.getTitle();
			String url = yndxvo.getUrl();
			if (url.indexOf("tumblr") > 0) {
				tumblrTempList.add(url);
				continue;
			}
			if (url.indexOf("instagram") > 0) {
				instaTemplist.add(url);
				continue;
			}
			Date date = yndxvo.getModdate();
			if (!megaList.contains(url)) {
				SearchDemoVO sdvo = new SearchDemoVO();
				sdvo.setUrl(url);
				sdvo.setTitle(title);
				sdvo.setDate(date);
				sdvo.setSearchsource(GOOGLE);
				megaList.add(url);
				fullSearchList.add(sdvo);

			}

		}

		for (int i = 0; i < yandexBlogResultList.size(); i++) {
			YandexVO yndxvo = (YandexVO) yandexBlogResultList.get(i);
			String title = yndxvo.getTitle();
			String url = yndxvo.getUrl();
			if (url.indexOf("tumblr") > 0) {
				tumblrTempList.add(url);
				continue;
			}
			if (url.indexOf("instagram") > 0) {
				instaTemplist.add(url);
				continue;
			}
			Date date = yndxvo.getModdate();
			if (!megaList.contains(url)) {
				SearchDemoVO sdvo = new SearchDemoVO();
				sdvo.setUrl(url);
				sdvo.setTitle(title);
				sdvo.setDate(date);
				sdvo.setSearchsource(BLOG);
				megaList.add(url);
				fullSearchList.add(sdvo);

			}

		}

		for (int i = 0; i < yandexLocationResultList.size(); i++) {
			YandexVO yndxvo = (YandexVO) yandexLocationResultList.get(i);
			String title = yndxvo.getTitle();
			String url = yndxvo.getUrl();
			if (url.indexOf("tumblr") > 0) {
				tumblrTempList.add(url);
				continue;
			}
			if (url.indexOf("instagram") > 0) {
				instaTemplist.add(url);
				continue;
			}
			Date date = yndxvo.getModdate();
			if (!megaList.contains(url)) {
				SearchDemoVO sdvo = new SearchDemoVO();
				sdvo.setUrl(url);
				sdvo.setTitle(title);
				sdvo.setDate(date);
				sdvo.setSearchsource(GOOGLE);
				megaList.add(url);
				fullSearchList.add(sdvo);

			}

		}

		for (int i = 0; i < yandexBlogLocationResultList.size(); i++) {
			YandexVO yndxvo = (YandexVO) yandexBlogLocationResultList.get(i);
			String title = yndxvo.getTitle();
			String url = yndxvo.getUrl();
			if (url.indexOf("tumblr") > 0) {
				tumblrTempList.add(url);
				continue;
			}
			if (url.indexOf("instagram") > 0) {
				instaTemplist.add(url);
				continue;
			}
			Date date = yndxvo.getModdate();
			if (!megaList.contains(url)) {
				SearchDemoVO sdvo = new SearchDemoVO();
				sdvo.setUrl(url);
				sdvo.setTitle(title);
				sdvo.setDate(date);
				sdvo.setSearchsource(BLOG);
				megaList.add(url);
				fullSearchList.add(sdvo);

			}

		}

		// Photo Search
		String photosearchterm = (String) noloclist.get(0);
		List imagesList = ImageSearch.googleImageSearch(photosearchterm);
		Map searchMap = new HashMap();
		searchMap.put("imagesList", imagesList);
		searchMap.put("fullSearchList", fullSearchList);
		for (int i = 0; i < fullSearchList.size(); i++) {
			SearchDemoVO sdvo = (SearchDemoVO) fullSearchList.get(i);
			String title = sdvo.getTitle();
			String url = sdvo.getUrl();
			String searchsource = sdvo.getSearchsource();

			System.out.println(title + "::" + url + "::" + searchsource);
		}
		searchMap.put("twitterList", twitterList);
		searchMap.put("YoutubeList", YoutubeList);

		// Otter Search

		List keyOtterlist = new ArrayList();
		keyOtterlist.add(searchtopic);
		List otterList = Otter.callCheck(keyOtterlist,twitvlist);
		List ottertruncatedList = new ArrayList();
		if (otterList.size() > 50) {
			ottertruncatedList = otterList.subList(0, 49);
		}
		// List otterList = new ArrayList();
		searchMap.put("otterList", ottertruncatedList);

		// Tumblr Search
		List tumbvlist = voicesDAO.getAllRecordsbyCardIdChannel(cc.getId(), TUMBLRSPP);
		List tumblrList = TumblrSearch.search(keyOtterlist);
		System.out.println("tumblrList size::" + tumblrList.size());
		searchMap.put("tumblrList", tumblrList);

		// Instagram Search
		List instavlist = voicesDAO.getAllRecordsbyCardIdChannel(cc.getId(), INSTAGRAMSPP);
		List instagramList = InstagramSearch.search(keyOtterlist);
		System.out.println("instagramList size::" + instagramList.size());
		searchMap.put("instagramList", instagramList);

		return searchMap;

	}

	private static void testTumblr(List list) {

		List tumblrList = TumblrSearch.search(list);
		System.out.println(tumblrList.size());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List list = new ArrayList();
		list.add("Red Dawn");
		testTumblr(list);

	}

}
