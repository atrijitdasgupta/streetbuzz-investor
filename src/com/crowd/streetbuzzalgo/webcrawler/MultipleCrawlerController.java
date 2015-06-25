/**
 * 
 */
package com.crowd.streetbuzzalgo.webcrawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * @author Atrijit
 *
 */
public class MultipleCrawlerController {
	private static Logger logger = LoggerFactory.getLogger(MultipleCrawlerController.class);

	/**
	 * 
	 */
	public MultipleCrawlerController() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		  if (args.length != 1) {
		      logger.info("Needed parameter: ");
		      logger.info("\t rootFolder (it will contain intermediate crawl data)");
		      return;
		    }

		    /*
		     * crawlStorageFolder is a folder where intermediate crawl data is
		     * stored.
		     */
		    String crawlStorageFolder = args[0];

		    CrawlConfig config1 = new CrawlConfig();
		    CrawlConfig config2 = new CrawlConfig();

		    /*
		     * The two crawlers should have different storage folders for their
		     * intermediate data
		     */
		    config1.setCrawlStorageFolder(crawlStorageFolder + "/crawler1");
		    config2.setCrawlStorageFolder(crawlStorageFolder + "/crawler2");

		    config1.setPolitenessDelay(1000);
		    config2.setPolitenessDelay(2000);

		    config1.setMaxPagesToFetch(50);
		    config2.setMaxPagesToFetch(100);

		    /*
		     * We will use different PageFetchers for the two crawlers.
		     */
		    PageFetcher pageFetcher1 = new PageFetcher(config1);
		    PageFetcher pageFetcher2 = new PageFetcher(config2);

		    /*
		     * We will use the same RobotstxtServer for both of the crawlers.
		     */
		    RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		    RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher1);
		    
		    CrawlController controller1 = null;
		    
		    CrawlController controller2 =  null;

		    try {
				controller1 = new CrawlController(config1, pageFetcher1, robotstxtServer);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    try {
				controller2 = new CrawlController(config2, pageFetcher2, robotstxtServer);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		    String[] crawler1Domains = new String[] { "http://www.ics.uci.edu/", "http://www.cnn.com/" };
		    String[] crawler2Domains = new String[] { "http://en.wikipedia.org/" };

		    controller1.setCustomData(crawler1Domains);
		    controller2.setCustomData(crawler2Domains);

		    controller1.addSeed("http://www.ics.uci.edu/");
		    controller1.addSeed("http://www.cnn.com/");
		    controller1.addSeed("http://www.ics.uci.edu/~lopes/");
		    controller1.addSeed("http://www.cnn.com/POLITICS/");

		    controller2.addSeed("http://en.wikipedia.org/wiki/Main_Page");
		    controller2.addSeed("http://en.wikipedia.org/wiki/Obama");
		    controller2.addSeed("http://en.wikipedia.org/wiki/Bing");

		    /*
		     * The first crawler will have 5 concurrent threads and the second
		     * crawler will have 7 threads.
		     */
		    controller1.startNonBlocking(BasicCrawler.class, 5);
		    controller2.startNonBlocking(BasicCrawler.class, 7);

		    controller1.waitUntilFinish();
		    logger.info("Crawler 1 is finished.");

		    controller2.waitUntilFinish();
		    logger.info("Crawler 2 is finished.");

	}

}
