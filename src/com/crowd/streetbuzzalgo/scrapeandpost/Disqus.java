/**
 * 
 */
package com.crowd.streetbuzzalgo.scrapeandpost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.crowd.streetbuzzalgo.crawler.vo.CrawlerVO;
import com.crowd.streetbuzzalgo.utils.DataClean;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.disqus.api.impl.Client;
import com.disqus.api.impl.ClientFactory;
import com.disqus.api.impl.impl.DisqusAPIClientModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * @author Atrijit
 * 
 */
public class Disqus implements ScrapeConstants {

	/**
	 * 
	 */
	public Disqus() {
		// TODO Auto-generated constructor stub
	}

	public static List scrape(String url, String forumShortName)
			throws Exception {
		List list = new ArrayList();
		final Injector injector = Guice
				.createInjector(new DisqusAPIClientModule(forumShortName));
		final ClientFactory clientFactory = injector
				.getInstance(ClientFactory.class);
		final Client threads = clientFactory
				.getInstance("http://disqus.com/api/3.0/threads/");
		final Client posts = clientFactory
				.getInstance("http://disqus.com/api/3.0/posts/");

		final Map<String, String> threadParams = new HashMap<String, String>();
		// threadParams.put("thread",
		// "link:http://www.ilikeplaces.com/page/Antananarivo_of_Madagascar?WOEID=2346150");
		threadParams.put("thread", ("link:" + url));
		final JSONObject threadJsonObject = threads.get("list", threadParams);
		// System.out.println(threadJsonObject);
		// System.out.println(threadJsonObject.getJSONArray("response").getJSONObject(0).get("id"));

		final Map<String, String> postParams = new HashMap<String, String>();
		postParams.put("thread", threadJsonObject.getJSONArray("response")
				.getJSONObject(0).get("id").toString());
		final JSONObject postJsonObject = posts.get("list", postParams);
		// System.out.println(postJsonObject);
		// System.out.println(postJsonObject.getJSONArray("response").getJSONObject(0));
		// System.out.println(postJsonObject.getJSONArray("response").getJSONObject(0).get("raw_message"));
		// List jsonList = postJsonObject.getJSONArray("response");
		int length = postJsonObject.getJSONArray("response").length();

		for (int i = 0; i < length; i++) {
			String comment = (String) postJsonObject.getJSONArray("response")
					.getJSONObject(i).get("raw_message");

			JSONObject jObject = postJsonObject.getJSONArray("response")
					.getJSONObject(i).getJSONObject("author");
			String temp = jObject.toString();
			// System.out.println("a. "+temp);
			String name = "";
			String avatar = "";
			if (temp.indexOf("isPrimary") > 0) {
				name = temp.substring((temp.lastIndexOf("name") + 6), temp
						.indexOf("reputation"));
				avatar = temp.substring((temp.indexOf("cache") + 10), (temp
						.indexOf("permalink") - 3));

			} else if (temp.indexOf("emailHash") > 0) {
				name = temp.substring((temp.lastIndexOf("name") + 6), temp
						.indexOf("isAnonymous"));
				avatar = temp.substring((temp.indexOf("cache") + 10), (temp
						.indexOf("permalink") - 3));

			}
			name = DataClean.clean(name);
			avatar = "http://" + avatar;
			//System.out.println("b. "+name);
			//System.out.println("c. "+avatar);
			CrawlerVO cvo = new CrawlerVO();
			cvo.setComment(comment);
			cvo.setUsername(name);
			cvo.setAvatar(avatar);
			cvo.setMode(StrUtil.getHost(url));
			list.add(cvo);

		}
		
		return list;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url = "http://www.theunrealtimes.com/2015/04/27/limerick-an-indian-007-remembers/";
		try {
			scrape(url, "theunrealtimes");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
