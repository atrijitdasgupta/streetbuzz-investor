/**
 * 
 */
package com.crowd.streetbuzzalgo.webcrawler;

import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * @author Atrijit
 *
 */
public class BasicCrawler extends WebCrawler{
	 private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
		      + "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
	 private String[] myCrawlDomains;
	 private Logger logger = LoggerFactory.getLogger(BasicCrawler.class);
	/**
	 * 
	 */
	public BasicCrawler() {
		// TODO Auto-generated constructor stub
	}
	public void onStart() {
	    myCrawlDomains = (String[]) myController.getCustomData();
	  }
	
	public boolean shouldVisit(Page page, WebURL url) {
	    String href = url.getURL().toLowerCase();
	    if (FILTERS.matcher(href).matches()) {
	      return false;
	    }

	    for (String crawlDomain : myCrawlDomains) {
	      if (href.startsWith(crawlDomain)) {
	        return true;
	      }
	    }

	    return false;
	  }
	
	 public void visit(Page page) {
		    int docid = page.getWebURL().getDocid();
		    String url = page.getWebURL().getURL();
		    int parentDocid = page.getWebURL().getParentDocid();

		    logger.debug("Docid: {}", docid);
		    logger.info("URL: {}", url);
		    logger.debug("Docid of parent page: {}", parentDocid);

		    if (page.getParseData() instanceof HtmlParseData) {
		      HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
		      String text = htmlParseData.getText();
		      String html = htmlParseData.getHtml();
		      List<WebURL> links = htmlParseData.getOutgoingUrls();

		      logger.debug("Text length: {}", text.length());
		      logger.debug("Html length: {}", html.length());
		      logger.debug("Number of outgoing links: {}", links.size());
		    }

		    logger.debug("=============");
		  }


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
