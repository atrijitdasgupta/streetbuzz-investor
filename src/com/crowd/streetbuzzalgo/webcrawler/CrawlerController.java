/**
 * 
 */
package com.crowd.streetbuzzalgo.webcrawler;

import com.crowd.streetbuzz.common.Constants;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * @author Atrijit
 *
 */
public class CrawlerController implements Constants{
	private static String seedurl;
	/**
	 * 
	 */
	public CrawlerController(String seedurl) {
		this.seedurl = seedurl;
		String [] strarr = new String[1];
		strarr[0]=seedurl;
		main(strarr);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RobotstxtConfig robotstxtConfig2 = new RobotstxtConfig();

	    System.out.println(robotstxtConfig2.getCacheSize());
	    System.out.println(robotstxtConfig2.getUserAgentName());

	    String crawlStorageFolder = BASESBSTORAGEPATH+"crawlstorage";
	    int numberOfCrawlers = 4;
	    CrawlConfig config = new CrawlConfig();
	    config.setCrawlStorageFolder(crawlStorageFolder);
	    config.setMaxDepthOfCrawling(4);

	    PageFetcher pageFetcher = new PageFetcher(config);
	    RobotstxtConfig robotstxtConfig = new RobotstxtConfig();

	    System.out.println(robotstxtConfig.getCacheSize());
	    System.out.println(robotstxtConfig.getUserAgentName());

	    RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
	    CrawlController controller = null;;
		try {
			controller = new CrawlController(config, 
			             pageFetcher, robotstxtServer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		seedurl = "http://www.somethingawful.com/";
		System.out.println(seedurl);
	    controller.addSeed(seedurl);
	    controller.start(Crawler.class, numberOfCrawlers);

	}

}
