/**
 * 
 */
package com.crowd.streetbuzz.webservicecontroller;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzzalgo.crawler.vo.CrawlerVO;
import com.crowd.streetbuzzalgo.lingpipe.LingPipe;
import com.crowd.streetbuzzalgo.parser.EntityParser;
import com.crowd.streetbuzzalgo.parser.vo.StanfordNerVO;
import com.crowd.streetbuzzalgo.scrapeandpost.ScrapePostFactory;
import com.crowd.streetbuzzalgo.topsy.Otter;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.crowd.streetbuzzalgo.webhose.WebHose;
import com.crowd.streetbuzzalgo.webhose.WebHoseVO;
import com.maruti.otterapi.search.Post;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

/**
 * @author Atrijit
 * 
 */
public class SearchReadAnalyseController implements Controller, Constants {
	private String returnView = "";
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List list = new ArrayList();
		list.add("Samsung Galaxy");
		list.add("Foxconn");
		list.add("Sky Diving");
		list.add("CNN");
		list.add("Nasa");
		list.add("Shahrukh Khan");
		list.add("Jazz");
		list.add("Nargis");
		list.add("Desert");
		list.add("Narendra Modi");
		list.add("Black Hole");
		list.add("Maradona");
		list.add("Tintin");
		list.add("Deadpool");
		list.add("Jazz");
		list.add("Nepal Earthquake");
		
		List results = Otter.callTemp(list);
		System.setOut(new PrintStream(new BufferedOutputStream(
				new FileOutputStream(BASESBSTORAGEPATH+"longtext/TWITTER.txt"))));
		for (int i=0;i<results.size();i++){
			Post post = (Post)results.get(i);
			String text = StrUtil.nonNull(post.getContent());
			System.out.println(text);
			System.out.println("\n");
		}
		
		return new ModelAndView(returnView);
	}
	
	public ModelAndView handleRequestJust(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		WebHose wh = new WebHose();
		return new ModelAndView(returnView);
	}

	public ModelAndView handleRequestOlder(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String topic = StrUtil.nonNull(request.getParameter("topic"));
		long zero = System.currentTimeMillis();
		List bloglist = WebHose.search(topic, "blog");
		long one = System.currentTimeMillis();
		System.out.println("TIME TAKEN: "+((one-zero)/1000)+" SECONDS AND SIZE OF LIST: "+bloglist.size());
		String serializedClassifier = BASEEXTLIBPATH
				+ "stanfordcorelib/english.all.3class.distsim.crf.ser.gz";
		AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier
				.getClassifierNoExceptions(serializedClassifier);
		for (int i = 0; i < bloglist.size(); i++) {
			WebHoseVO whv = (WebHoseVO) bloglist.get(i);
			String text = StrUtil.nonNull(whv.getText());
			StanfordNerVO snvo = EntityParser.coreparseAlt(text,classifier);
			List personlist = snvo.getPerson();
			List locationlist = snvo.getLocation();
			List orglist = snvo.getOrganization();
			
			System.out.println(personlist);
			System.out.println(locationlist);
			System.out.println(orglist);
			

		}
		long two = System.currentTimeMillis();
		System.out.println("TIME TAKEN: "+((two-one)/1000)+" SECONDS");
		return new ModelAndView(returnView);
	}

	public ModelAndView handleRequestOldest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String topic = StrUtil.nonNull(request.getParameter("topic"));

		Long start = System.currentTimeMillis();
		List bloglist = WebHose.search(topic, "blog");
		Long one = System.currentTimeMillis();
		System.out.println("Time to do blog search: " + ((one - start) / 1000)
				+ " seconds and size of list: " + bloglist.size());
		List discussionslist = WebHose.search(topic, "discussions");
		Long two = System.currentTimeMillis();
		System.out.println("Time to do discussions search: "
				+ ((two - one) / 1000) + " seconds and size of list: "
				+ discussionslist.size());

		// not to be scraped for comments

		List fbloglist = filterBlogs(bloglist, topic);
		Long three = System.currentTimeMillis();
		System.out.println("Time to filter blog list: "
				+ ((three - two) / 1000)
				+ " seconds and size of filtered list: " + fbloglist.size());
		// to be scraped for comments
		List fdiscussionslist = filterDiscussions(discussionslist, topic);
		Long four = System.currentTimeMillis();
		System.out.println("Time to filter discussions list: "
				+ ((four - three) / 1000)
				+ " seconds and size of filtered list: "
				+ fdiscussionslist.size());
		List scrapedfdiscussionslist = new ArrayList();
		for (int i = 0; i < fdiscussionslist.size(); i++) {
			WebHoseVO whv = (WebHoseVO) fdiscussionslist.get(i);
			String url = whv.getUrl();
			List crawlerList = new ArrayList();
			try {
				crawlerList = ScrapePostFactory.scrape(url);
			} catch (Exception e) {

			}
			whv.setCrawlerList(crawlerList);
			scrapedfdiscussionslist.add(whv);
		}
		Long five = System.currentTimeMillis();
		System.out.println("Time to extract comments from  discussions list: "
				+ ((five - four) / 1000) + " seconds.");

		// Sentiment Analysis Run
		LingPipe lingpipe = new LingPipe();
		Long six = System.currentTimeMillis();
		System.out.println("Time to instantiate Lingpipe: "
				+ ((six - five) / 1000) + " seconds.");
		// First run for blogs
		for (int i = 0; i < fbloglist.size(); i++) {
			WebHoseVO whv = (WebHoseVO) fbloglist.get(i);
			String text = StrUtil.nonNull(whv.getText());
			String sentiment = lingpipe.classify(text);
			System.out.println("sentiment: " + sentiment);
			int score = lingpipe.getSentimentScore(sentiment);
		}
		Long seven = System.currentTimeMillis();
		System.out.println("Time to sentiment analyse blog list: "
				+ ((seven - six) / 1000) + " seconds.");
		// Second run for discussions
		for (int i = 0; i < scrapedfdiscussionslist.size(); i++) {
			WebHoseVO whv = (WebHoseVO) scrapedfdiscussionslist.get(i);
			String text = StrUtil.nonNull(whv.getText());
			String sentiment = lingpipe.classify(text);
			System.out.println("sentiment: " + sentiment);
			int score = lingpipe.getSentimentScore(sentiment);
			List crawlerList = whv.getCrawlerList();
			for (int j = 0; j < crawlerList.size(); j++) {
				CrawlerVO vo = (CrawlerVO) crawlerList.get(j);
				String comment = vo.getComment();
				String sentimentcomment = lingpipe.classify(comment);
				System.out.println("sentimentcomment: " + sentimentcomment);
				int scorecomment = lingpipe.getSentimentScore(sentimentcomment);
			}
		}

		Long eight = System.currentTimeMillis();
		System.out.println("Time to sentiment analyse discussions list: "
				+ ((eight - seven) / 1000) + " seconds.");

		return new ModelAndView(returnView);
	}

	private List filterBlogs(List bloglist, String topic) {
		/*
		 * System.out.println("filterBlogs"); Pattern p =
		 * Pattern.compile("blog|blogger|wordpress|tumblr|blogspot|medium|typepad|svbtle|livejournal|weebly|postach.io|pen.io|ghost");
		 * topic = topic.toLowerCase(); Pattern topicp = Pattern.compile(topic);
		 * List retList = new ArrayList(); for (int i=0;i<bloglist.size();i++){
		 * WebHoseVO whv = (WebHoseVO)bloglist.get(i); String url =
		 * whv.getUrl(); System.out.println(url); String text = whv.getText();
		 * List sentences = StrUtil.breakPara(text); if(sentences!=null &&
		 * sentences.size()>0){ text = (String)sentences.get(0);
		 * System.out.println(text); } boolean matches = blogmatch(url,text,
		 * p,topicp); if(matches){ retList.add(whv); } } return retList;
		 */
		return bloglist;
	}

	private List filterDiscussions(List discussionslist, String topic) {
		/*
		 * System.out.println("filterDiscussions"); Pattern p =
		 * Pattern.compile("reddit|digg|delicious|stumbleupon|fark|slashdot|metafilter|straightdope");
		 * topic = topic.toLowerCase(); Pattern topicp = Pattern.compile(topic);
		 * List retList = new ArrayList(); for (int i=0;i<discussionslist.size();i++){
		 * WebHoseVO whv = (WebHoseVO)discussionslist.get(i); String url =
		 * whv.getUrl(); System.out.println(url); String text = whv.getText();
		 * List sentences = StrUtil.breakPara(text); if(sentences!=null &&
		 * sentences.size()>0){ text = (String)sentences.get(0);
		 * System.out.println(text); } boolean matches = discussionsmatch(url,
		 * text,p,topicp); if(matches){ retList.add(whv); } } return retList;
		 */
		return discussionslist;
	}

	private boolean blogmatch(String url, String line, Pattern p, Pattern topicp) {
		Matcher m = p.matcher(url);
		if (!m.find()) {
			return false;
		} else {
			Matcher topicm = topicp.matcher(line);
			if (!topicm.find()) {
				return false;
			}
		}
		return true;
	}

	private boolean discussionsmatch(String url, String line, Pattern p,
			Pattern topicp) {
		Matcher m = p.matcher(url);
		if (!m.find()) {
			return false;
		} else {
			Matcher topicm = topicp.matcher(line);
			if (!topicm.find()) {
				return false;
			}
		}
		return true;
	}
}
