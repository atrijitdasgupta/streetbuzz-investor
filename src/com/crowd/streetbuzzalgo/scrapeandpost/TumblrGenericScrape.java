/**
 * 
 */
package com.crowd.streetbuzzalgo.scrapeandpost;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.crowd.streetbuzzalgo.crawler.vo.CrawlerVO;
import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.UserAgent;

/**
 * @author Atrijit
 *
 */
public class TumblrGenericScrape {

	/**
	 * 
	 */
	public TumblrGenericScrape() {
		// TODO Auto-generated constructor stub
	}
	
	public static List scrape(String url) throws Exception {
		List retList = new ArrayList();
		UserAgent userAgent = new UserAgent();
		userAgent.visit(url);
		Element div = userAgent.doc.findFirst("<ol class=notes>");
		Elements blockquotes = div.findEach("<blockquote>");
		List blocklist = blockquotes.toList();
		for (int i=0;i<blocklist.size();i++){
			Element block = (Element) blocklist.get(i);
			String comment = block.innerText();
			CrawlerVO cvo = new CrawlerVO();
			cvo.setComment(comment);
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
		try {
			scrape("http://mademoiselle-bazaar.tumblr.com/post/117909896955");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
