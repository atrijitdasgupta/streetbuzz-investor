/**
 * 
 */
package com.crowd.streetbuzzalgo.scrapeandpost;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.crowd.streetbuzzalgo.crawler.vo.CrawlerVO;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.UserAgent;

/**
 * @author Atrijit
 * 
 */
public class WordPressCrawler {

	/**
	 * 
	 */
	public WordPressCrawler() {
		// TODO Auto-generated constructor stub
	}

	public static List scrape(String url) throws Exception {
		UserAgent userAgent = new UserAgent();
		userAgent.visit(url);
		Element div = null;
		try {
			div = userAgent.doc.findFirst("<div id=comments>");
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			System.out.println("GenericOne JAUNT parse exception: "
					+ e2.getMessage());
		}
		Elements comments = null;
		Elements commenters = null;
		if (div != null) {
			try {
				comments = div.findEach("<div class=comment-content>");
				// commenters = div.findEach("<span class>");
				commenters = div.findEach("<div class=comment-author vcard>");
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				System.out.println("GenericOne JAUNT parse exception: "
						+ e.getMessage());
			}
		}
		List commentsList = new ArrayList();
		List commentersList = new ArrayList();
		if (comments != null) {
			commentsList = comments.toList();
		}
		if (commenters != null) {
			commentersList = commenters.toList();
		}
		int comsize = 0;
		if (commentsList != null) {
			comsize = commentsList.size();
		}

		List retList = new ArrayList();
		for (int i = 0; i < comsize; i++) {
			Element comnt = (Element) commentsList.get(i);
			// Element comntr = (Element)commentersList.get(i);
			String comment = StrUtil.nonNull(comnt.innerText());
			// System.out.println(comment);
			// String name = StrUtil.nonNull(comntr.getText());
			String name = "";
			//System.out.println(comment + "::" + name);
			if ("".equalsIgnoreCase(name)) {
				name = "Anonymous";
			}
			CrawlerVO cvo = new CrawlerVO();
			cvo.setComment(comment);
			cvo.setUsername(name);
			cvo.setURLParsed(url);
			cvo.setMode(StrUtil.getHost(url));
			retList.add(cvo);
		}
		try {
			userAgent.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		return retList;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
