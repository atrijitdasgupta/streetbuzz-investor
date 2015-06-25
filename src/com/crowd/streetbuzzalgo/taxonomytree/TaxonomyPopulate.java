/**
 * 
 */
package com.crowd.streetbuzzalgo.taxonomytree;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.TaxonomyDAO;
import com.crowd.streetbuzz.model.Taxonomy;
import com.crowd.streetbuzz.processhelperutils.WebSiteSearch;
import com.crowd.streetbuzz.stopword.Stopper;
import com.crowd.streetbuzzalgo.constants.SystemConstants;
import com.crowd.streetbuzzalgo.faroosearch.vo.FarooResult;
import com.crowd.streetbuzzalgo.faroosearch.vo.FarooResultSet;
import com.crowd.streetbuzzalgo.googlesearch.vo.Result;
import com.crowd.streetbuzzalgo.parser.RankedKeywordParse;
import com.crowd.streetbuzzalgo.parser.vo.RankedKeywordVO;
import com.crowd.streetbuzzalgo.topsy.Otter;
import com.crowd.streetbuzzalgo.utils.DataClean;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.crowd.streetbuzzalgo.yandexsearch.YandexSearch;
import com.crowd.streetbuzzalgo.yandexsearch.YandexVO;
import com.jaunt.ResponseException;
import com.jaunt.UserAgent;

/**
 * @author Atrijit
 * 
 */
public class TaxonomyPopulate implements Constants, SystemConstants {

	/**
	 * 
	 */
	public TaxonomyPopulate() {
		// TODO Auto-generated constructor stub
	}

	public static void populate(TaxonomyDAO taxonomyDAO) {
		// List seedList = taxonomyDAO.getAllRecords();
		/*List seedList = taxonomyDAO.getAllRecordsByRoot("Vehicles");
		for (int i = 0; i < seedList.size(); i++) {
			Taxonomy taxonomy = (Taxonomy) seedList.get(i);
			String linkedword = taxonomy.getLinkedword();
			String searchterm = linkedword.trim();
			String rootword = taxonomy.getRootword();
			Map finalMap = runSearch(searchterm);
			process(rootword, finalMap, searchterm, taxonomyDAO);
		}*/
		List seedList = taxonomyDAO.getAllRecordsByRoot("Fitness");
		for (int i = 0; i < seedList.size(); i++) {
			Taxonomy taxonomy = (Taxonomy) seedList.get(i);
			String linkedword = taxonomy.getLinkedword();
			String searchterm = linkedword.trim();
			String rootword = taxonomy.getRootword();
			Map finalMap = runSearch(searchterm);
			process(rootword, finalMap, searchterm, taxonomyDAO);
		}

	}

	private static void process(String rootword, Map finalMap,
			String searchterm, TaxonomyDAO taxonomyDAO) {
		List phraseList = (List) finalMap.get("phraseList");
		for (int i = 0; i < phraseList.size(); i++) {
			TaxonomyVO taxvo = (TaxonomyVO) phraseList.get(i);
			String phrase = taxvo.getWordphrase();
			String url = taxvo.getUrl();
			String hash = StrUtil.getHash(url);
			String compare = StrUtil.removeColon(phrase);
			compare = compare.trim();

			if (!compare.equalsIgnoreCase(searchterm)) {
				Taxonomy taxonomy = (Taxonomy) taxonomyDAO
						.getObjectByRootLinkHash(searchterm, compare, hash);
				if (taxonomy != null) {
					Integer count = taxonomy.getScore();
					int countInt = count.intValue();
					countInt = countInt + 1;
					count = new Integer(countInt);
					taxonomy.setScore(count);
					taxonomyDAO.addOrUpdateRecord(taxonomy);
				} else {
					boolean processbool = true;
					if (compare.startsWith("http")) {
						processbool = false;
					}
					if (compare.startsWith("www")) {
						processbool = false;

					}
					if (compare.length() > (maxsentencesize - 2)) {
						processbool = false;
					}
					if (processbool) {
						Taxonomy taxonomynew = new Taxonomy();
						taxonomynew.setLinkedword(compare);
						taxonomynew.setRootword(searchterm);
						taxonomynew.setScore(new Integer(1));
						taxonomynew.setCategoryid(getCategoryId(rootword));
						taxonomynew.setType("phrase");
						taxonomynew.setRun(new Integer(2));
						taxonomynew.setHash(hash);
						taxonomyDAO.addOrUpdateRecord(taxonomynew);
					}

				}
			}

		}
		List wordList = (List) finalMap.get("wordList");
		for (int i = 0; i < wordList.size(); i++) {
			TaxonomyVO taxvo = (TaxonomyVO) wordList.get(i);
			String word = taxvo.getWordphrase();
			String url = taxvo.getUrl();
			String hash = StrUtil.getHash(url);
			String[] tempArr = word.split(" ");
			for (int j = 0; j < tempArr.length; j++) {
				String compare = tempArr[j];
				compare = compare.trim();
				if (!compare.equalsIgnoreCase(searchterm)) {
					Taxonomy taxonomy = (Taxonomy) taxonomyDAO
							.getObjectByRootLinkHash(searchterm, compare,hash);
					if (taxonomy != null) {
						Integer count = taxonomy.getScore();
						int countInt = count.intValue();
						countInt = countInt + 1;
						count = new Integer(countInt);
						taxonomy.setScore(count);
						taxonomyDAO.addOrUpdateRecord(taxonomy);
					} else {
						boolean processbool = true;
						if (compare.startsWith("http")) {
							processbool = false;
						}
						if (compare.startsWith("www")) {
							processbool = false;

						}
						if (compare.length() > comparewordsize) {
							processbool = false;
						}
						if (processbool) {
							Taxonomy taxonomynew = new Taxonomy();
							taxonomynew.setLinkedword(compare);
							taxonomynew.setRootword(searchterm);
							taxonomynew.setScore(new Integer(1));
							taxonomynew.setCategoryid(getCategoryId(rootword));
							taxonomynew.setType("word");
							taxonomynew.setRun(new Integer(2));
							taxonomynew.setHash(hash);
							taxonomyDAO.addOrUpdateRecord(taxonomynew);
						}

					}
				}
			}
		}
	}

	private static Long getCategoryId(String searchterm) {
		if ("Fitness".equalsIgnoreCase(searchterm)) {
			return new Long(3);
		} else {
			return new Long(4);
		}
	}

	private static Map runSearch(String searchterm) {
		List searchtermList = new ArrayList();
		searchtermList.add(searchterm);
		searchtermList.add(searchterm + " blog");

		List otterList = new ArrayList();
		List googleResultList = new ArrayList();
		List farooSearchResultList = new ArrayList();
		List yandexSearchResultList = new ArrayList();
		List grList = new ArrayList();
		if (otterbool) {
			otterList = Otter.call(searchtermList);
		}
		if (googlebool) {
			googleResultList = WebSiteSearch.googleSearch(searchtermList);
			int count = googleResultList.size();

			if (count > 6) {
				grList = googleResultList.subList(0, 4);
			}

		}

		if (faroobool) {
			farooSearchResultList = WebSiteSearch.farooSearch(searchtermList);
		}

		if (yandexbool) {
			yandexSearchResultList = YandexSearch.yandexSearch(searchtermList);
		}

		// OtterList can be processed as such. The other three search results
		// need pulling out of the URLs
		List linksList = extractLinks(grList, farooSearchResultList,
				yandexSearchResultList);
		System.out.println("linksList size()::" + linksList.size());
		Map finalMap = processLinks(linksList);
		/*
		 * List wordList = (List)finalMap.get("wordList"); for (int i = 0; i <
		 * otterList.size(); i++) { Post post = (Post) otterList.get(i); String
		 * content = post.getContent(); String cleantext =
		 * DataClean.clean(content); StringBuffer stoppedBuf =
		 * Stopper.stoptaxonomy(cleantext); wordList.add(stoppedBuf.toString()); }
		 * finalMap.put("wordList", wordList);
		 */
		return finalMap;

	}

	private static List extractLinks(List googleResultList,
			List farooSearchResultList, List yandexSearchResultList) {
		List linksList = new ArrayList();

		for (int i = 0; i < googleResultList.size(); i++) {
			Result result = (Result) googleResultList.get(i);
			String url = result.getUrl();
			linksList.add(url);
		}

		for (int j = 0; j < farooSearchResultList.size(); j++) {
			FarooResultSet resultsSet = (FarooResultSet) farooSearchResultList
					.get(j);
			List fResults = resultsSet.getResults();
			for (int k = 0; k < fResults.size(); k++) {
				FarooResult fr = (FarooResult) fResults.get(k);
				String url = fr.getUrl();
				linksList.add(url);
			}
		}

		for (int p = 0; p < yandexSearchResultList.size(); p++) {
			YandexVO yndxvo = (YandexVO) yandexSearchResultList.get(p);
			String url = yndxvo.getUrl();
			linksList.add(url);
		}

		return linksList;
	}

	private static List getPhrases(String content) {
		System.out.println("inside getPhrases");
		content = DataClean.htmlClean(content);
		List phrasesList = new ArrayList();
		BreakIterator border = BreakIterator.getSentenceInstance(Locale.US);
		border.setText(content);
		int start = border.first();
		List list = new ArrayList();
		for (int end = border.next(); end != BreakIterator.DONE; start = end, end = border
				.next()) {
			String temp = content.substring(start, end);
			if (temp.length() > maxsentencesize) {
				temp = temp.substring(0, (maxsentencesize - 2));
			}
			try {
				System.out.println("temp " + temp);
				list = RankedKeywordParse.parseRankedKeywords(temp);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
			if (list != null && list.size() > 0) {
				for (int j = 0; j < list.size(); j++) {
					RankedKeywordVO rkvo = (RankedKeywordVO) list.get(j);
					String phrase = rkvo.getText();
					System.out.println("phrase " + phrase);
					String phrasesmall = phrase.toLowerCase();
					if (!Stopper.stopLucene(phrasesmall)) {
						phrasesList.add(j + ":" + phrase);
					}

				}

			}
		}
		return phrasesList;
	}

	private static Map processLinks(List linksList) {
		Map map = new HashMap();
		List wordList = new ArrayList();
		List fullphraseList = new ArrayList();
		UserAgent userAgent = new UserAgent();
		for (int i = 0; i < linksList.size(); i++) {
			String url = (String) linksList.get(i);
			try {
				userAgent.visit(url);
			} catch (ResponseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
			String content = "";
			try {
				content = StrUtil.nonNull(userAgent.doc.innerHTML());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}

			if (!"".equalsIgnoreCase(content)) {
				// getPhrases call also removes stop words
				List phraseList = getPhrases(content);
				if (phraseList != null) {
					for (int k = 0; k < phraseList.size(); k++) {
						String temp = (String) phraseList.get(k);
						TaxonomyVO taxvo = new TaxonomyVO();
						taxvo.setUrl(url);
						taxvo.setWordphrase(temp);
						fullphraseList.add(taxvo);
					}

				}
			}
			String cleantext = "";
			if (!"".equalsIgnoreCase(content)) {
				cleantext = StrUtil.nonNull(DataClean.numberClean(content));
			}
			StringBuffer stoppedBuf = new StringBuffer();
			if (!"".equalsIgnoreCase(cleantext)) {
				stoppedBuf = Stopper.stoptaxonomy(cleantext);

			}
			TaxonomyVO taxvo = new TaxonomyVO();
			taxvo.setUrl(url);
			taxvo.setWordphrase(stoppedBuf.toString());
			wordList.add(taxvo);
		}
		map.put("wordList", wordList);
		map.put("phraseList", fullphraseList);
		try {
			userAgent.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		return map;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
