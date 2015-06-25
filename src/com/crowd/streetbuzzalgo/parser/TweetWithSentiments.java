/**
 * 
 */
package com.crowd.streetbuzzalgo.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jinstagram.entity.comments.CommentData;
import org.jinstagram.entity.common.Caption;
import org.jinstagram.entity.common.Comments;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jsoup.Jsoup;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.helper.ParserResourceHelper;
import com.crowd.streetbuzz.model.ProtoVoicesDetails;
import com.crowd.streetbuzz.model.Voices;
import com.crowd.streetbuzzalgo.crawler.vo.CrawlerVO;
import com.crowd.streetbuzzalgo.lingpipe.LingPipe;
import com.crowd.streetbuzzalgo.scrapeandpost.TumblrGenericScrape;
import com.crowd.streetbuzzalgo.sentiwordnet.BagOfWordsSentiment;
import com.crowd.streetbuzzalgo.utils.DataClean;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.crowd.streetbuzzalgo.vo.SearchVO;
import com.crowd.streetbuzzalgo.vo.YoutubeVO;
import com.maruti.otterapi.search.Post;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

/**
 * @author Atrijit
 * 
 */
public class TweetWithSentiments implements Constants {

	/**
	 * 
	 */
	/*
	 * public TweetWithSentiments() { try { List tweetList = this.search("Jack
	 * Reacher"); if(tweetList!=null){ this.analyseSentiment(tweetList); } }
	 * catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } }
	 */
	public TweetWithSentiments() {

	}

	public List twitterSearch(List searchTermsList, List searchTermsListLocation) {
		List retList = new ArrayList();
		Twitter twitter = new TwitterFactory().getInstance();
		for (int i = 0; i < searchTermsList.size(); i++) {
			String querystr = (String) searchTermsList.get(i);
			List resultList = doSearch(querystr, twitter);
			if (resultList.size() > 0) {
				retList.addAll(resultList);
			}

		}

		for (int i = 0; i < searchTermsListLocation.size(); i++) {
			String querystr = (String) searchTermsListLocation.get(i);
			List resultList = doSearch(querystr, twitter);
			if (resultList.size() > 0) {
				retList.addAll(resultList);
			}
		}
		return retList;
	}

	public List<Status> doSearch(String querystr, Twitter twitter) {
		// System.out.println("Here: "+querystr);
		List<Status> tweets = new ArrayList();
		Query query = new Query(
				querystr
						+ " -filter:retweets -filter:links -filter:replies -filter:images");
		query.setCount(10);
		query.setLocale("en");
		query.setLang("en");
		QueryResult result = null;
		do {
			try {
				System.out.println("1");
				result = twitter.search(query);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("2");
				e.printStackTrace();
				return Collections.emptyList();
			}
			tweets = result.getTweets();
		} while ((query = result.nextQuery()) != null);

		if (tweets != null) {
			System.out.println("1");
			return tweets;
		} else {
			System.out.println("4");
			return Collections.emptyList();
		}
	}

	public List search(String querystr) throws Exception {
		Twitter twitter = new TwitterFactory().getInstance();
		List tweets = new ArrayList();
		// Query query = new Query(querystr);
		Query query = new Query(
				querystr
						+ " -filter:retweets -filter:links -filter:replies -filter:images");
		query.setCount(10);
		query.setLocale("en");
		query.setLang("en");
		QueryResult result;
		do {
			result = twitter.search(query);
			tweets = result.getTweets();
			for (int i=0;i<tweets.size();i++){
				Status status = (Status) tweets.get(i);
			}
		} while ((query = result.nextQuery()) != null);
		return tweets;

	}

	private StanfordCoreNLP getPipe() {
		ParserResourceHelper prh = ParserResourceHelper.getInstance();
		Map resourcesMap = prh.getResourcesMap();
		StanfordCoreNLP pipeline = (StanfordCoreNLP) resourcesMap
				.get("stanfordpipe");
		return pipeline;
	}

	public List analyseTweetSentiment(List tweetList, StanfordCoreNLP pipeline, LingPipe lingpipe)
			throws Exception {
		
		List retList = new ArrayList();
		
		for (int i = 0; i < tweetList.size(); i++) {
			SearchVO svo = (SearchVO) tweetList.get(i);
			String line = svo.getText();
			line = DataClean.clean(line);
			svo.setPositivephrase("");
			svo.setNegativephrase("");
			String sentimentstr = lingpipe.classify(line);
			int sentimentscore = lingpipe.getSentimentScore(sentimentstr);
			svo.setSentimentscore(sentimentscore);
			svo.setSentiment(sentimentstr);
			retList.add(svo);
		}
		return retList;
	}
	private static Map getPosNeg(String line){
		return BagOfWordsSentiment.findPosNegWords(line);
	}

	public List analyseOtterSentiment(List otterList, StanfordCoreNLP pipeline,
			String searchtopic,LingPipe lingpipe) throws Exception {
		// System.out.println("In here");
		// System.out.println("tweetList size: "+tweetList.size());
		List retList = new ArrayList();
		
		for (int i = 0; i < otterList.size(); i++) {
			Post post = (Post) otterList.get(i);
			String url = post.getUrl();
			String line = post.getContent();
			String author = post.getTrackback_author_name();
			// String authorimg = post.getTopsy_author_img();
			SearchVO svo = new SearchVO();
			svo.setPositivephrase("");
			svo.setNegativephrase("");
			svo.setText(line);
			svo.setCommenter(author);
			svo.setUrl(url);
			// svo.setCommentdate(post.getFirstpost_date());
			svo.setCommentdate(new Date());
			svo.setLocation("");
			svo.setSearchterm(searchtopic);
			svo.setMode(TWITTER);
			line = DataClean.clean(line);
			
			String sentimentstr = lingpipe.classify(line);
			int sentimentscore = lingpipe.getSentimentScore(sentimentstr);
			svo.setSentimentscore(sentimentscore);
			svo.setSentiment(sentimentstr);
			retList.add(svo);
			
		}
		return retList;
	}

	public List analyseYoutubeSentiment(List ytList, StanfordCoreNLP pipeline, LingPipe lingpipe)
			throws Exception {
		List retList = new ArrayList();
		for (int i = 0; i < ytList.size(); i++) {
			YoutubeVO ytv = (YoutubeVO) ytList.get(i);
			String line = ytv.getTitle() + ytv.getDescription();
			// System.out.println("Youtube: line is "+line);
			line = DataClean.clean(line);

			ytv.setPositivephrase("");
			ytv.setNegativephrase("");
			
			String videoid = ytv.getVideoId();
			
			List crawlerList = ytv.getCrawlerList();
			List senticrawlerList = new ArrayList();
			for (int j=0;j<crawlerList.size();j++){
				
				CrawlerVO cvo = (CrawlerVO)crawlerList.get(j);
				String comment = StrUtil.nonNull(cvo.getComment());
				comment = DataClean.clean(comment);
				cvo.setPositivephrase("");
				cvo.setNegativephrase("");
				String sentimentstr = lingpipe.classify(comment);
				int sentimentscore = lingpipe.getSentimentScore(sentimentstr);
				cvo.setSentimentscore(sentimentscore);
				cvo.setSentimentstr(sentimentstr);
				senticrawlerList.add(cvo);
			}
			String sentimentstr = lingpipe.classify(line);
			ytv.setSentimentrating(sentimentstr);
			ytv.setCrawlerList(senticrawlerList);
			retList.add(ytv);
		}
		return retList;
	}

	public List analyseTumblrSentiment(List tumblrList,
			StanfordCoreNLP pipeline, String searchtopic, LingPipe lingpipe) throws Exception {
		List retList = new ArrayList();
		for (int i = 0; i < tumblrList.size(); i++) {
			//System.out.println("Analysing tumblr sentiment: " + i + 1);
			com.tumblr.jumblr.types.Post p = (com.tumblr.jumblr.types.Post) tumblrList
					.get(i);
			SearchVO svo = new SearchVO();
			Long id = p.getId();
			String author = StrUtil.nonNull(p.getAuthorId());
			String blogname = StrUtil.nonNull(p.getBlogName());
			String posturl = StrUtil.nonNull(p.getPostUrl());
			String sourcetitle = "";
			org.jsoup.nodes.Document doc = null;
			try {
				doc = Jsoup.connect(posturl).get();
			} catch (Exception e1) {
				System.out.println(e1.getMessage());
			}
			
			if(doc!=null){
				try {
					sourcetitle = StrUtil.nonNull(doc.title());
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			
			String date = StrUtil.nonNull(p.getDateGMT());
			List tags = p.getTags();
			StringBuffer sbfr = new StringBuffer();
			for (int j=0;j<tags.size();j++){
				String temp = (String)tags.get(j);
				sbfr.append(temp+", ");
			}
			String tag = sbfr.toString();
			tag = tag.trim();
			Date dt  = new Date();
			try {
				dt = StrUtil.getTumblrDate(date);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				dt  = new Date();
			}
					
			svo.setCommentdate(dt);
			String domain = StrUtil.getDomain(posturl);
			svo.setCommenter(domain);
			svo.setSearchterm(searchtopic);
			svo.setUrl(posturl);
			svo.setText(sourcetitle);
			String thumbnail = StrUtil.getThumb(posturl);
			svo.setThumbnail(thumbnail);
			List crawlerList = new ArrayList();
			/*try {
				crawlerList = TumblrGenericScrape.scrape(posturl);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("TweetWitSentiments tumblr: "
						+ e.getMessage());
				crawlerList = new ArrayList();
			}*/
			List sentiCrawlerList = new ArrayList();
			if (crawlerList != null && crawlerList.size() > 0) {
				for (int j = 0; j < crawlerList.size(); j++) {
					CrawlerVO cvo = (CrawlerVO) crawlerList.get(j);
					String line = cvo.getComment();
					line = DataClean.clean(line);

					cvo.setPositivephrase("");
					cvo.setNegativephrase("");
								
					String sentimentstr = lingpipe.classify(line);
					int sentimentscore = lingpipe.getSentimentScore(sentimentstr);
					cvo.setSentimentscore(sentimentscore);
					cvo.setSentimentstr(sentimentstr);
					sentiCrawlerList.add(cvo);
				}
			} else {
				//System.out.println("crawlerList is null");
			}
			if (sentiCrawlerList != null && sentiCrawlerList.size() > 0) {
				svo.setCrawlerList(sentiCrawlerList);
			}

			retList.add(svo);

		}
		return retList;
	}

	public List analyseInstagramSentiment(List instagramList,
			StanfordCoreNLP pipeline, String searchtopic, LingPipe lingpipe) throws Exception {
		List retList = new ArrayList();
		for (int i = 0; i < instagramList.size(); i++) {
			MediaFeedData mfd = (MediaFeedData) instagramList.get(i);
			String link = StrUtil.nonNull(mfd.getLink());
			Caption caption = mfd.getCaption();
			String ctime = mfd.getCreatedTime();
			long ctimelong =  0;
			if(!"".equalsIgnoreCase(ctime)){
				ctimelong = new Long(ctime).longValue();
			}
			Date dt = new Date(ctimelong*1000);
			String owner = "";
			String time = "";
			String profilepic = "";
			String captiontext = "";
			String thumbnail = "";
			try {
				thumbnail = StrUtil.nonNull(StrUtil.getThumb(link));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if("".equalsIgnoreCase(thumbnail)){
				try {
					thumbnail = mfd.getImages().getThumbnail().getImageUrl();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
					thumbnail = "";
				}
			}
			
			if (caption != null) {
				owner = StrUtil.nonNull(caption.getFrom().getUsername());
				time = StrUtil.nonNull(caption.getCreatedTime());
				profilepic = StrUtil.nonNull(caption.getFrom()
						.getProfilePicture());
				captiontext = StrUtil.nonNull(caption.getText());
			}

			SearchVO svo = new SearchVO();
			svo.setCommenter(owner);
			svo.setSearchterm(searchtopic);
			svo.setText(captiontext);
			svo.setUrl(link);
			svo.setThumbnail(thumbnail);
			svo.setCommentdate(dt);
			svo.setProfilepic(profilepic);

			Comments comments = mfd.getComments();
			List sentiCrawlerList = new ArrayList();
			List commentsList = new ArrayList();
			if (comments != null) {
				commentsList = comments.getComments();
			}
			if (commentsList != null && commentsList.size() > 0) {
				for (int j = 0; j < commentsList.size(); j++) {
					CommentData cd = (CommentData) commentsList.get(j);
					String line = cd.getText();
					String createdtime = cd.getCreatedTime();
					long cmnttimelong =  0;
					if(!"".equalsIgnoreCase(ctime)){
						cmnttimelong = new Long(ctime).longValue();
					}
					Date dtcmnt = new Date(cmnttimelong*1000);
					String cname = cd.getCommentFrom().getUsername();
					String pic = cd.getCommentFrom().getProfilePicture();

					CrawlerVO cvo = new CrawlerVO();
					cvo.setAvatar(pic);
					cvo.setDate(dtcmnt);
					cvo.setComment(line);
					cvo.setUsername(cname);
					line = DataClean.clean(line);
					cvo.setPositivephrase("");
					cvo.setNegativephrase("");
					String sentimentstr = lingpipe.classify(line);
					int sentimentscore = lingpipe.getSentimentScore(sentimentstr);
					cvo.setSentimentscore(sentimentscore);
					cvo.setSentimentstr(sentimentstr);
					sentiCrawlerList.add(cvo);

				}
			}

			svo.setCrawlerList(sentiCrawlerList);

			retList.add(svo);

		}
		return retList;
	}

	public static Voices analyseSentimentVoices(Voices voices,
			StanfordCoreNLP pipeline, LingPipe lingpipe) throws Exception {
		String line = voices.getPosttext();
		/*String sentimentrating = MahoutCaller.getSentiments(line);
		int sentimentscore = MahoutCaller.getSentimentScore(sentimentrating);
		voices.setSentimentrating(sentimentrating);
		voices.setSentimentscore(new Long(sentimentscore));*/
		/*Annotation annotation = pipeline.process(line);
		for (CoreMap sentence : annotation
				.get(CoreAnnotations.SentencesAnnotation.class)) {
			Tree tree = sentence
					.get(SentimentCoreAnnotations.AnnotatedTree.class);
			int sentimentscore = RNNCoreAnnotations.getPredictedClass(tree);
			String sentimentrating = sentence
					.get(SentimentCoreAnnotations.ClassName.class);
			voices.setSentimentrating(sentimentrating);
			voices.setSentimentscore(new Long(sentimentscore));
		}*/
		/*PipeLineSingle pls = PipeLineSingle.getInstance();
		voices = pls.analyse(voices, line);
		return voices;*/
		String sentimentstr = lingpipe.classify(line);
		int sentimentscore = lingpipe.getSentimentScore(sentimentstr);
		voices.setSentimentrating(sentimentstr);
		voices.setSentimentscore(new Long(sentimentscore));
		return voices;

	}

	public static ProtoVoicesDetails analyseSentmentProtoVoices(
			ProtoVoicesDetails pvd, StanfordCoreNLP pipeline) throws Exception {
		String line = StrUtil.nonNull(pvd.getPosttext());

		Annotation annotation = pipeline.process(line);
		for (CoreMap sentence : annotation
				.get(CoreAnnotations.SentencesAnnotation.class)) {
			Tree tree = sentence
					.get(SentimentCoreAnnotations.AnnotatedTree.class);
			int sentimentscore = RNNCoreAnnotations.getPredictedClass(tree);
			String sentimentrating = sentence
					.get(SentimentCoreAnnotations.ClassName.class);
			pvd.setSentimentrating(sentimentrating);
			pvd.setSentimentscore(new Long(sentimentscore));
		}
		return pvd;
	}

	public ProtoVoicesDetails analyseSentmentProtoVoices(ProtoVoicesDetails pvd)
			throws Exception {
		String line = StrUtil.nonNull(pvd.getPosttext());
		if (line.length() > 200) {
			line = line.substring(0, 199);
		}
		System.out.println("line: " + line);
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		Annotation annotation = pipeline.process(line);
		for (CoreMap sentence : annotation
				.get(CoreAnnotations.SentencesAnnotation.class)) {
			Tree tree = sentence
					.get(SentimentCoreAnnotations.AnnotatedTree.class);
			int sentimentscore = RNNCoreAnnotations.getPredictedClass(tree);
			String sentimentrating = sentence
					.get(SentimentCoreAnnotations.ClassName.class);
			System.out.println("sentimentscore: " + sentimentscore);
			System.out.println("sentimentrating: " + sentimentrating);
			pvd.setSentimentrating(sentimentrating);
			pvd.setSentimentscore(new Long(sentimentscore));
		}

		return pvd;
	}
	
	public void analyseStringSentiment(String line){
		PipeLineSingle pls = PipeLineSingle.getInstance();
		pls.analyseStringSentiment(line);
	}

	public List analyseCrawlSentiment(List cList, StanfordCoreNLP pipeline, LingPipe lingpipe)
			throws Exception {
		// System.out.println("In here");
		// System.out.println("cList size: "+cList.size());
		List retList = new ArrayList();

		for (int i = 0; i < cList.size(); i++) {
			CrawlerVO cvo = (CrawlerVO) cList.get(i);
			String line = cvo.getComment();
			/*if (line.length() > 200) {
				line = line.substring(0, 199);
			}*/
//			posneg words
			Map posnegmap = getPosNeg(line);
			List plist = (List)posnegmap.get(positivemaptag);
			List nlist = (List)posnegmap.get(negativemaptag);
			if(plist!=null && plist.size()>0){
				String positivephrase = (String)plist.get(0);
				cvo.setPositivephrase(positivephrase);
			}
			if(nlist!=null && nlist.size()>0){
				String negativephrase = (String)nlist.get(0);
				cvo.setNegativephrase(negativephrase);
			}
			/*String sentimentStr = MahoutCaller.getSentiments(line);
			int sentimentscore = MahoutCaller.getSentimentScore(sentimentStr);
			cvo.setSentimentstr(sentimentStr);
			cvo.setSentimentscore(sentimentscore);
			retList.add(cvo);*/
			/*Annotation annotation = pipeline.process(line);
			for (CoreMap sentence : annotation
					.get(CoreAnnotations.SentencesAnnotation.class)) {
				Tree tree = sentence
						.get(SentimentCoreAnnotations.AnnotatedTree.class);
				int sentimentscore = RNNCoreAnnotations.getPredictedClass(tree);
				String sentimentStr = sentence
						.get(SentimentCoreAnnotations.ClassName.class);
				cvo.setSentimentstr(sentimentStr);
				cvo.setSentimentscore(sentimentscore);
				retList.add(cvo);
				
			}*/
			/*PipeLineSingle pls = PipeLineSingle.getInstance();
			cvo = pls.analyse(cvo, line);
			retList.add(cvo);*/
			
			String sentimentstr = lingpipe.classify(line);
			int sentimentscore = lingpipe.getSentimentScore(sentimentstr);
			cvo.setSentimentscore(sentimentscore);
			cvo.setSentimentstr(sentimentstr);
			retList.add(cvo);
		}
		return retList;
	}

	public void analyseSentiment(List tweetList) throws Exception {

		StanfordCoreNLP pipeline = getPipe();
		for (int i = 0; i < tweetList.size(); i++) {
			Status temp = (Status) tweetList.get(i);
			String line = temp.getText();
			System.out.println("line: " + line);
			// line = DataClean.clean(line);
			Annotation annotation = pipeline.process(line);
			for (CoreMap sentence : annotation
					.get(CoreAnnotations.SentencesAnnotation.class)) {
				Tree tree = sentence
						.get(SentimentCoreAnnotations.AnnotatedTree.class);
				int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
				String sentimentStr = sentence
						.get(SentimentCoreAnnotations.ClassName.class);
				String partText = sentence.toString();
				// System.out.println("Input: "+line+", sentiment numeric value:
				// "+sentiment+", sentiment absolute value: "+sentimentStr);
			}
		}
	}

	public SearchVO analyseSentimentforSVO(SearchVO svo,
			StanfordCoreNLP pipeline) {
		/*
		 * Properties props = new Properties(); props.setProperty("annotators",
		 * "tokenize, ssplit, parse, sentiment"); StanfordCoreNLP pipeline = new
		 * StanfordCoreNLP(props);
		 */
		// StanfordCoreNLP pipeline = getPipe();
		String line = StrUtil.nonNull(svo.getText());
		int mainSentiment = 0;
		String mainSentimentStr = "";
		if (line != null && line.length() > 0) {
			Annotation annotation = pipeline.process(line);
			for (CoreMap sentence : annotation
					.get(CoreAnnotations.SentencesAnnotation.class)) {
				Tree tree = sentence
						.get(SentimentCoreAnnotations.AnnotatedTree.class);
				int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
				String sentimentStr = sentence
						.get(SentimentCoreAnnotations.ClassName.class);
				svo.setSentiment(sentimentStr);
				svo.setSentimentscore(sentiment);
			}

		}
		return svo;
	}

	public void analyseSentiment(String line, StanfordCoreNLP pipeline)
			throws Exception {
		/*
		 * Properties props = new Properties(); props.setProperty("annotators",
		 * "tokenize, ssplit, parse, sentiment"); StanfordCoreNLP pipeline = new
		 * StanfordCoreNLP(props);
		 */
		// StanfordCoreNLP pipeline = getPipe();
		int mainSentiment = 0;
		String mainSentimentStr = "";
		if (line != null && line.length() > 0) {
			int longest = 0;
			Annotation annotation = pipeline.process(line);
			for (CoreMap sentence : annotation
					.get(CoreAnnotations.SentencesAnnotation.class)) {
				Tree tree = sentence
						.get(SentimentCoreAnnotations.AnnotatedTree.class);
				int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
				String sentimentStr = sentence
						.get(SentimentCoreAnnotations.ClassName.class);
				System.out.println("sentiment numeric value: " + sentiment
						+ ", sentiment absolute value: " + sentimentStr);
				/*
				 * String partText = sentence.toString(); if (partText.length() >
				 * longest) { mainSentiment = sentiment; mainSentimentStr =
				 * sentimentStr; longest = partText.length(); }
				 */
			}
		}
		/*
		 * if (mainSentiment == 2 || mainSentiment > 4 || mainSentiment < 0) {
		 * System.out.println("Some issue there, mainSentment value is
		 * "+mainSentiment); }else{ System.out.println("Input: "+line+",
		 * sentiment numeric value: "+mainSentiment+", sentiment absolute value:
		 * "+mainSentimentStr); }
		 */
		// System.out.println("Input: "+line+", sentiment numeric value:
		// "+mainSentiment+", sentiment absolute value: "+mainSentimentStr);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new TweetWithSentiments().search("pizza");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
