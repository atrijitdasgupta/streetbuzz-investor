/**
 * 
 */
package com.crowd.streetbuzzalgo.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.helper.SiteSearchHelper;
import com.crowd.streetbuzzalgo.parser.TweetWithSentiments;
import com.crowd.streetbuzzalgo.vo.ThreadObject;
import com.crowd.streetbuzzalgo.vo.ThreadObjectReturn;

/**
 * @author Atrijit
 * 
 */
public class MyCallable implements Callable, Constants {
	private ThreadObject tobj;

	private String searchtopic;

	MyCallable(ThreadObject tobj, String searchtopic) {
		this.tobj = tobj;
		this.searchtopic = searchtopic;
	}

	public ThreadObjectReturn call() {
		System.out.println("In here");
		String type = tobj.getType();

		if (WEBSPP.equalsIgnoreCase(type)) {
			System.out.println("In here " + WEBSPP);
			Map searchresultMap = tobj.getMap();
			Map analysedMap = SiteSearchHelper.siteAnalyse(null,
					searchresultMap, searchtopic);
			ThreadObjectReturn tor = new ThreadObjectReturn();
			tor.setType(WEBSPP);
			tor.setMap(analysedMap);
			return tor;

		} else if (TWITTERSPP.equalsIgnoreCase(type)) {
			System.out.println("In here " + TWITTERSPP);
			List twitterList = tobj.getList();
			TweetWithSentiments tws = new TweetWithSentiments();
			List sentimentalTwitterList = new ArrayList();
			try {
				sentimentalTwitterList = tws.analyseTweetSentiment(twitterList,
						null,null);
				ThreadObjectReturn tor = new ThreadObjectReturn();
				tor.setType(TWITTERSPP);
				tor.setList(sentimentalTwitterList);

				return tor;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (OTTERSPP.equalsIgnoreCase(type)) {
			System.out.println("In here " + OTTERSPP);
			List otterList = tobj.getList();

			TweetWithSentiments tws = new TweetWithSentiments();
			List sentimentalOtterList = new ArrayList();
			try {
				sentimentalOtterList = tws.analyseOtterSentiment(otterList,
						null, searchtopic,null);
				ThreadObjectReturn tor = new ThreadObjectReturn();
				tor.setType(OTTERSPP);
				tor.setList(sentimentalOtterList);

				return tor;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (YOUTUBESPP.equalsIgnoreCase(type)) {
			System.out.println("In here " + YOUTUBESPP);
			List YoutubeList = tobj.getList();
			TweetWithSentiments tws = new TweetWithSentiments();
			List sentimentalYoutubeList = new ArrayList();
			try {
				sentimentalYoutubeList = tws.analyseYoutubeSentiment(
						YoutubeList, null,null);
				ThreadObjectReturn tor = new ThreadObjectReturn();
				tor.setType(YOUTUBESPP);
				tor.setList(sentimentalYoutubeList);

				return tor;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (TUMBLRSPP.equalsIgnoreCase(type)) {
			System.out.println("In here " + TUMBLRSPP);
			List tumblrList = tobj.getList();

			TweetWithSentiments tws = new TweetWithSentiments();
			List sentimentalTumblrList = new ArrayList();
			try {
				sentimentalTumblrList = tws.analyseTumblrSentiment(tumblrList,
						null, searchtopic,null);
				ThreadObjectReturn tor = new ThreadObjectReturn();
				tor.setType(TUMBLRSPP);
				tor.setList(sentimentalTumblrList);

				return tor;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (INSTAGRAMSPP.equalsIgnoreCase(type)) {
			System.out.println("In here " + INSTAGRAMSPP);
			List instagramList = tobj.getList();

			TweetWithSentiments tws = new TweetWithSentiments();
			List sentimentalInstagramList = new ArrayList();
			try {
				sentimentalInstagramList = tws.analyseInstagramSentiment(
						instagramList, null, searchtopic,null);
				ThreadObjectReturn tor = new ThreadObjectReturn();
				tor.setType(TUMBLRSPP);
				tor.setList(sentimentalInstagramList);
				return tor;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return null;
	}
}
