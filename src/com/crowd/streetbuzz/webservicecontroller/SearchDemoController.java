/**
 * 
 */
package com.crowd.streetbuzz.webservicecontroller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.processhelperutils.DemoWordProcess;
import com.crowd.streetbuzz.processhelperutils.ImageSearch;
import com.crowd.streetbuzz.processhelperutils.NetworkSearch;
import com.crowd.streetbuzz.processhelperutils.ProcessHelperUtils;
import com.crowd.streetbuzz.processhelperutils.VideoSearch;
import com.crowd.streetbuzz.processhelperutils.WebSiteSearch;
import com.crowd.streetbuzzalgo.faroosearch.vo.FarooResult;
import com.crowd.streetbuzzalgo.faroosearch.vo.FarooResultSet;
import com.crowd.streetbuzzalgo.googlesearch.vo.Result;
import com.crowd.streetbuzzalgo.parser.EntityParser;
import com.crowd.streetbuzzalgo.parser.InvokeParser;
import com.crowd.streetbuzzalgo.parser.RankedKeywordParse;
import com.crowd.streetbuzzalgo.parser.RelationsParse;
import com.crowd.streetbuzzalgo.parser.vo.BasicDependencyVO;
import com.crowd.streetbuzzalgo.parser.vo.RelationsParseVO;
import com.crowd.streetbuzzalgo.parser.vo.StanfordNerVO;
import com.crowd.streetbuzzalgo.socialplugin.youtube.YTSearch;
import com.crowd.streetbuzzalgo.topsy.Otter;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.crowd.streetbuzzalgo.vo.SearchDemoVO;
import com.crowd.streetbuzzalgo.yandexsearch.YandexVO;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

/**
 * @author Atrijit
 * 
 */
public class SearchDemoController implements Controller, Constants {
	private String returnView = "";
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("In here SearchDemoController");
		String query = request.getParameter("query");
		
		long start = System.currentTimeMillis();
		YTSearch.searchYoutube(query);
		long end = System.currentTimeMillis();
		long gap = end - start;
		System.out.println(query+" ***************YUTUBE EACH TIMING************"+gap/1000+" secs.");
		return new ModelAndView(returnView);
	}


	public ModelAndView handleRequestOld(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String searchtopic = StrUtil.nonNull(request
				.getParameter("searchstring"));
		String category = StrUtil.nonNull(request.getParameter("category"));
		Map searchMap = runSearch(searchtopic, category);
		returnView = "pages/searchdemoresult.jsp?p=";
		HttpSession session = request.getSession(true);
		session.setAttribute("searchMap", searchMap);
		return new ModelAndView(returnView);
	}

	private static Map runSearch(String searchtopic, String category) {
		// First clean the String
		searchtopic = ProcessHelperUtils.clean(searchtopic);

		// The Stanford entity parse call to go here
		StanfordNerVO snvo = getStanfordNamedEntities(searchtopic);

		Map searchTermsMap = getSearchTerms(searchtopic, snvo);
		Map searchresultMap = doSearch(searchTermsMap);
		return searchresultMap;
	}

	private static Map doSearch(Map searchTermsMap) {
		List noloclist = (List) searchTermsMap.get("nolocation");
		List nolocbloglist = (List) searchTermsMap.get("nolocationblog");
		List loclist = (List) searchTermsMap.get("location");
		List locbloglist = (List) searchTermsMap.get("locationblog");

		// Twitter Search
		List twitterList = NetworkSearch.searchTwitter(noloclist, loclist);
		// Video Search
		// Youtube Search
		List YoutubeList = VideoSearch.searchYoutube(noloclist);
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
		List farooResultList = WebSiteSearch.farooSearch(noloclist);
		List farooBlogResultList = WebSiteSearch.farooSearch(nolocbloglist);
		List farooLocationResultList = new ArrayList();
		if (loclist != null && loclist.size() > 0) {
			farooLocationResultList = WebSiteSearch.farooSearch(loclist);
		}
		List farooBlogLocationResultList = new ArrayList();
		if (locbloglist != null && locbloglist.size() > 0) {
			farooBlogLocationResultList = WebSiteSearch
					.farooSearch(locbloglist);
		}

		// Yandex Searches
		List yandexResultList = WebSiteSearch.yandexSearch(noloclist);
		List yandexBlogResultList = WebSiteSearch.yandexSearch(nolocbloglist);
		List yandexLocationResultList = new ArrayList();
		if (loclist != null && loclist.size() > 0) {
			yandexLocationResultList = WebSiteSearch.yandexSearch(loclist);
		}
		List yandexBlogLocationResultList = new ArrayList();
		if (locbloglist != null && locbloglist.size() > 0) {
			yandexBlogLocationResultList = WebSiteSearch
					.yandexSearch(locbloglist);
		}
		// Mash these up
		List megaList = new ArrayList();
		List fullSearchList = new ArrayList();
		// Google mash
		for (int i = 0; i < googleResultList.size(); i++) {
			Result result = (Result) googleResultList.get(i);
			String title = result.getTitle();
			String url = result.getUrl();
			if (!megaList.contains(url)) {
				SearchDemoVO sdvo = new SearchDemoVO();
				sdvo.setUrl(url);
				sdvo.setTitle(title);
				sdvo.setDate(new Date());
				sdvo.setSearchsource("Google");
				megaList.add(url);
				fullSearchList.add(sdvo);

			}

		}
		for (int i = 0; i < googleBlogResultList.size(); i++) {
			Result result = (Result) googleBlogResultList.get(i);
			String title = result.getTitle();
			String url = result.getUrl();
			if (!megaList.contains(url)) {
				SearchDemoVO sdvo = new SearchDemoVO();
				sdvo.setUrl(url);
				sdvo.setTitle(title);
				sdvo.setDate(new Date());
				sdvo.setSearchsource("Google - blog");
				megaList.add(url);
				fullSearchList.add(sdvo);
			}

		}

		for (int i = 0; i < googleLocationResultList.size(); i++) {
			Result result = (Result) googleLocationResultList.get(i);
			String title = result.getTitle();
			String url = result.getUrl();
			if (!megaList.contains(url)) {
				SearchDemoVO sdvo = new SearchDemoVO();
				sdvo.setUrl(url);
				sdvo.setTitle(title);
				sdvo.setDate(new Date());
				sdvo.setSearchsource("Google - location");
				megaList.add(url);
				fullSearchList.add(sdvo);
			}

		}

		for (int i = 0; i < googleBlogLocationResultList.size(); i++) {
			Result result = (Result) googleBlogLocationResultList.get(i);
			String title = result.getTitle();
			String url = result.getUrl();
			if (!megaList.contains(url)) {
				SearchDemoVO sdvo = new SearchDemoVO();
				sdvo.setUrl(url);
				sdvo.setTitle(title);
				sdvo.setDate(new Date());
				sdvo.setSearchsource("Google - location - blog");
				megaList.add(url);
				fullSearchList.add(sdvo);
			}

		}
		// Faroo mash
		for (int i = 0; i < farooResultList.size(); i++) {
			FarooResultSet frs = (FarooResultSet) farooResultList.get(i);
			List results = frs.getResults();
			if (results != null) {
				for (int j = 0; j < results.size(); j++) {
					FarooResult fr = (FarooResult) results.get(j);
					String title = fr.getTitle();
					String url = fr.getUrl();
					if (!megaList.contains(url)) {
						SearchDemoVO sdvo = new SearchDemoVO();
						sdvo.setUrl(url);
						sdvo.setTitle(title);
						sdvo.setDate(new Date());
						sdvo.setSearchsource("Faroo");
						megaList.add(url);
						fullSearchList.add(sdvo);

					}
				}
			}

		}

		for (int i = 0; i < farooBlogResultList.size(); i++) {
			FarooResultSet frs = (FarooResultSet) farooBlogResultList.get(i);
			List results = frs.getResults();
			if (results != null) {
				for (int j = 0; j < results.size(); j++) {
					FarooResult fr = (FarooResult) results.get(j);
					String title = fr.getTitle();
					String url = fr.getUrl();
					if (!megaList.contains(url)) {
						SearchDemoVO sdvo = new SearchDemoVO();
						sdvo.setUrl(url);
						sdvo.setTitle(title);
						sdvo.setDate(new Date());
						sdvo.setSearchsource("Faroo");
						megaList.add(url);
						fullSearchList.add(sdvo);

					}
				}
			}

		}

		for (int i = 0; i < farooLocationResultList.size(); i++) {
			FarooResultSet frs = (FarooResultSet) farooLocationResultList
					.get(i);
			List results = frs.getResults();
			if (results != null) {
				for (int j = 0; j < results.size(); j++) {
					FarooResult fr = (FarooResult) results.get(j);
					String title = fr.getTitle();
					String url = fr.getUrl();
					if (!megaList.contains(url)) {
						SearchDemoVO sdvo = new SearchDemoVO();
						sdvo.setUrl(url);
						sdvo.setTitle(title);
						sdvo.setDate(new Date());
						sdvo.setSearchsource("Faroo");
						megaList.add(url);
						fullSearchList.add(sdvo);

					}
				}
			}

		}

		for (int i = 0; i < farooBlogLocationResultList.size(); i++) {
			FarooResultSet frs = (FarooResultSet) farooBlogLocationResultList
					.get(i);
			List results = frs.getResults();
			if (results != null) {
				for (int j = 0; j < results.size(); j++) {
					FarooResult fr = (FarooResult) results.get(j);
					String title = fr.getTitle();
					String url = fr.getUrl();
					if (!megaList.contains(url)) {
						SearchDemoVO sdvo = new SearchDemoVO();
						sdvo.setUrl(url);
						sdvo.setTitle(title);
						sdvo.setDate(new Date());
						sdvo.setSearchsource("Faroo");
						megaList.add(url);
						fullSearchList.add(sdvo);

					}
				}
			}

		}
		// Yandex Search
		for (int i = 0; i < yandexResultList.size(); i++) {
			YandexVO yndxvo = (YandexVO) yandexResultList.get(i);
			String title = yndxvo.getTitle();
			String url = yndxvo.getUrl();
			if (!megaList.contains(url)) {
				SearchDemoVO sdvo = new SearchDemoVO();
				sdvo.setUrl(url);
				sdvo.setTitle(title);
				sdvo.setDate(new Date());
				sdvo.setSearchsource("Yandex");
				megaList.add(url);
				fullSearchList.add(sdvo);

			}

		}

		for (int i = 0; i < yandexBlogResultList.size(); i++) {
			YandexVO yndxvo = (YandexVO) yandexBlogResultList.get(i);
			String title = yndxvo.getTitle();
			String url = yndxvo.getUrl();
			if (!megaList.contains(url)) {
				SearchDemoVO sdvo = new SearchDemoVO();
				sdvo.setUrl(url);
				sdvo.setTitle(title);
				sdvo.setDate(new Date());
				sdvo.setSearchsource("Yandex - blog");
				megaList.add(url);
				fullSearchList.add(sdvo);

			}

		}

		for (int i = 0; i < yandexLocationResultList.size(); i++) {
			YandexVO yndxvo = (YandexVO) yandexLocationResultList.get(i);
			String title = yndxvo.getTitle();
			String url = yndxvo.getUrl();
			if (!megaList.contains(url)) {
				SearchDemoVO sdvo = new SearchDemoVO();
				sdvo.setUrl(url);
				sdvo.setTitle(title);
				sdvo.setDate(new Date());
				sdvo.setSearchsource("Yandex - location");
				megaList.add(url);
				fullSearchList.add(sdvo);

			}

		}

		for (int i = 0; i < yandexBlogLocationResultList.size(); i++) {
			YandexVO yndxvo = (YandexVO) yandexBlogLocationResultList.get(i);
			String title = yndxvo.getTitle();
			String url = yndxvo.getUrl();
			if (!megaList.contains(url)) {
				SearchDemoVO sdvo = new SearchDemoVO();
				sdvo.setUrl(url);
				sdvo.setTitle(title);
				sdvo.setDate(new Date());
				sdvo.setSearchsource("Yandex - location - blog");
				megaList.add(url);
				fullSearchList.add(sdvo);

			}

		}

		// Photo Search
		List imagesList = ImageSearch.googleImageSearch(noloclist);
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
		
		List otterList = Otter.call(noloclist);
		searchMap.put("otterList", otterList);
		return searchMap;
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

		searchMap.put("nolocation", new ArrayList());
		searchMap.put("nolocationblog", new ArrayList());
		searchMap.put("location", new ArrayList());
		searchMap.put("locationblog", new ArrayList());

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

			String temploc = locStr.toLowerCase();
			if (smalltemp.indexOf(temploc) < 0) {
				loclist.add(temp + " " + locStr);

				if (smalltemp.indexOf("blog") < 0) {
					locbloglist.add(temp + " " + locStr + " blog");
				}
			}
		}
		searchMap.put("nolocation", noloclist);
		searchMap.put("nolocationblog", nolocbloglist);
		searchMap.put("location", loclist);
		searchMap.put("locationblog", locbloglist);

		return searchMap;
	}

	public static String getVerb(RelationsParseVO rvo) {
		List subjectverbtextlist = rvo.getSubjectverbtext();
		List subjectverbactionlist = rvo.getSubjectactiontext();
		String verb = "";
		if (subjectverbtextlist != null && subjectverbtextlist.size() > 0) {
			verb = StrUtil.nonNull((String) subjectverbtextlist.get(0));
		}
		if (!"".equalsIgnoreCase(verb)) {
			return verb;
		} else if (subjectverbactionlist != null
				&& subjectverbactionlist.size() > 0) {
			verb = StrUtil.nonNull((String) subjectverbactionlist.get(0));
			return verb;
		} else {
			return verb;
		}
	}

	public static List rankedKeywordsParse(String entry) {
		List result = new ArrayList();
		try {
			result = RankedKeywordParse.parseRankedKeywords(entry);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static RelationsParseVO relationsParse(String entry) {
		RelationsParseVO rvo = new RelationsParseVO();
		try {
			rvo = RelationsParse.parseRelations(entry);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rvo;
	}

	public static BasicDependencyVO getBasicStanfordDependencies(String entry) {
		String grammar = BASEEXTLIBPATH + "stanfordnlp/englishPCFG.ser.gz";
		String[] options = { "-maxLength", "80", "-retainTmpSubcategories" };
		LexicalizedParser lp = LexicalizedParser.loadModel(grammar, options);
		BasicDependencyVO bdvo = InvokeParser.runMorefromSentence(lp, entry);
		return bdvo;
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

	public static void main(String[] args) {
		runSearch("Will BJP win in Delhi?", "");
	}
}
