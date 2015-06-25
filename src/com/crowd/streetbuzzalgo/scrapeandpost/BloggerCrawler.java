/**
 * 
 */
package com.crowd.streetbuzzalgo.scrapeandpost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.crowd.streetbuzzalgo.constants.SystemConstants;
import com.crowd.streetbuzzalgo.crawler.vo.CrawlerVO;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gdata.client.blogger.BloggerService;
import com.google.gdata.data.Entry;
import com.google.gdata.data.Feed;
import com.google.gdata.data.TextContent;
import com.google.gdata.util.ServiceException;
import com.jaunt.UserAgent;

/**
 * @author Atrijit
 * 
 */
public class BloggerCrawler implements SystemConstants, ScrapeConstants {
	private static final String FEED_URI_BASE = "http://www.blogger.com/feeds";

	private static final String POSTS_FEED_URI_SUFFIX = "/posts/default";

	private static final String COMMENTS_FEED_URI_SUFFIX = "/comments/default";

	private static String feedUri;

	/**
	 * 
	 */
	public BloggerCrawler() {
		// TODO Auto-generated constructor stub
	}

	public static List scrape(String url, String html) throws Exception {
	//	BloggerService myService = new BloggerService("streetbuzz");
		run(null, "atrijitdasgupta@gmail.com", "lkpg363jkw22", html,url);
		return null;
	}
	
	private static List run(BloggerService myService, String userName,
			String userPassword, String html, String url) throws Exception {
		List list = new ArrayList();
		return list;
	}

	private static void runOld(BloggerService myService, String userName,
			String userPassword, String html) throws Exception {
		myService.setUserCredentials(userName, userPassword);
		String[] idarr = getIds(html);
		String blogId = StrUtil.nonNull(idarr[0]);
		String postId = StrUtil.nonNull(idarr[1]);
		String feedUri = "http://www.blogger.com/feeds" + "/" + blogId;
		printAllComments(myService, postId, feedUri);
	}

	private static void printAllComments(BloggerService myService,
			String postId, String feedUri) throws ServiceException, IOException {
		// Build comment feed URI and request comments on the specified post
		String commentsFeedUri = feedUri + "/" + postId
				+ COMMENTS_FEED_URI_SUFFIX;
		System.out.println(commentsFeedUri);
		URL feedUrl = new URL(commentsFeedUri);
		Feed resultFeed = myService.getFeed(feedUrl, Feed.class);

		// Display the results
		System.out.println(resultFeed.getTitle().getPlainText());
		for (int i = 0; i < resultFeed.getEntries().size(); i++) {
			Entry entry = resultFeed.getEntries().get(i);
			System.out.println("\t"
					+ ((TextContent) entry.getContent()).getContent()
							.getPlainText());
			System.out.println("\t" + entry.getUpdated().toStringRfc822());
		}
		System.out.println();
	}

	public static List directscrape(String url, String html) throws Exception {
		List retList = new ArrayList();
		String bloggerurl = blogger_url;
		String[] idarr = getIds(html);
		String blogId = StrUtil.nonNull(idarr[0]);
		String postId = StrUtil.nonNull(idarr[1]);
		boolean run = false;
		if (StrUtil.onlyNumbers(blogId) && StrUtil.onlyNumbers(postId)) {
			run = true;
		}
		if (!run) {
			return new ArrayList();
		}
		bloggerurl = bloggerurl.replaceAll("#blogId#", blogId);
		bloggerurl = bloggerurl.replaceAll("#postId#", postId);
		bloggerurl = bloggerurl + "?key=" + blogger_key;
		System.out.println(bloggerurl);
		bloggerurl = "http://www.blogger.com/feeds/" + blogId
				+ "/comments/default";

		BufferedReader in = new BufferedReader(new InputStreamReader(new URL(
				bloggerurl).openStream()));
		String inputLine;
		StringBuffer sbfr = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			sbfr.append(inputLine);
		}

		in.close();
		System.out.println(sbfr);
		return retList;
	}

	private static String[] getIds(String html) {
		String blogId = "";
		blogId = html.substring(html.indexOf("blogID=") + "blogID=".length(),
				html.indexOf("&widgetType"));
		System.out.println(blogId);
		int count = html.indexOf("data-id='") + "data-id='".length();

		String postId = html.substring(count, (count + 19));
		System.out.println(postId);
		String[] idarr = new String[2];
		idarr[0] = blogId;
		idarr[1] = postId;
		return idarr;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UserAgent u = new UserAgent();
		try {
			// u.visit("http://digbysblog.blogspot.in/");//http://
			// thesartorialist.blogspot.com/
			// u.visit("http://googleblog.blogspot.in/");
			u
					.visit("http://googleblog.blogspot.in/2015/04/protect-your-google-account-with.html");
			String html = u.doc.innerHTML();
			// getIds(html);
			scrape(
					"http://googleblog.blogspot.in/2015/04/protect-your-google-account-with.html",
					html);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			u.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}

	}
}
