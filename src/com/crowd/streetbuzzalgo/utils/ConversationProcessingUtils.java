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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.CardExclusionDAO;
import com.crowd.streetbuzz.dao.implementation.CategoryMasterDAO;
import com.crowd.streetbuzz.dao.implementation.ConversationCardDAO;
import com.crowd.streetbuzz.dao.implementation.DistributionDAO;
import com.crowd.streetbuzz.dao.implementation.MediaFilesDAO;
import com.crowd.streetbuzz.dao.implementation.SentimentQueueDAO;
import com.crowd.streetbuzz.dao.implementation.UserCategoryMapDAO;
import com.crowd.streetbuzz.dao.implementation.UserDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDetailsDAO;
import com.crowd.streetbuzz.dao.implementation.WordCloudStoreDAO;
import com.crowd.streetbuzz.helper.SiteSearchHelper;
import com.crowd.streetbuzz.model.ConversationCard;
import com.crowd.streetbuzz.model.User;
import com.crowd.streetbuzz.model.Voices;
import com.crowd.streetbuzz.model.VoicesDetails;
import com.crowd.streetbuzz.processhelperutils.ImageSearch;
import com.crowd.streetbuzz.processhelperutils.NetworkSearch;
import com.crowd.streetbuzz.processhelperutils.ProcessHelperUtils;
import com.crowd.streetbuzz.processhelperutils.VideoSearch;
import com.crowd.streetbuzz.processhelperutils.WebSiteSearch;
import com.crowd.streetbuzzalgo.distribution.Distribution;
import com.crowd.streetbuzzalgo.googlesearch.vo.Result;
import com.crowd.streetbuzzalgo.imagecompression.ImageCompress;
import com.crowd.streetbuzzalgo.instagram.InstagramSearch;
import com.crowd.streetbuzzalgo.lingpipe.LingPipe;
import com.crowd.streetbuzzalgo.parser.EntityParser;
import com.crowd.streetbuzzalgo.parser.TweetWithSentiments;
import com.crowd.streetbuzzalgo.parser.vo.StanfordNerVO;
import com.crowd.streetbuzzalgo.topsy.Otter;
import com.crowd.streetbuzzalgo.tumblrsearch.TumblrSearch;
import com.crowd.streetbuzzalgo.vo.SearchDemoVO;
import com.crowd.streetbuzzalgo.webhose.WebHose;
import com.crowd.streetbuzzalgo.yandexsearch.YandexVO;

/**
 * @author Atrijit
 * 
 */
public class ConversationProcessingUtils implements Constants {
	private static final String POSITIVEPLUS = "positiveplus";

	private static final String POSITIVE = "pos";

	private static final String NEUTRAL = "neu";

	private static final String NEGATIVEPLUS = "neg";

	private static final String NEGATIVE = "negativeplus";

	/**
	 * 
	 */
	public ConversationProcessingUtils() {
		// TODO Auto-generated constructor stub
	}

	public static void processConversation(ConversationCard cc, String fbtoken,
			ConversationCardDAO conversationCardDAO, UserDAO userDAO,
			UserCategoryMapDAO userCategoryMapDAO,
			CategoryMasterDAO categoryMasterDAO, VoicesDAO voicesDAO,
			VoicesDetailsDAO voicesDetailsDAO, DistributionDAO distributionDAO,
			CardExclusionDAO cardExclusionDAO,
			SentimentQueueDAO sentimentQueueDAO, MediaFilesDAO mediaFilesDAO,
			WordCloudStoreDAO wordCloudStoreDAO) {
		String searchtopic = StrUtil.nonNull(cc.getTopic());
		String category = StrUtil.nonNull(cc.getInteresttag());
		String actiontype = StrUtil.nonNull(cc.getActiontype());
		Map searchresultMap = runSearch(searchtopic, cc, voicesDAO);

		LingPipe lingpipe = new LingPipe();
		List voiceslist = voicesDAO.getAllRecordsbyCardId(cc.getId());
		List checkList = new ArrayList();
		for (int i = 0; i < voiceslist.size(); i++) {
			Voices v = (Voices) voiceslist.get(i);
			String sourcelink = v.getSourcelink();
			checkList.add(sourcelink);
		}
		long start = System.currentTimeMillis();
		Map analysedMap = SiteSearchHelper.siteAnalyseCheck(null,
				searchresultMap, searchtopic, checkList, lingpipe);
		long webs = System.currentTimeMillis();
		System.out.println("****************WEB SENTIMENT ANALYSIS*****************" +(( webs - start)/1000)+"secs");

		List analysedWebHoseBlogList = SiteSearchHelper
				.siteAnalyseCheckWebHoseBlog(searchresultMap, searchtopic,
						checkList, lingpipe);
		
		long webhb = System.currentTimeMillis();
		System.out.println("****************WEB HOSE BLOGS SENTIMENT ANALYSIS*****************" +(( webhb - webs)/1000)+"secs");
		
		List analysedWebHoseDiscussionList = SiteSearchHelper
				.siteAnalyseCheckWebHoseDiscussions(searchresultMap,
						searchtopic, checkList, lingpipe);
		long webhd = System.currentTimeMillis();
		System.out.println("****************WEB HOSE DISC SENTIMENT ANALYSIS*****************" +(( webhd - webhb)/1000)+"secs");

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
		} 

		else {
			if (imagesList != null && imagesList.size() > 1) {
				// imageurl = (String) imagesList.get(1);
				for (int i = 0; i < imagesList.size(); i++) {
					String temp = (String) imagesList.get(i);
					boolean bool = ImageCompress.saveImage(temp, (BASESBSTORAGEPATH
									+ System.currentTimeMillis() + ".jpg"));
					//bool = StrUtil.imageExtnCheck(imageurl);
					if (bool) {
						imageurl = temp;
						break;
					}
				}
			}
			cc.setImageurl(imageurl);
		}

		TweetWithSentiments tws = new TweetWithSentiments();
		List sentimentalTwitterList = new ArrayList();
		try {
			sentimentalTwitterList = tws.analyseTweetSentiment(twitterList,
					null, lingpipe);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long twit = System.currentTimeMillis();
		System.out.println("****************TWIT SENTIMENT ANALYSIS*****************" +(( twit - webhd)/1000)+"secs");
		List sentimentalOtterList = new ArrayList();
		try {
			sentimentalOtterList = tws.analyseOtterSentiment(otterList, null,
					searchtopic, lingpipe);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("YOUTUBELIST SIZE IS " + YoutubeList.size());
		List sentimentalYoutubeList = new ArrayList();
		try {
			sentimentalYoutubeList = tws.analyseYoutubeSentiment(YoutubeList,
					null, lingpipe);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		long yt = System.currentTimeMillis();
		System.out.println("****************YOUTUBE SENTIMENT ANALYSIS*****************" +(( yt - twit)/1000)+"secs");
		/*System.out.println("SENTI YOUTUBELIST SIZE IS "
				+ sentimentalYoutubeList.size());*/

		List sentimentalTumblrList = new ArrayList();
		try {
			sentimentalTumblrList = tws.analyseTumblrSentiment(tumblrList,
					null, searchtopic, lingpipe);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		long tumb = System.currentTimeMillis();
		System.out.println("****************TUMBLR SENTIMENT ANALYSIS*****************" +(( tumb - yt)/1000)+"secs");

		List sentimentalInstagramList = new ArrayList();
		try {
			sentimentalInstagramList = tws.analyseInstagramSentiment(
					instagramList, null, searchtopic, lingpipe);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		long insta = System.currentTimeMillis();
		System.out.println("****************INSTAGRAM SENTIMENT ANALYSIS*****************" +(( insta - tumb)/1000)+"secs");
		if (ACTIONTYPENEEW.equalsIgnoreCase(actiontype)) {

			cc = SBProcessingUtils.processConversation(cc, userDAO, null,
					conversationCardDAO, voicesDAO, lingpipe);
		}

		if (ACTIONTYPENEEW.equalsIgnoreCase(actiontype)) {
			cc = ProcessingHelper.spotateLatestConversationCard(cc,
					sentimentalTwitterList, sentimentalOtterList, analysedMap,
					voicesDAO, voicesDetailsDAO, sentimentalYoutubeList,
					imagesList, sentimentalTumblrList,
					sentimentalInstagramList, analysedWebHoseBlogList,
					analysedWebHoseDiscussionList, wordCloudStoreDAO);
		}
		long db = System.currentTimeMillis();
		
		
		if (ACTIONTYPEREFRESH.equalsIgnoreCase(actiontype)) {
			/*
			 * cc = ProcessingHelper .spotateRefreshConversationCard(cc,
			 * sentimentalTwitterList, sentimentalOtterList, analysedMap,
			 * voicesDAO, voicesDetailsDAO, sentimentalYoutubeList, imagesList,
			 * sentimentalTumblrList,
			 * sentimentalInstagramList,analysedWebHoseBlogList,analysedWebHoseDiscussionList);
			 */
			cc = ProcessingHelper.spotateLatestConversationCard(cc,
					sentimentalTwitterList, sentimentalOtterList, analysedMap,
					voicesDAO, voicesDetailsDAO, sentimentalYoutubeList,
					imagesList, sentimentalTumblrList,
					sentimentalInstagramList, analysedWebHoseBlogList,
					analysedWebHoseDiscussionList, wordCloudStoreDAO);
		}

		cc.setAction(ACTIONNO);
		// Get all Voice related details
		cc = getVoiceSpecificDetails(cc, voicesDAO, voicesDetailsDAO);
		long voiceget = System.currentTimeMillis();
		System.out.println("****************GET ALL VOICE SPECIFIC DETAILS NEEDED FOR UPDATING CARD INFO*****************" +(( voiceget - db)/1000)+"secs");
		// Generate the Graphs and keep them ready
		long graphstart = System.currentTimeMillis();
		cc = generateGraph(cc,voicesDAO,voicesDetailsDAO,mediaFilesDAO,wordCloudStoreDAO);
		long graphend = System.currentTimeMillis();
		System.out.println("****************GRAPH GENERATION*****************" +(( graphend - graphstart)/1000)+"secs");

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
		long loc = System.currentTimeMillis();
		System.out.println("****************PULL LOCATION FROM GEOCODINGEVERSELOOKUP*****************" +(( loc - graphend)/1000)+"secs");
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
		//Here set the channel counts
		//System.out.println("Setting channel counts");
		List reviewlist = voicesDAO.getAllRecordsbyCardIdChannel(cc.getId(), REVIEWCH);
		List forumlist = voicesDAO.getAllRecordsbyCardIdChannel(cc.getId(), FORUMCH);
		List bloglist = voicesDAO.getAllRecordsbyCardIdChannel(cc.getId(), BLOGCH);
		List fblist = voicesDAO.getAllRecordsbyCardIdChannel(cc.getId(), FACEBOOKCH);
		List tweetlist = voicesDAO.getAllRecordsbyCardIdChannel(cc.getId(), TWEETCH);
		List articlelist = voicesDAO.getAllRecordsbyCardIdChannel(cc.getId(), ARTICLECH);
		List videolist = voicesDAO.getAllRecordsbyCardIdChannel(cc.getId(), VIDEOCH);
		List imagelist = voicesDAO.getAllRecordsbyCardIdChannel(cc.getId(), IMAGECH);
		
		int reviewcount = 0;
		if(reviewlist!=null){
			reviewcount = reviewlist.size();
		}
		int forumcount = 0;
		if(forumlist!=null){
			forumcount = forumlist.size();
		}
		int blogcount = 0;
		if(bloglist!=null){
			blogcount = bloglist.size();
		}
		int fbcount = 0;
		if(fblist!=null){
			fbcount = fblist.size();
		}
		int tweetcount = 0;
		if(tweetlist!=null){
			tweetcount = tweetlist.size();
		}
		int articlecount = 0;
		if(articlelist!=null){
			articlecount = articlelist.size();
		}
		int videocount = 0;
		if(videolist!=null){
			videocount = videolist.size();
		}
		int imagecount = 0;
		if(imagelist!=null){
			imagecount = imagelist.size();
		}
		cc.setReviewcount(new Long(reviewcount));
		cc.setForumcount(new Long(forumcount));
		cc.setBlogcount(new Long(blogcount));
		cc.setFacebookcount(new Long(fbcount));
		cc.setTweetcount(new Long(tweetcount));
		cc.setArticlecount(new Long(articlecount));
		cc.setVideocount(new Long(videocount));
		cc.setImagecount(new Long(imagecount));
		long chan = System.currentTimeMillis();
		System.out.println("****************SETTING CHANNELS*****************" +((chan  - loc)/1000)+"secs");
		
		conversationCardDAO.addOrUpdateRecord(cc);
		try {
			Distribution.distribute(cc, userDAO, userCategoryMapDAO,
					categoryMasterDAO, distributionDAO, cardExclusionDAO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("****************CONV CARD UPDATE & DISTRIBUTION (GCM MESSAGE SENT)*****************" +((end  - chan)/1000)+"secs");
		
		System.out
				.println("********************************************************");
		System.out.println("DONE PROCESSING CONVERSATION CARD");
		System.out
				.println("********************************************************");

	}

	private static ConversationCard generateGraph(ConversationCard cc,
			VoicesDAO voicesDAO, VoicesDetailsDAO voicesDetailsDAO,
			MediaFilesDAO mediaFilesDAO, WordCloudStoreDAO wordCloudStoreDAO) {
		cc = GraphDataUtils.getCardData(voicesDAO, voicesDetailsDAO,
				cc.getId(), mediaFilesDAO, wordCloudStoreDAO, cc);
		return cc;
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

	private static Map runSearch(String searchtopic, ConversationCard cc,
			VoicesDAO voicesDAO) {
		// First clean the String
		searchtopic = ProcessHelperUtils.clean(searchtopic);

		// The Stanford entity parse call to go here
		// StanfordNerVO snvo = getStanfordNamedEntities(searchtopic);

		Map searchTermsMap = getSearchTerms(searchtopic);
		Map searchresultMap = doSearch(searchTermsMap, searchtopic, cc,
				voicesDAO);
		return searchresultMap;
	}

	private static Map doSearch(Map searchTermsMap, String searchtopic,
			ConversationCard cc, VoicesDAO voicesDAO) {
		List noloclist = (List) searchTermsMap.get("nolocation");
		List nolocbloglist = (List) searchTermsMap.get("nolocationblog");
		long start = System.currentTimeMillis();
		// Twitter Search
		//List twitvlist = voicesDAO.getAllRecordsbyCardIdChannel(cc.getId(),TWITTERSPP);
		List twitvlist = new ArrayList();
		List twitterList = new ArrayList();
		/*List twitterList = NetworkSearch.searchTwitterCheck(noloclist,
				new ArrayList());*/
		long twit = System.currentTimeMillis();
		
		//System.out.println("****************TWITTER SEARCH*****************" +((twit - start)/1000)+"secs");
		// Video Search
		// Youtube Search
		/*List ytvlist = voicesDAO.getAllRecordsbyCardIdChannel(cc.getId(),
				YOUTUBESPP);*/
		// List YoutubeList = VideoSearch.searchYoutubeCheck(noloclist,ytvlist);
		List YoutubeList = VideoSearch.searchYoutube(noloclist);
		long yt = System.currentTimeMillis();
		System.out.println("****************YOUTUBE SEARCH*****************" +((yt - twit)/1000)+"secs");

		// Google Searches
		// List googleResultList = WebSiteSearch.googleSearch(noloclist);
		List googleResultList = new ArrayList();

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
		

		// Mash these up
		List megaList = new ArrayList();
		List fullSearchList = new ArrayList();

		// Google mash
		for (int i = 0; i < googleResultList.size(); i++) {
			Result result = (Result) googleResultList.get(i);
			String title = result.getTitle();
			String url = result.getUrl();
			if (url.indexOf("tumblr") > 0) {
				continue;
			}
			if (url.indexOf("instagram") > 0) {
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

		// Yandex mash
		for (int i = 0; i < yandexResultList.size(); i++) {
			YandexVO yndxvo = (YandexVO) yandexResultList.get(i);
			String title = yndxvo.getTitle();
			String url = yndxvo.getUrl();
			if (url.indexOf("tumblr") > 0) {
				continue;
			}
			if (url.indexOf("instagram") > 0) {
				continue;
			}
			if (url.indexOf(".ru") > 0) {
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
				continue;
			}
			if (url.indexOf("instagram") > 0) {
				continue;
			}
			if (url.indexOf(".ru") > 0) {
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
		
		long websearch = System.currentTimeMillis();
		System.out.println("****************WEB SEARCH*****************" +((websearch - yt)/1000)+"secs");

		// Photo Search
		String photosearchterm = (String) noloclist.get(0);
		List imagesList = ImageSearch.googleImageSearch(photosearchterm);
		Map searchMap = new HashMap();
		searchMap.put("imagesList", imagesList);
		searchMap.put("fullSearchList", fullSearchList);
		searchMap.put("twitterList", twitterList);
		searchMap.put("YoutubeList", YoutubeList);
		
		long ph = System.currentTimeMillis();
		
		System.out.println("****************PHOTO SEARCH*****************" +(( ph- websearch)/1000)+"secs");
		// Otter Search

		List keyOtterlist = new ArrayList();
		keyOtterlist.add(searchtopic);
		//List otterList = Otter.callCheck(keyOtterlist, twitvlist);
		 List otterList = Otter.call(keyOtterlist);
		List ottertruncatedList = new ArrayList();
		if (otterList.size() > 50) {
			ottertruncatedList = otterList.subList(0, 49);
		} else {
			ottertruncatedList = otterList;
		}
		searchMap.put("otterList", ottertruncatedList);
		//searchMap.put("otterList", new ArrayList());
		long twitt = System.currentTimeMillis();
		System.out.println("****************TWITTER SEARCH*****************" +(( twitt- ph)/1000)+"secs");
		// Tumblr Search
		/*List tumbvlist = voicesDAO.getAllRecordsbyCardIdChannel(cc.getId(),
				TUMBLRSPP);*/
		List tumblrList = TumblrSearch.search(keyOtterlist);
		System.out.println("tumblrList size::" + tumblrList.size());
		searchMap.put("tumblrList", tumblrList);
		long tumb = System.currentTimeMillis();
		System.out.println("****************TUMBLR SEARCH*****************" +(( tumb- twitt)/1000)+"secs");

		// Instagram Search
		/*List instavlist = voicesDAO.getAllRecordsbyCardIdChannel(cc.getId(),
				INSTAGRAMSPP);*/
		List instagramList = InstagramSearch.search(keyOtterlist);
		System.out.println("instagramList size::" + instagramList.size());
		searchMap.put("instagramList", instagramList);
		long inst = System.currentTimeMillis();
		System.out.println("****************INSTAGRAM SEARCH*****************" +(( inst- tumb)/1000)+"secs");

		// Webhose blog search
		List webhoseblogList = WebHose.search(searchtopic, "blog");
		// Webhose Discussions Search
		List webhosediscussionList = WebHose.search(searchtopic, "discussions");
		long webh = System.currentTimeMillis();
		System.out.println("****************	WEBHOSE SEARCH (BOTH)*****************" +(( webh- inst)/1000)+"secs");
		searchMap.put("webhoseblogList", webhoseblogList);
		searchMap.put("webhosediscussionList", webhosediscussionList);

		return searchMap;
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

	public static Map getSearchTerms(String entry) {
		List allsearchterms = new ArrayList();
		allsearchterms.add(entry);
		Map searchMap = new HashMap();
		List noloclist = new ArrayList();
		List nolocbloglist = new ArrayList();
		for (int i = 0; i < allsearchterms.size(); i++) {
			String temp = (String) allsearchterms.get(i);
			noloclist.add(temp);
			String smalltemp = temp.toLowerCase();
			if (smalltemp.indexOf("blog") < 0) {
				nolocbloglist.add(temp + " blog");
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
		}
		searchMap.put("nolocation", noloclist);
		searchMap.put("nolocationblog", nolocbloglist);
		return searchMap;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
