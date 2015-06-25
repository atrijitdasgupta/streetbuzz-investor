/**
 * 
 */
package com.crowd.streetbuzzalgo.scrapeandpost;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Atrijit
 *
 */
public class FunnyCrawler {
	private static Pattern patternDomainName;
	  private Matcher matcher;
	  private static final String DOMAIN_NAME_PATTERN 
		= "([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}";
	  static {
		patternDomainName = Pattern.compile(DOMAIN_NAME_PATTERN);
	  }
	/**
	 * 
	 */
	public FunnyCrawler() {
		// TODO Auto-generated constructor stub
	}
	
	public String getDomainName(String url){
		 
		String domainName = "";
		matcher = patternDomainName.matcher(url);
		if (matcher.find()) {
			domainName = matcher.group(0).toLowerCase().trim();
		}
		return domainName;
	 
	  }
	public List getDataFromGoogle(String query) {
		 
		List result = new ArrayList();	
		String request = "https://www.google.com/search?q=" + query + "&num=20";
		System.out.println("Sending request..." + request);
	 
		try {
	 
			// need http protocol, set this as a Google bot agent :)
			Document doc = Jsoup
				.connect(request)
				.userAgent(
				  "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
				.timeout(5000).get();
	 
			// get all links
			Elements links = doc.select("a[href]");
			for (Element link : links) {
	 
				String temp = link.attr("href");
				//System.out.println(temp);
				if(temp.startsWith("/url?q=")){
	                //use regex to get domain name
					/*String url = getDomainName(temp);
					if(!url.startsWith("webcache")){
						//url = "http://"+ url.substring(0, url.indexOf("&sa"));
						result.add(url);
					}*/
					if(temp.indexOf("webcache")<0){
						int size = "/url?q=".length();
						temp = temp.substring(size, temp.length());
						if(temp.indexOf("&sa")>0){
							temp = temp.substring(0, temp.indexOf("&sa"));
						//	System.out.println(temp);
							result.add(temp);
						}
						
					}
					
					
				}
	 
			}
	 
		} catch (IOException e) {
			e.printStackTrace();
		}
	 
		return result;
	  }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FunnyCrawler obj = new FunnyCrawler();
		List result = obj.getDataFromGoogle("butterfly");
		for (int i=0;i<result.size();i++){
			String temp = (String)result.get(i);
			System.out.println(temp);
		}
		System.out.println(result.size());

	}

}
