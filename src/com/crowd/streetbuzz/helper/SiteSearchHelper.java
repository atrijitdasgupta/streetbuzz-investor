/**
 * 
 */
package com.crowd.streetbuzz.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzzalgo.constants.SystemConstants;
import com.crowd.streetbuzzalgo.crawler.CrawlerFactory;
import com.crowd.streetbuzzalgo.faroosearch.vo.FarooResult;
import com.crowd.streetbuzzalgo.faroosearch.vo.FarooResultSet;
import com.crowd.streetbuzzalgo.googlesearch.vo.Result;
import com.crowd.streetbuzzalgo.lingpipe.LingPipe;
import com.crowd.streetbuzzalgo.parser.TweetWithSentiments;
import com.crowd.streetbuzzalgo.scrapeandpost.ScrapePostFactory;
import com.crowd.streetbuzzalgo.utils.DataClean;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.crowd.streetbuzzalgo.vo.SearchDemoVO;
import com.crowd.streetbuzzalgo.webhose.WebHoseVO;
import com.crowd.streetbuzzalgo.yandexsearch.YandexVO;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * @author Atrijit
 * 
 */
public class SiteSearchHelper implements SystemConstants, Constants {

	/**
	 * 
	 */
	public SiteSearchHelper() {
		// TODO Auto-generated constructor stub
	}

	public static List siteAnalyseCheckWebHoseBlog(Map searchresultMap,
			String searchtopic, List checkList, LingPipe lingpipe) {

		List webhoseblogList = (List) searchresultMap.get("webhoseblogList");
		List webhoseblogsentiList = new ArrayList();

		for (int i = 0; i < webhoseblogList.size(); i++) {
			WebHoseVO whv = (WebHoseVO) webhoseblogList.get(i);
			String url = StrUtil.nonNull(whv.getUrl());
			if (checkList.contains(url)) {
				continue;
			}
			String text = StrUtil.nonNull(whv.getText());
			text = DataClean.htmlClean(text);
			String sentiment = lingpipe.classify(text);
			whv.setSentiment(sentiment);
			webhoseblogsentiList.add(whv);

		}

		return webhoseblogsentiList;
	}

	public static List siteAnalyseCheckWebHoseDiscussions(Map searchresultMap,
			String searchtopic, List checkList, LingPipe lingpipe) {

		List webhosediscussionList = (List) searchresultMap
				.get("webhosediscussionList");

		List webhosediscussionsentiList = new ArrayList();
		for (int i = 0; i < webhosediscussionList.size(); i++) {
			WebHoseVO whv = (WebHoseVO) webhosediscussionList.get(i);
			String url = StrUtil.nonNull(whv.getUrl());
			if (checkList.contains(url)) {
				continue;
			}
			String text = StrUtil.nonNull(whv.getText());
			text = DataClean.htmlClean(text);
			String sentiment = lingpipe.classify(text);
			whv.setSentiment(sentiment);
			webhosediscussionsentiList.add(whv);

		}
		return webhosediscussionsentiList;

	}

	public static Map siteAnalyseCheck(StanfordCoreNLP pipeline,
			Map searchresultMap, String searchtopic, List checkList,
			LingPipe lingpipe) {
		List fullSearchList = (List) searchresultMap.get("fullSearchList");

		Map fullSearchMap = new HashMap();

	//	TweetWithSentiments tws = new TweetWithSentiments();

		for (int i = 0; i < fullSearchList.size(); i++) {
			SearchDemoVO sdvo = (SearchDemoVO) fullSearchList.get(i);
			String url = StrUtil.nonNull(sdvo.getUrl());
			if (checkList.contains(url)) {
				continue;
			}
			// TODO currently working with a makeshift version of the crawl
			// method
			// List cList = CrawlerFactory.crawl(url,new Date());
			List cList = new ArrayList();

			/*try {
				cList = ScrapePostFactory.scrape(url);
				if (cList != null && cList.size() > 0) {
					cList = tws.analyseCrawlSentiment(cList, null, lingpipe);
					 cList = tws.analyseCrawlSentiment(cList, pipeline); 
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}*/
			boolean isblog = false;

			if (url.indexOf(BLOGSPOTC) > 0) {
				isblog = true;
			}
			if (url.indexOf(WORDPRESSC) > 0) {
				isblog = true;
			}
			if (url.indexOf(BLOGGERC) > 0) {
				isblog = true;
			}
			/*if (url.indexOf(TUMBLRC) > 0) {
				isblog = true;
			}*/
			if (isblog) {
				
				fullSearchMap.put(sdvo, cList);
			} /*else {
				if (cList != null && cList.size() > 0) {
					if (!showwithoutcomments) {
						fullSearchMap.put(sdvo, cList);
					}

				}
			}*/

		}

		return fullSearchMap;
	}

	public static Map siteAnalyse(StanfordCoreNLP pipeline,
			Map searchresultMap, String searchtopic) {
		List fullSearchList = (List) searchresultMap.get("fullSearchList");

		Map fullSearchMap = new HashMap();

		TweetWithSentiments tws = new TweetWithSentiments();

		for (int i = 0; i < fullSearchList.size(); i++) {
			SearchDemoVO sdvo = (SearchDemoVO) fullSearchList.get(i);
			String url = StrUtil.nonNull(sdvo.getUrl());
			// TODO currently working with a makeshift version of the crawl
			// method
			// List cList = CrawlerFactory.crawl(url,new Date());
			List cList = new ArrayList();

			try {
				cList = ScrapePostFactory.scrape(url);
				if (cList != null && cList.size() > 0) {
					cList = tws.analyseCrawlSentiment(cList, null, null);
					/* cList = tws.analyseCrawlSentiment(cList, pipeline); */
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			boolean isblog = false;

			if (url.indexOf(BLOGSPOTC) > 0) {
				isblog = true;
			}
			if (url.indexOf(WORDPRESSC) > 0) {
				isblog = true;
			}
			if (url.indexOf(BLOGGERC) > 0) {
				isblog = true;
			}
			if (url.indexOf(TUMBLRC) > 0) {
				isblog = true;
			}
			if (isblog) {
				if (cList == null || cList.size() > 0) {
					cList = new ArrayList();
				}
				fullSearchMap.put(sdvo, cList);
			} else {
				if (cList != null && cList.size() > 0) {
					if (!showwithoutcomments) {
						fullSearchMap.put(sdvo, cList);
					}

				}
			}

		}

		return fullSearchMap;
	}

	public static Map siteSearch(StanfordCoreNLP pipeline,
			List googleResultList, List googleLocationResultList,
			List farooSearchResultList, List farooLocationSearchResultList,
			List yandexSearchResultList, List yandexLocationSearchResultList) {
		Map gmap = new HashMap();
		Map fmap = new HashMap();
		Map ymap = new HashMap();
		List googleList = new ArrayList();
		for (int i = 0; i < googleResultList.size(); i++) {
			Result result = (Result) googleResultList.get(i);
			if (!googleList.contains(result)) {
				googleList.add(result);
			}
		}
		/*
		 * for (int i=0;i<googleLocationResultList.size();i++){ Result result =
		 * (Result) googleLocationResultList.get(i);
		 * if(!googleList.contains(result)){ googleList.add(result); } }
		 */

		for (int i = 0; i < googleList.size(); i++) {
			Result result = (Result) googleList.get(i);
			String title = StrUtil.nonNull(result.getTitle());
			String url = StrUtil.nonNull(result.getUrl());

			// TODO currently working with a makeshift version of the crawl
			// method
			List cList = CrawlerFactory.crawl(url, new Date());
			TweetWithSentiments tws = new TweetWithSentiments();
			try {
				cList = tws.analyseCrawlSentiment(cList, pipeline, null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			gmap.put(result, cList);

		}

		List farooList = new ArrayList();

		for (int i = 0; i < farooSearchResultList.size(); i++) {
			FarooResultSet resultsSet = (FarooResultSet) farooSearchResultList
					.get(i);
			if (!farooList.contains(resultsSet)) {
				farooList.add(resultsSet);
			}
		}
		/*
		 * for (int i=0;i<farooLocationSearchResultList.size();i++){
		 * FarooResultSet resultsSet = (FarooResultSet)
		 * farooLocationSearchResultList.get(i);
		 * if(!farooList.contains(resultsSet)){ farooList.add(resultsSet); } }
		 */

		for (int i = 0; i < farooList.size(); i++) {
			FarooResultSet resultsSet = (FarooResultSet) farooList.get(i);
			List farooResultList = resultsSet.getResults();
			for (int j = 0; j < farooResultList.size(); j++) {
				FarooResult fr = (FarooResult) farooResultList.get(j);
				String url = fr.getUrl();
				String datestr = fr.getDate();
				Date dt = StrUtil.getFarooDate(datestr);
				List cList = CrawlerFactory.crawl(url, dt);
				TweetWithSentiments tws = new TweetWithSentiments();
				try {
					cList = tws.analyseCrawlSentiment(cList, pipeline, null);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				fmap.put(fr, cList);
			}
		}

		List yandexList = new ArrayList();
		for (int i = 0; i < yandexSearchResultList.size(); i++) {
			YandexVO yndxvo = (YandexVO) yandexSearchResultList.get(i);
			if (yandexList.contains(yndxvo)) {
				yandexList.add(yndxvo);
			}
		}
		/*
		 * for (int i=0;i<yandexLocationSearchResultList.size();i++){ YandexVO
		 * yndxvo = (YandexVO)yandexLocationSearchResultList.get(i);
		 * if(yandexList.contains(yndxvo)){ yandexList.add(yndxvo); } }
		 */
		for (int j = 0; j < yandexList.size(); j++) {
			YandexVO yndxvo = (YandexVO) yandexList.get(j);
			String url = yndxvo.getUrl();
			Date dt = yndxvo.getModdate();
			List cList = CrawlerFactory.crawl(url, dt);
			TweetWithSentiments tws = new TweetWithSentiments();
			try {
				cList = tws.analyseCrawlSentiment(cList, pipeline, null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			ymap.put(yndxvo, cList);
		}
		Map map = new HashMap();
		map.put("google", gmap);
		map.put("faroo", fmap);
		map.put("yandex", ymap);
		System.out.println("gmap size: " + gmap.size());
		System.out.println("fmap size: " + fmap.size());
		System.out.println("ymap size: " + ymap.size());
		return map;
	}

	public static Map siteSearch(StanfordCoreNLP pipeline,
			List googleResultList, List googleLocationResultList,
			List farooSearchResultList, List farooLocationSearchResultList) {
		Map gmap = new HashMap();
		Map fmap = new HashMap();
		List googleList = new ArrayList();
		for (int i = 0; i < googleResultList.size(); i++) {
			Result result = (Result) googleResultList.get(i);
			if (!googleList.contains(result)) {
				googleList.add(result);
			}
		}
		for (int i = 0; i < googleLocationResultList.size(); i++) {
			Result result = (Result) googleLocationResultList.get(i);
			if (!googleList.contains(result)) {
				googleList.add(result);
			}
		}

		for (int i = 0; i < googleList.size(); i++) {
			Result result = (Result) googleList.get(i);
			String title = StrUtil.nonNull(result.getTitle());
			String url = StrUtil.nonNull(result.getUrl());
			// TODO currently working with a makeshift version of the crawl
			// method
			List cList = CrawlerFactory.crawl(url);
			TweetWithSentiments tws = new TweetWithSentiments();
			try {
				cList = tws.analyseCrawlSentiment(cList, pipeline, null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			gmap.put(result, cList);

		}

		List farooList = new ArrayList();

		for (int i = 0; i < farooSearchResultList.size(); i++) {
			FarooResultSet resultsSet = (FarooResultSet) farooSearchResultList
					.get(i);
			if (!farooList.contains(resultsSet)) {
				farooList.add(resultsSet);
			}
		}
		for (int i = 0; i < farooLocationSearchResultList.size(); i++) {
			FarooResultSet resultsSet = (FarooResultSet) farooLocationSearchResultList
					.get(i);
			if (!farooList.contains(resultsSet)) {
				farooList.add(resultsSet);
			}
		}

		for (int i = 0; i < farooList.size(); i++) {
			FarooResultSet resultsSet = (FarooResultSet) farooList.get(i);
			List farooResultList = resultsSet.getResults();
			for (int j = 0; j < farooResultList.size(); j++) {
				FarooResult fr = (FarooResult) farooResultList.get(j);
				String url = fr.getUrl();
				List cList = CrawlerFactory.crawl(url);
				TweetWithSentiments tws = new TweetWithSentiments();
				try {
					cList = tws.analyseCrawlSentiment(cList, pipeline, null);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				fmap.put(fr, cList);
			}
		}
		Map map = new HashMap();
		map.put("google", gmap);
		map.put("faroo", fmap);

		return map;
	}

	public static Map siteSearch(StanfordCoreNLP pipeline) {
		List sitesearchresult = new ArrayList();
		String aUrl = "http://deansarablog.wordpress.com/2014/10/13/the-nurse-who-broke-protocol/";
		String bUrl = "http://www.chictopia.com/photo/show/1075415-P+is+for+Pattern-blue-givenchy-bag";
		String cUrl = "http://www.flipkart.com/moto-g-2nd-gen/p/itmdygz8gqk2w3xp?pid=MOBDYGZ6SHNB7RFC&otracker=from-search&srno=t_1&query=moto+g&ref=f35665c0-140c-434c-9077-43c80ccb93ea";
		List aList = CrawlerFactory.crawl(aUrl);
		List bList = CrawlerFactory.crawl(bUrl);
		List cList = CrawlerFactory.crawl(cUrl);
		TweetWithSentiments tws = new TweetWithSentiments();
		try {
			aList = tws.analyseCrawlSentiment(aList, pipeline, null);
			bList = tws.analyseCrawlSentiment(bList, pipeline, null);
			cList = tws.analyseCrawlSentiment(cList, pipeline, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map aMap = new HashMap();
		Map bMap = new HashMap();
		Map cMap = new HashMap();

		aMap.put(aUrl, aList);
		bMap.put(bUrl, bList);
		cMap.put(cUrl, cList);

		Map newMap = new HashMap();

		newMap.put(WORDPRESS, aMap);
		newMap.put(CHICTOPIA, bMap);
		newMap.put(FLIPKART, cMap);
		System.out.println("Done putting");

		return newMap;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
