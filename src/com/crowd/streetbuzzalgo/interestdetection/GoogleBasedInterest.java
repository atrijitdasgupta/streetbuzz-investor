/**
 * 
 */
package com.crowd.streetbuzzalgo.interestdetection;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.processhelperutils.WebSiteSearch;
import com.crowd.streetbuzz.stopword.Stopper;
import com.crowd.streetbuzzalgo.constants.SystemConstants;
import com.crowd.streetbuzzalgo.googlesearch.vo.Result;
import com.crowd.streetbuzzalgo.parser.RankedKeywordParse;
import com.crowd.streetbuzzalgo.parser.vo.RankedKeywordVO;
import com.crowd.streetbuzzalgo.utils.DataClean;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.UserAgent;

/**
 * @author Atrijit
 * 
 */
public class GoogleBasedInterest implements Constants, SystemConstants {

	/**
	 * 
	 */
	public GoogleBasedInterest() {
		// TODO Auto-generated constructor stub
	}

	public static List searchGoogle(String topic) throws Exception {
		List topiclist = new ArrayList();
		topiclist.add(topic);
		List googleResultList = WebSiteSearch.googleSearch(topiclist);
		List truncatedlist;
		if (googleResultList.size() > 10) {
			truncatedlist = googleResultList.subList(0, 10);
		} else {
			truncatedlist = googleResultList;
		}
		List urllist = new ArrayList();
		for (int i = 0; i < truncatedlist.size(); i++) {
			Result result = (Result) truncatedlist.get(i);
			String url = StrUtil.nonNull(result.getUrl());
			urllist.add(url);
		
		}
		List headersList = getHeaders(urllist);
		//System.out.println(headersList);
		return headersList;
	}
	
	private static List getHeaders(List urllist)throws Exception{
		List finallist = new ArrayList();
		UserAgent ua = new UserAgent();
		for (int i = 0; i < urllist.size(); i++) {
			String url = (String) urllist.get(i);
		//	System.out.println(url);
			try {
				ua.visit(url);
			} catch (Exception e1) {
				System.out.println(e1.getMessage());
			}
			Element title = null;
			try {
				title = ua.doc.findFirst("<title>");
			} catch (Exception e) {
				
			}
			String titletext = "";
			if(title!=null){
				titletext = title.innerText();
			}
		//	System.out.println(titletext);
			finallist.add(titletext);
			/*Elements metalot = null;
			try {
				metalot = ua.doc.findFirst("<head>").findEvery("<meta>");
			} catch (Exception e1) {
				
			}
			List tempList = new ArrayList();
			if (metalot != null) {
				tempList = metalot.toList();
			}*/
			
			/*for (int j = 0; j < tempList.size(); j++) {
				Element meta = (Element) tempList.get(j);
				List attNames = meta.getAttributeNames();
				for (int k = 0; k < attNames.size(); k++) {
					String content = meta.getAttx("content");
					if (!finallist.contains(content)) {
						if(content.indexOf("http")<0){
							finallist.add(content);
						}
						
					}
					
				}
			}*/
			
			
			
		}
		return finallist;
	}

	private static List getPhrases(List urllist) throws Exception {
		List finallist = new ArrayList();
		UserAgent ua = new UserAgent();
		for (int i = 0; i < urllist.size(); i++) {
			String url = (String) urllist.get(i);
			try {
				ua.visit(url);
			} catch (RuntimeException e1) {
				System.out.println(e1.getMessage());
			}
			String content = StrUtil.nonNull(ua.doc.innerHTML());
			content = DataClean.htmlClean(content);
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
					// System.out.println("temp " + temp);
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
						//System.out.println("phrase " + phrase);
						String phrasesmall = phrase.toLowerCase();
						if (!Stopper.stopLucene(phrasesmall)) {
							if (!finallist.contains(phrasesmall)) {
								if(!phrasesmall.startsWith("http")|| !phrasesmall.startsWith("www")){
									finallist.add(phrasesmall);
								}
								
							}

						}

					}

				}
			}
		}
		try {
			ua.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		return finallist;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			searchGoogle("It happened one night");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
