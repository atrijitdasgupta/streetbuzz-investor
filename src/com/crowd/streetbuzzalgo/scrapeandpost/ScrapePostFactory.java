/**
 * 
 */
package com.crowd.streetbuzzalgo.scrapeandpost;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzzalgo.constants.SystemConstants;
import com.jaunt.UserAgent;

/**
 * @author Atrijit
 * 
 */
public class ScrapePostFactory implements SystemConstants, Constants {

	/**
	 * 
	 */
	public ScrapePostFactory() {
		// TODO Auto-generated constructor stub
	}
	
	public static void scrapelist(List list){
		for (int i=0;i<list.size();i++){
			Long start = System.currentTimeMillis();
			String url = (String)list.get(i);
			if(url.indexOf(WIKIPEDIAEXC)>-1){
				continue;
			}
			if(url.indexOf(YOUTUBEEXC)>-1){
				continue;
			}
			if(url.indexOf(PDFEXC)>-1){
				continue;
			}
			try {
				scrape(url);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Long end = System.currentTimeMillis();
			System.out.println(i+". Time needed: "+(end-start));
			System.out.println("******************************************************************");
			
		}
	}

	public static List scrape(String url) throws Exception {

		List list = new ArrayList();
		UserAgent userAgent = new UserAgent();
		userAgent.visit(url);
		String html = userAgent.doc.innerHTML();
		// System.out.println(html);

		// DISQUS COMMENTING SYSTEM
		boolean disqustypeone = false;
		boolean disqustypetwo = false;
		if (html.indexOf("disqus_shortname") > 0) {
			disqustypeone = true;
		}
		String shortname = "";
		if (disqustypeone) {
			int num = html.indexOf("disqus_shortname");
			int length = "disqus_shortname".length();
			String temp = html.substring(num + length, num + length + 50);
			// System.out.println(temp);
			shortname = temp.substring((temp.indexOf("'") + 1), temp
					.indexOf("';"));
			//System.out.println("one: " + shortname);
		}
		
		if(html.indexOf("disqus_identifier")>0){
			int num = html.indexOf("disqus_identifier");
			int length = "disqus_identifier".length();
			String temp = html.substring(num + length, num + length + 25);
			temp = temp.substring((temp.indexOf("': '")+"': '".length()),temp.indexOf("',"));
			System.out.println(temp);
			shortname = temp;
			disqustypeone = true;
		}

		if (html.indexOf("dsq.src") > 0 && !disqustypeone) {
			disqustypetwo = true;
		}
		if (disqustypetwo) {
			int num = html.indexOf("dsq.src");
			int length = "dsq.src".length();
			String temp = html.substring(num + length, num + length + 50);
			// System.out.println(temp);
			shortname = temp.substring((temp.indexOf("'") + 1), temp
					.indexOf("';"));
		//	System.out.println("two: " + shortname);
		}
		if (disqustypeone || disqustypetwo) {
			System.out.println("Got Disqus");
			list = Disqus.scrape(url, shortname);
			/*
			 * for (int i=0;i<list.size();i++){ CrawlerVO cvo =
			 * (CrawlerVO)list.get(i); System.out.println(cvo.getAvatar()+"<==>"+cvo.getComment()+"<==>"+cvo.getUsername()); }
			 */
		}
		if (list != null && list.size() > 0) {
			return list;
		}
		// IF WORDPRESS BUT NOT DISQUS
		/*if (url.indexOf(WORDPRESS) > -1) {
		//	System.out.println("Detected wordpress URL");
			try {
				list = WordPressCrawler.scrape(url);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (list != null && list.size() > 0) {
			return list;
		}*/

		if (url.indexOf("blogger") > -1 || url.indexOf("blogspot") > -1) {
			list = BloggerCrawler.scrape(url, html);
		}
		if (list != null && list.size() > 0) {
			return list;
		}

		if (url.indexOf("tumblr.com") > -1) {
			System.out.println("Detected tumblr URL");
			list = TumblrGenericScrape.scrape(url);
		}
		if (list != null && list.size() > 0) {
			return list;
		}

		/*try {
			list = GenericOne.scrape(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (list != null && list.size() > 0) {
			return list;
		}*/
		try {
			userAgent.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		return list;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			scrape("https://disqus.com/home/channel/discussdisqus/discussion/channel-discussdisqus/discuss_disqus_101/?utm_campaign=kb_referral&utm_content=footer_link&utm_source=disqus&utm_medium=website");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
