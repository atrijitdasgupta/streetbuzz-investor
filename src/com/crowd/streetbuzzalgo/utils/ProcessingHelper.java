/**
 * 
 */
package com.crowd.streetbuzzalgo.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import org.hibernate.HibernateException;

import com.aliasi.util.ScoredObject;
import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.VoicesDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDetailsDAO;
import com.crowd.streetbuzz.dao.implementation.WordCloudStoreDAO;
import com.crowd.streetbuzz.model.ConversationCard;
import com.crowd.streetbuzz.model.SearchCard;
import com.crowd.streetbuzz.model.Voices;
import com.crowd.streetbuzz.model.VoicesDetails;
import com.crowd.streetbuzz.model.WordCloudStore;
import com.crowd.streetbuzzalgo.crawler.vo.CrawlerVO;
import com.crowd.streetbuzzalgo.faroosearch.vo.FarooResult;
import com.crowd.streetbuzzalgo.googlesearch.vo.Result;
import com.crowd.streetbuzzalgo.lingpipe.InterestingPhrases;
import com.crowd.streetbuzzalgo.vo.SearchDemoVO;
import com.crowd.streetbuzzalgo.vo.SearchVO;
import com.crowd.streetbuzzalgo.vo.YoutubeVO;
import com.crowd.streetbuzzalgo.webhose.WebHoseVO;
import com.google.api.client.util.DateTime;

/**
 * @author Atrijit
 * 
 */
public class ProcessingHelper implements Constants {

	/**
	 * 
	 */
	public ProcessingHelper() {
		// TODO Auto-generated constructor stub
	}

	public static ConversationCard spotateRefreshConversationCard(
			ConversationCard cc, List sentimentalTwitterList,
			List sentimentalOtterList, Map analysedMap, VoicesDAO voicesDAO,
			VoicesDetailsDAO voicesDetailsDAO, List YoutubeList,
			List imagesList, List sentimentalTumblrList,
			List sentimentalInstagramList,List analysedWebHoseBlogList,List analysedWebHoseDiscussionList,WordCloudStoreDAO wordCloudStoreDAO) {
		// CLEANUP FIRST
		// Clean Up Twitter
		List twitvlist = voicesDAO.getAllRecordsbyCardIdChannel(cc.getId(),
				TWITTERSPP);

		List filteredSentimentalTwitterList = new ArrayList();

		if (sentimentalTwitterList != null && sentimentalTwitterList.size() > 0) {
			for (int i = 0; i < sentimentalTwitterList.size(); i++) {
				SearchVO svotwit = (SearchVO) sentimentalTwitterList.get(i);
				String twitT = StrUtil.nonNull(svotwit.getText());
				twitT = twitT.trim();
				twitT = DataClean.htmlClean(twitT);
				twitT = twitT.toLowerCase();

				String nameT = svotwit.getCommenter();
				nameT = nameT.trim();
				nameT = nameT.toLowerCase();
				for (int j = 0; j < twitvlist.size(); j++) {
					Voices tv = (Voices) twitvlist.get(j);
					String posttext = tv.getPosttext();
					posttext = posttext.trim();
					posttext = posttext.toLowerCase();

					String posttextauthor = tv.getPosttextauthor();
					posttextauthor = posttextauthor.trim();
					posttextauthor = posttextauthor.toLowerCase();
					if (!twitT.equalsIgnoreCase(posttext)
							&& !nameT.equalsIgnoreCase(posttextauthor)) {
						filteredSentimentalTwitterList.add(svotwit);
					}
				}
			}
		}

		// Clean Up Otter
		List filteredSentimentalOttererList = new ArrayList();

		if (sentimentalOtterList != null && sentimentalOtterList.size() > 0) {
			for (int i = 0; i < sentimentalOtterList.size(); i++) {
				SearchVO svotwitotter = (SearchVO) sentimentalOtterList.get(i);
				String twitT = StrUtil.nonNull(svotwitotter.getText());
				twitT = twitT.trim();
				twitT = DataClean.htmlClean(twitT);
				twitT = twitT.toLowerCase();

				String nameT = svotwitotter.getCommenter();
				nameT = nameT.trim();
				nameT = nameT.toLowerCase();

				for (int j = 0; j < twitvlist.size(); j++) {
					Voices tv = (Voices) twitvlist.get(j);
					String posttext = tv.getPosttext();
					posttext = posttext.trim();
					posttext = posttext.toLowerCase();

					String posttextauthor = tv.getPosttextauthor();
					posttextauthor = posttextauthor.trim();
					posttextauthor = posttextauthor.toLowerCase();
					if (!twitT.equalsIgnoreCase(posttext)
							&& !nameT.equalsIgnoreCase(posttextauthor)) {
						filteredSentimentalOttererList.add(svotwitotter);
					}
				}
			}
		}

		// Clean Up Youtube
		List ytvlist = voicesDAO.getAllRecordsbyCardIdChannel(cc.getId(),
				YOUTUBESPP);
		List filteredSentimentalYoutubeList = new ArrayList();
		if (YoutubeList != null && YoutubeList.size() > 0) {
			for (int i = 0; i < YoutubeList.size(); i++) {
				YoutubeVO ytvo = (YoutubeVO) YoutubeList.get(i);
				String youtubevideoid = ytvo.getVideoId();
				for (int j = 0; j < ytvlist.size(); j++) {
					Voices ytv = (Voices) ytvlist.get(j);
					String postid = ytv.getPostid();
					if (!postid.equalsIgnoreCase(youtubevideoid)) {
						filteredSentimentalYoutubeList.add(ytvo);
					}
				}
			}
		}

		// Clean up Tumblr
		List tumblrList = voicesDAO.getAllRecordsbyCardIdChannel(cc.getId(),
				TUMBLRSPP);
		List filteredSentimentalTumblrList = new ArrayList();
		if (sentimentalTumblrList != null && sentimentalTumblrList.size() > 0) {
			for (int i = 0; i < sentimentalTumblrList.size(); i++) {
				SearchVO svo = (SearchVO) sentimentalTumblrList.get(i);
				String url = StrUtil.nonNull(svo.getUrl());
				for (int j = 0; j < tumblrList.size(); j++) {
					Voices tVoices = (Voices) tumblrList.get(j);
					String sourcelink = tVoices.getSourcelink();
					if (!sourcelink.equalsIgnoreCase(url)) {
						filteredSentimentalTumblrList.add(svo);
					}
				}
			}

		}

		// Clean up Instagram
		List instaList = voicesDAO.getAllRecordsbyCardIdChannel(cc.getId(),
				INSTAGRAMSPP);
		List filteredSentimentalInstagramList = new ArrayList();
		if (sentimentalInstagramList != null
				&& sentimentalInstagramList.size() > 0) {
			for (int i = 0; i < sentimentalInstagramList.size(); i++) {
				SearchVO svo = (SearchVO) sentimentalInstagramList.get(i);
				String url = StrUtil.nonNull(svo.getUrl());
				for (int j = 0; j < instaList.size(); j++) {
					Voices iVoices = (Voices) instaList.get(j);
					String sourcelink = iVoices.getSourcelink();
					if (!sourcelink.equalsIgnoreCase(url)) {
						filteredSentimentalInstagramList.add(svo);
					}
				}
			}

		}
		// Clean up Web
		List allotherlist = voicesDAO.getAllRecordsbyCardId(cc.getId());
		Map filteredAnalysedMap = new HashMap();
		Set keyset = analysedMap.keySet();
		Iterator it = keyset.iterator();
		while (it.hasNext()) {
			SearchDemoVO sdvo = (SearchDemoVO) it.next();
			String url = sdvo.getUrl();
			for (int j = 0; j < allotherlist.size(); j++) {
				Voices wVoices = (Voices) allotherlist.get(j);
				String sourcelink = wVoices.getSourcelink();
				if (!sourcelink.equalsIgnoreCase(url)) {
					List cList = (List) analysedMap.get(sdvo);
					filteredAnalysedMap.put(sdvo, cList);
				}
			}
		}
		cc = spotateLatestConversationCard(cc, filteredSentimentalTwitterList,
				filteredSentimentalOttererList, filteredAnalysedMap, voicesDAO,
				voicesDetailsDAO, filteredSentimentalYoutubeList, imagesList,
				filteredSentimentalTumblrList, filteredSentimentalInstagramList,analysedWebHoseBlogList,analysedWebHoseDiscussionList,wordCloudStoreDAO);
		return cc;
	}

	public static ConversationCard spotateLatestConversationCard(
			ConversationCard cc, List sentimentalTwitterList,
			List sentimentalOtterList, Map analysedMap, VoicesDAO voicesDAO,
			VoicesDetailsDAO voicesDetailsDAO, List YoutubeList,
			List imagesList, List sentimentalTumblrList,
			List sentimentalInstagramList, List analysedWebHoseBlogList,List analysedWebHoseDiscussionList,WordCloudStoreDAO wordCloudStoreDAO) {
		long start = System.currentTimeMillis();
		List existingVoicesList = voicesDAO.getAllRecordsbyCardId(cc.getId());
		// Process one by one

		// Twitter from twitter
		if (sentimentalTwitterList != null && sentimentalTwitterList.size() > 0) {
			for (int i = 0; i < sentimentalTwitterList.size(); i++) {
				SearchVO svotwit = (SearchVO) sentimentalTwitterList.get(i);
				String twitT = StrUtil.nonNull(svotwit.getText());
				twitT = twitT.trim();
				twitT = DataClean.htmlClean(twitT);
				if ("".equalsIgnoreCase(twitT)) {
					continue;
				}
				Voices tv = new Voices();

				String author = cc.getAuthor();
				tv.setAuthor(author);

				Long cardid = cc.getId();
				tv.setCardid(cardid);

				tv.setCardtype(CONVERSATIONTYPE);

				String carduniqueid = cc.getUniqueid();
				tv.setCarduniqueid(carduniqueid);
				
				String nameT = StrUtil.nonNull(svotwit.getCommenter());
				if(!"".equalsIgnoreCase(nameT)){
					tv.setSource(nameT);
				}else{
					tv.setSource(TWITTER);
				}
				
				tv.setChannel(TWEETCH);

				String uniquevoiceid = StrUtil.getUniqueId();
				tv.setUniquevoiceid(uniquevoiceid);

				String userid = cc.getUserid();
				tv.setUserid(new Long(userid));

				tv.setVoicesdate(new Date());
				tv.setVoicetype(TWITTER);

				String url = svotwit.getUrl();
				tv.setSourcelink(url);

				String negativephrase = svotwit.getNegativephrase();
				tv.setNegativephrase(negativephrase);

				String neutralphrase = svotwit.getNeutralphrase();
				tv.setNeutralphrase(neutralphrase);

				String positivephrase = svotwit.getPositivephrase();
				tv.setPositivephrase(positivephrase);

				String nameidT = svotwit.getCommenterid();
				tv.setPostauthorid(nameidT);

				// String twitT = svotwit.getText();
				tv.setPosttext(twitT);

				
				tv.setPosttextauthor(nameT);

				String sentimentT = svotwit.getSentiment();
				tv.setSentimentrating(sentimentT);

				int sentimentratingT = svotwit.getSentimentscore();
				tv.setSentimentscore(new Long(sentimentratingT));

				tv.setCancomment(true);

				try {
					voicesDAO.addOrUpdateRecord(tv);
				} catch (HibernateException e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
					continue;
				}
			}
		}
		List twitwordcloud = new ArrayList();
		// Twitter from Otter
		if (sentimentalOtterList != null && sentimentalOtterList.size() > 0) {
			for (int i = 0; i < sentimentalOtterList.size(); i++) {
				SearchVO svotwitotter = (SearchVO) sentimentalOtterList.get(i);
				String twitT = StrUtil.nonNull(svotwitotter.getText());
				twitT = twitT.trim();
				twitT = DataClean.htmlClean(twitT);
				if ("".equalsIgnoreCase(twitT)) {
					continue;
				}
				Voices tv = new Voices();

				String author = cc.getAuthor();
				tv.setAuthor(author);

				Long cardid = cc.getId();
				tv.setCardid(cardid);

				tv.setCardtype(CONVERSATIONTYPE);

				String carduniqueid = cc.getUniqueid();
				tv.setCarduniqueid(carduniqueid);
				
				String nameT = StrUtil.nonNull(svotwitotter.getCommenter());
				if(!"".equalsIgnoreCase(nameT)){
					tv.setSource(nameT);
				}else{
					tv.setSource(TWITTER);
				}
				
				tv.setChannel(TWEETCH);

				String uniquevoiceid = StrUtil.getUniqueId();
				tv.setUniquevoiceid(uniquevoiceid);

				String userid = cc.getUserid();
				tv.setUserid(new Long(userid));

				tv.setVoicesdate(new Date());
				tv.setVoicetype(TWITTER);

				String url = svotwitotter.getUrl();
				tv.setSourcelink(url);

				String negativephrase = svotwitotter.getNegativephrase();
				tv.setNegativephrase(negativephrase);

				String neutralphrase = svotwitotter.getNeutralphrase();
				tv.setNeutralphrase(neutralphrase);

				String positivephrase = svotwitotter.getPositivephrase();
				tv.setPositivephrase(positivephrase);

				String nameidT = svotwitotter.getCommenterid();
				tv.setPostauthorid(nameidT);

				tv.setPosttext(twitT);
				String twitTtemp = twitT.toLowerCase();
				if(!twitwordcloud.contains(twitTtemp)){
					twitwordcloud.add(twitTtemp);
				}
				
				
				tv.setPosttextauthor(nameT);

				String sentimentT = svotwitotter.getSentiment();
				tv.setSentimentrating(sentimentT);

				int sentimentratingT = svotwitotter.getSentimentscore();
				tv.setSentimentscore(new Long(sentimentratingT));

				tv.setCancomment(true);

				try {
					voicesDAO.addOrUpdateRecord(tv);
				} catch (HibernateException e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
					continue;
				}
			}
		}
		List blogwordcloud = new ArrayList();
		//Webhose Blogs
		if(analysedWebHoseBlogList!=null && analysedWebHoseBlogList.size()>0){
			for(int i=0;i<analysedWebHoseBlogList.size();i++){
				WebHoseVO whv = (WebHoseVO)analysedWebHoseBlogList.get(i);
				String author = StrUtil.nonNull(whv.getAuthor());
				String sentiment = StrUtil.nonNull(whv.getSentiment());
				Date dt = whv.getDate();
				String title = StrUtil.nonNull(whv.getTitle());
				String url = StrUtil.nonNull(whv.getUrl());
				if(blogwordcloud.size()<11){
					String text = StrUtil.nonNull(whv.getText());
					String texttemp = text.toLowerCase();
					if(!blogwordcloud.contains(texttemp)){
						blogwordcloud.add(texttemp);
					}
					
				}
				
				Voices tv = new Voices();
				tv.setAuthor(author);
				tv.setPosttext(title);
				tv.setChannel(BLOGCH);
				tv.setSource("WebHose");
				if(dt!=null){
					tv.setVoicesdate(dt);
				}else{
					tv.setVoicesdate(new Date());
				}
				
				Long cardid = cc.getId();
				tv.setCardid(cardid);

				tv.setCardtype(CONVERSATIONTYPE);

				String carduniqueid = cc.getUniqueid();
				tv.setCarduniqueid(carduniqueid);
				
				String userid = cc.getUserid();
				tv.setUserid(new Long(userid));

				tv.setVoicesdate(new Date());
				tv.setVoicetype("WebHose");
				
				tv.setSourcelink(url);
				tv.setPosttextauthor(author);
				
				tv.setImageurl("http://203.123.190.50/streetbuzz/getimage.htm?mediaid=webhoselogo");
				
				tv.setSentimentrating(sentiment);
				
				tv.setCancomment(true);
				
				try {
					voicesDAO.addOrUpdateRecord(tv);
				} catch (HibernateException e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
					continue;
				}
			}
		}
		//Webhose Discussions
		List discwordcloud = new ArrayList();
		
		if(analysedWebHoseDiscussionList!=null && analysedWebHoseDiscussionList.size()>0){
			for(int i=0;i<analysedWebHoseDiscussionList.size();i++){
				WebHoseVO whv = (WebHoseVO)analysedWebHoseDiscussionList.get(i);
				String author = StrUtil.nonNull(whv.getAuthor());
				String sentiment = StrUtil.nonNull(whv.getSentiment());
				Date dt = whv.getDate();
				String title = StrUtil.nonNull(whv.getTitle());
				String url = StrUtil.nonNull(whv.getUrl());
				if(discwordcloud.size()<11){
					String text = StrUtil.nonNull(whv.getText());
					String texttemp = text.toLowerCase();
					if(!discwordcloud.contains(texttemp)){
						discwordcloud.add(texttemp);
					}
					
				}
				Voices tv = new Voices();
				tv.setAuthor(author);
				tv.setPosttext(title);
				tv.setChannel(FORUMCH);
				tv.setSource("WebHose");
				if(dt!=null){
					tv.setVoicesdate(dt);
				}else{
					tv.setVoicesdate(new Date());
				}
				
				Long cardid = cc.getId();
				tv.setCardid(cardid);

				tv.setCardtype(CONVERSATIONTYPE);

				String carduniqueid = cc.getUniqueid();
				tv.setCarduniqueid(carduniqueid);
				
				String userid = cc.getUserid();
				tv.setUserid(new Long(userid));

				tv.setVoicesdate(new Date());
				tv.setVoicetype("WebHose");
				
				tv.setSourcelink(url);
				tv.setPosttextauthor(author);
				
				tv.setImageurl("http://203.123.190.50/streetbuzz/getimage.htm?mediaid=webhoselogo");
				
				tv.setSentimentrating(sentiment);
				
				tv.setCancomment(true);
				
				try {
					voicesDAO.addOrUpdateRecord(tv);
				} catch (HibernateException e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
					continue;
				}
			}
		}
		
		

		// Web Sites
		Set keyset = analysedMap.keySet();

		Iterator it = keyset.iterator();
		while (it.hasNext()) {
			SearchDemoVO sdvo = (SearchDemoVO) it.next();
			List cList = (List) analysedMap.get(sdvo);
			Voices vw = new Voices();

			String url = sdvo.getUrl();
			if (url.indexOf(YOUTUBEEXC) > 0 || url.indexOf(WIKIPEDIAEXC) > 0) {
				continue;
			}
			vw.setSourcelink(url);

			String author = cc.getAuthor();
			vw.setAuthor(author);

			Long cardid = cc.getId();
			vw.setCardid(cardid);

			vw.setCardtype(CONVERSATIONTYPE);

			String carduniqueid = cc.getUniqueid();
			vw.setCarduniqueid(carduniqueid);
			
			//String host = StrUtil.nonNull(StrUtil.getHost(url));
			String domain = StrUtil.nonNull(StrUtil.getDomain(url));
			//vw.setSource(sdvo.getSearchsource());
			vw.setSource(domain);
			String channel = StrUtil.nonNull(StrUtil.getChannel(url));
			if ("".equalsIgnoreCase(channel)||GOOGLESEARCHC.equalsIgnoreCase(channel)) {
				//channel = GOOGLESEARCHC;
				continue;
			}
			vw.setChannel(channel);

			String userid = cc.getUserid();
			vw.setUserid(new Long(userid));

			vw.setVoicesdate(sdvo.getDate());
			
			String title = "";
			try {
				title = StrUtil.nonNull(StrUtil.getTitle(url));
			} catch (Exception e1) {
				System.out.println(e1.getMessage());
			}
			
			vw.setPosttext(title);
			// vw.setVoicetype(title);
			vw.setPosttextauthor(domain);

			String uniquevoicesidw = StrUtil.getUniqueId();
			vw.setUniquevoiceid(uniquevoicesidw);

			// To set thumb
			String thumb = "";
			try {
				// TODO Method to be implemented.
				thumb = StrUtil.getThumb(url);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("ProcessingHelper JAUNT parse error "
						+ e.getMessage());
			}
			vw.setThumb(thumb);
			// TODO for now till cross posting is sorted out.
			vw.setCancomment(true);

			try {
				voicesDAO.addOrUpdateRecord(vw);
			} catch (HibernateException e) {
				System.out.println(e.getMessage());
				continue;
			}

			Voices justvw = (Voices) voicesDAO
					.getObjectByUniqueId(uniquevoicesidw);
			Long justvwid = justvw.getId();
			if (cList != null && cList.size() > 0) {
				for (int i = 0; i < cList.size(); i++) {
					CrawlerVO cvo = (CrawlerVO) cList.get(i);

					VoicesDetails vdw = new VoicesDetails();

					String comment = StrUtil.nonNull(cvo.getComment());
					String username = StrUtil.nonNull(cvo.getUsername());
					if (comment.length() > 10000) {
						comment = comment.substring(0, 9000);
					}
					comment = DataClean.htmlClean(comment);
					if (comment.length()>9000){
						comment = comment.substring(0, 8500);
					}
					vdw.setPosttext(comment);
					vdw.setComment(comment);
					vdw.setCommenter(username);
					vdw.setPosttextauthor(username);

					String sentimentrating = StrUtil.nonNull(cvo
							.getSentimentstr());
					vdw.setSentimentrating(sentimentrating);

					int sentimentscore = cvo.getSentimentscore();
					vdw.setSentimentscore(new Long(sentimentscore));

					String positivephrase = cvo.getPositivephrase();
					String negativephrase = cvo.getNegativephrase();
					String neutralphrase = cvo.getNeutralphrase();
					vdw.setPositivephrase(positivephrase);
					vdw.setNeutralphrase(neutralphrase);
					vdw.setNegativephrase(negativephrase);

					vdw.setVoicedate(sdvo.getDate());
					vdw.setCardid(cc.getId());
					vdw.setVoicesid(justvwid);
					try {
						voicesDetailsDAO.addOrUpdateRecord(vdw);
					} catch (HibernateException e) {
						System.out.println(e.getMessage());
						continue;
					}
				}
				try {
					voicesDAO.addOrUpdateRecord(justvw);
				} catch (HibernateException e) {
					System.out.println(e.getMessage());
					continue;
				}
			}
		}
	//	List youtubewordcloud = new ArrayList();
		// Youtube
		System.out.println("HERE YOUTUBE SIZE "+YoutubeList.size());
		if (YoutubeList != null && YoutubeList.size() > 0) {
			for (int i = 0; i < YoutubeList.size(); i++) {
				YoutubeVO ytvo = (YoutubeVO) YoutubeList.get(i);
				DateTime dtTime = ytvo.getPublishdatetime();
				Date dt = new Date(dtTime.getValue());
				Voices yv = new Voices();

				String yauthor = cc.getAuthor();
				yv.setAuthor(yauthor);

				Long ycardid = cc.getId();
				yv.setCardid(ycardid);

				yv.setCardtype(CONVERSATIONTYPE);

				String ycarduniqueid = cc.getUniqueid();
				yv.setCarduniqueid(ycarduniqueid);

				yv.setSource(YOUTUBE);
				yv.setChannel(VIDEOCH);

				String yuniquevoiceid = StrUtil.getUniqueId();
				yv.setUniquevoiceid(yuniquevoiceid);

				String yuserid = cc.getUserid();
				yv.setUserid(new Long(yuserid));

				yv.setVoicesdate(dt);
				yv.setVoicetype(YOUTUBE);

				String youtubetitle = ytvo.getTitle();
				youtubetitle = DataClean.htmlClean(youtubetitle);
				String youtubeurl = ytvo.getYoutubeurl();
				String youtubethumbnailurl = ytvo.getThumbnailurl();
				String youtubevideoid = ytvo.getVideoId();
				String sentimentrating = ytvo.getSentimentrating();
				int sentimentscore = ytvo.getSentimentscore();

				yv.setPosttext(youtubetitle);
				yv.setPostid(youtubevideoid);
				yv.setSourcelink(youtubeurl);
				yv.setThumb(youtubethumbnailurl);
				yv.setSentimentrating(sentimentrating);
				yv.setSentimentscore(new Long(sentimentscore));

				yv.setCancomment(true);

				try {
					voicesDAO.addOrUpdateRecord(yv);
				} catch (HibernateException e) {
					System.out.println(e.getMessage());
					continue;
				}
				Voices justyv = (Voices) voicesDAO
						.getObjectByUniqueId(yuniquevoiceid);
				List crawlerList = ytvo.getCrawlerList();
			//	System.out.println("YOUTUBE CRAWLER LIST SIZE "+crawlerList.size());
				for (int j = 0; j < crawlerList.size(); j++) {
					CrawlerVO cvo = (CrawlerVO) crawlerList.get(j);

					String yvcomment = StrUtil.nonNull(cvo.getComment());
					String yvvommenttemp = yvcomment.toLowerCase();
					/*if(!youtubewordcloud.contains(yvvommenttemp)){
						youtubewordcloud.add(yvvommenttemp);
					}*/
					
					String yvusername = StrUtil.nonNull(cvo.getUsername());
					String yvpositivephrase = StrUtil.nonNull(cvo
							.getPositivephrase());
					String yvnegativephrase = StrUtil.nonNull(cvo
							.getNegativephrase());
					String yvneutralphrase = StrUtil.nonNull(cvo
							.getNeutralphrase());
					String yvsentiment = cvo.getSentimentstr();
					int yvsentimentscore = cvo.getSentimentscore();

					VoicesDetails yvd = new VoicesDetails();

					yvcomment = DataClean.htmlClean(yvcomment);
					if (yvcomment.length()>9000){
						yvcomment = yvcomment.substring(0, 8500);
					}
					yvd.setPosttext(yvcomment);
					yvd.setComment(yvcomment);
					yvd.setCommenter(yvusername);
					yvd.setPosttextauthor(yvusername);

					yvd.setSentimentrating(yvsentiment);
					yvd.setSentimentscore(new Long(yvsentimentscore));
					yvd.setCardid(cc.getId());
					yvd.setVoicesid(justyv.getId());
					yvd.setPositivephrase(yvpositivephrase);
					yvd.setNegativephrase(yvnegativephrase);
					yvd.setNeutralphrase(yvneutralphrase);
					yvd.setCommentdate(dt);
					voicesDetailsDAO.addOrUpdateRecord(yvd);

				}
			}
		}
		


		// Tumblr
		// System.out.println("****************STARTING PROCESSING
		// TUMBLR****************");
		//List tumblrwordcloud = new ArrayList();
		if (sentimentalTumblrList != null && sentimentalTumblrList.size() > 0) {
			// System.out.println("****************GOT INTO PROCESSING
			// TUMBLR****************");
			for (int i = 0; i < sentimentalTumblrList.size(); i++) {
				SearchVO svo = (SearchVO) sentimentalTumblrList.get(i);
				List crawllist = svo.getCrawlerList();
				Long commentscount = new Long(0);
				if (crawllist != null) {
					commentscount = new Long(crawllist.size());
				}

				Date dt = svo.getCommentdate();
				String commenter = StrUtil.nonNull(svo.getCommenter());
				String url = StrUtil.nonNull(svo.getUrl());
				String searchtopic = StrUtil.nonNull(svo.getSearchterm());
				String text = StrUtil.nonNull(svo.getText());
				String texttemp = text.toLowerCase();
			/*	if(!tumblrwordcloud.contains(texttemp)){
					tumblrwordcloud.add(texttemp);
				}*/
				String thumbnail = StrUtil.nonNull(svo.getThumbnail());
				text = DataClean.htmlClean(text);
				Voices tuv = new Voices();

				// tuv.setAuthor(commenter);
				tuv.setAuthor(commenter);
				tuv.setCardid(cc.getId());
				tuv.setCardtype(CONVERSATIONTYPE);
				tuv.setCarduniqueid(cc.getUniqueid());
				tuv.setVoicesdate(dt);
				tuv.setChannel(BLOGCH);
				tuv.setCommentscount(commentscount);
				tuv.setImageurl(thumbnail);
				tuv.setThumb(thumbnail);
				tuv.setSource(TUMBLRC);
				tuv.setSourcelink(url);
				String title = "";
				try {
					title = StrUtil.nonNull(StrUtil.getTitle(url));
				} catch (Exception e1) {
					System.out.println(e1.getMessage());
				}
				tuv.setPosttext(title);
				tuv.setPosttextauthor(commenter);
				tuv.setUserid(new Long(cc.getUserid()));

				String uniquevoicesidtu = StrUtil.getUniqueId();
				tuv.setUniquevoiceid(uniquevoicesidtu);
				tuv.setCancomment(true);
				try {
					voicesDAO.addOrUpdateRecord(tuv);
				} catch (HibernateException e) {
					System.out.println(e.getMessage());
					continue;
				}
				Voices justtuv = (Voices) voicesDAO
						.getObjectByUniqueId(uniquevoicesidtu);
				Long justtuvid = justtuv.getId();

				if (crawllist != null) {
					for (int j = 0; j < crawllist.size(); j++) {
						CrawlerVO cvo = (CrawlerVO) crawllist.get(j);
						String comment = StrUtil.nonNull(cvo.getComment());
						VoicesDetails tuvd = new VoicesDetails();
						tuvd.setCardid(cc.getId());
						tuvd.setVoicesid(justtuvid);
						tuvd.setImageurl("");
						tuvd.setPostauthorid("");
						comment = DataClean.htmlClean(comment);
						if (comment.length()>9000){
							comment = comment.substring(0, 8500);
						}
						tuvd.setPosttext(comment);
						tuvd.setPosttextauthor(StrUtil.nonNull(cvo
								.getUsername()));
						tuvd.setCommenter(StrUtil.nonNull(cvo.getUsername()));
						tuvd.setComment(comment);

						int sentimentscoretuvd = cvo.getSentimentscore();
						String sentimentratingtuvd = cvo.getSentimentstr();
						String positivephrase = cvo.getPositivephrase();
						String negativephrase = cvo.getNegativephrase();
						String neutralphrase = cvo.getNeutralphrase();
						tuvd.setNegativephrase(negativephrase);
						tuvd.setPositivephrase(positivephrase);
						tuvd.setNeutralphrase(neutralphrase);
						tuvd.setSentimentrating(sentimentratingtuvd);
						tuvd.setSentimentscore(new Long(sentimentscoretuvd));

						try {
							voicesDetailsDAO.addOrUpdateRecord(tuvd);
						} catch (HibernateException e) {
							System.out.println(e.getMessage());
							continue;
						}

					}
				}
			}
		}
		long one = System.currentTimeMillis();
//		Process the wordcloud
		List list = new ArrayList();
		list.addAll(blogwordcloud);
		list.addAll(discwordcloud);
		list.addAll(twitwordcloud);
		//list.addAll(youtubewordcloud);
		//list.addAll(tumblrwordcloud);
		Map grossmap = StrUtil.process(list);
		Set set = grossmap.keySet();
		Iterator itw = set.iterator();
		int counter = 0;
		while(itw.hasNext()){
			String key = (String)itw.next();
			boolean bool = StrUtil.isAlphabet(key);
			if(!bool){
				continue;
			}
			Integer value = (Integer)grossmap.get(key);
			if(!"".equalsIgnoreCase(key)){
				counter = counter+1;
				if(counter>20){
					break;
				}
				WordCloudStore wcs = new WordCloudStore();
				wcs.setCardid(cc.getId());
				wcs.setWord(key);
				int valueint = value.intValue();
				Long vLong = new Long(valueint);
				wcs.setCount(vLong);
				wordCloudStoreDAO.addOrUpdateRecord(wcs);
				
			}
		
			
		}
	long two = System.currentTimeMillis();
	
	System.out.println("****************************** data for Wordcloud: ****************************** "+((two-one)/1000)+" secs");

		// Instagram
		if (sentimentalInstagramList != null
				&& sentimentalInstagramList.size() > 0) {
			for (int i = 0; i < sentimentalInstagramList.size(); i++) {
				SearchVO svo = (SearchVO) sentimentalInstagramList.get(i);

				List crawllist = svo.getCrawlerList();
				Long commentscount = new Long(0);
				if (crawllist != null) {
					commentscount = new Long(crawllist.size());
				}
				String commenter = StrUtil.nonNull(svo.getCommenter());
				String searchterm = StrUtil.nonNull(svo.getSearchterm());
				String text = StrUtil.nonNull(svo.getText());
				String url = StrUtil.nonNull(svo.getUrl());
				String thumbnail = StrUtil.nonNull(svo.getThumbnail());
				String profilepic = StrUtil.nonNull(svo.getProfilepic());
				Date dt = svo.getCommentdate();

				Voices vinst = new Voices();

				vinst.setAuthor(commenter);
				vinst.setCardid(cc.getId());
				vinst.setCardtype(CONVERSATIONTYPE);
				vinst.setCarduniqueid(cc.getUniqueid());
				vinst.setChannel(IMAGECH);
				vinst.setCommentscount(commentscount);
				vinst.setImageurl(thumbnail);
				vinst.setThumb(thumbnail);
				vinst.setAvatar(profilepic);
				vinst.setVoicesdate(dt);
				vinst.setSource(INSTAGRAMC);
				vinst.setSourcelink(url);
				text = DataClean.htmlClean(text);
				vinst.setPosttext(text);
				vinst.setPosttextauthor(commenter);
				vinst.setUserid(new Long(cc.getUserid()));

				String uniquevoicesidinst = StrUtil.getUniqueId();
				vinst.setUniquevoiceid(uniquevoicesidinst);
				vinst.setCancomment(true);
				try {
					voicesDAO.addOrUpdateRecord(vinst);
				} catch (HibernateException e) {
					System.out.println(e.getMessage());
					continue;
				}
				Voices justvinst = (Voices) voicesDAO
						.getObjectByUniqueId(uniquevoicesidinst);
				Long justtuvid = justvinst.getId();

				if (crawllist != null) {
					for (int j = 0; j < crawllist.size(); j++) {
						CrawlerVO cvo = (CrawlerVO) crawllist.get(j);
						String comment = StrUtil.nonNull(cvo.getComment());
						String avatar = StrUtil.nonNull(cvo.getAvatar());
						Date cmntdt = cvo.getDate();
						

						VoicesDetails tuvd = new VoicesDetails();
						tuvd.setCardid(cc.getId());
						tuvd.setVoicesid(justtuvid);
						tuvd.setImageurl("");
						tuvd.setCommentdate(cmntdt);
						tuvd.setPostauthorid("");
						comment = DataClean.htmlClean(comment);
						if (comment.length()>9000){
							comment = comment.substring(0, 8500);
						}
						tuvd.setPosttext(comment);
						tuvd.setComment(comment);
						tuvd.setPosttextauthor(StrUtil.nonNull(cvo
								.getUsername()));
						tuvd.setCommenter(StrUtil.nonNull(cvo.getUsername()));
						int sentimentscoretuvd = cvo.getSentimentscore();
						String sentimentratingtuvd = cvo.getSentimentstr();
						String positivephrase = cvo.getPositivephrase();
						String negativephrase = cvo.getNegativephrase();
						String neutralphrase = cvo.getNeutralphrase();
						tuvd.setNegativephrase(negativephrase);
						tuvd.setPositivephrase(positivephrase);
						tuvd.setNeutralphrase(neutralphrase);
						tuvd.setSentimentrating(sentimentratingtuvd);
						tuvd.setSentimentscore(new Long(sentimentscoretuvd));
						tuvd.setImageurl(avatar);

						try {
							voicesDetailsDAO.addOrUpdateRecord(tuvd);
						} catch (HibernateException e) {
							System.out.println(e.getMessage());
							continue;
						}
					}
				}
			}
		}
		if(existingVoicesList!=null){
			if(existingVoicesList.size()>0){
				for (int i=0;i<existingVoicesList.size();i++){
					Voices voices = (Voices)existingVoicesList.get(i);
					voicesDAO.deleteRecord(voices);
				}
			}
		}
		
		long end = System.currentTimeMillis();
		long gap = end - start;
		System.out.println("****************VOICES / COMMENTS DB UPDATES*****************" +(( gap)/1000)+"secs");
		return cc;
	}

	public static ConversationCard spotateConversationCard(ConversationCard cc,
			List twitterList, Map sitesearchresult, VoicesDAO voicesDAO,
			VoicesDetailsDAO voicesDetailsDAO, List YoutubeList) {
		// Twitter
		if (twitterList != null && twitterList.size() > 0) {
			for (int g = 0; g < twitterList.size(); g++) {
				SearchVO svotwit = (SearchVO) twitterList.get(g);
				Voices tv = new Voices();

				String author = cc.getAuthor();
				tv.setAuthor(author);

				Long cardid = cc.getId();
				tv.setCardid(cardid);

				tv.setCardtype(SEARCHTYPE);

				String carduniqueid = cc.getUniqueid();
				tv.setCarduniqueid(carduniqueid);

				tv.setSource(TWITTER);

				String uniquevoiceid = StrUtil.getUniqueId();
				tv.setUniquevoiceid(uniquevoiceid);

				String userid = cc.getUserid();
				tv.setUserid(new Long(userid));

				tv.setVoicesdate(new Date());
				tv.setVoicetype(TWITTER);

				String negativephrase = svotwit.getNegativephrase();
				tv.setNegativephrase(negativephrase);

				String neutralphrase = svotwit.getNeutralphrase();
				tv.setNeutralphrase(neutralphrase);

				String positivephrase = svotwit.getPositivephrase();
				tv.setPositivephrase(positivephrase);

				String nameidT = svotwit.getCommenterid();
				tv.setPostauthorid(nameidT);

				String twitT = svotwit.getText();
				tv.setPosttext(twitT);

				String nameT = svotwit.getCommenter();
				tv.setPosttextauthor(nameT);

				String sentimentT = svotwit.getSentiment();
				tv.setSentimentrating(sentimentT);

				int sentimentratingT = svotwit.getSentimentscore();
				tv.setSentimentscore(new Long(sentimentratingT));

				voicesDAO.addOrUpdateRecord(tv);

			}
		}

		// Web Sites
		// Google Searches
		Map gmap = (Map) sitesearchresult.get("google");
		Set keyset = gmap.keySet();
		Iterator wit = keyset.iterator();
		while (wit.hasNext()) {
			Result result = (Result) wit.next();
			List cList = (List) gmap.get(result);

			Voices vw = new Voices();

			String author = cc.getAuthor();
			vw.setAuthor(author);

			Long cardid = cc.getId();
			vw.setCardid(cardid);

			vw.setCardtype(SEARCHTYPE);

			String carduniqueid = cc.getUniqueid();
			vw.setCarduniqueid(carduniqueid);

			vw.setSource(WEB);

			String userid = cc.getUserid();
			vw.setUserid(new Long(userid));

			vw.setVoicesdate(new Date());

			String title = result.getTitle();
			vw.setVoicetype(title);

			String url = result.getUrl();
			vw.setSourcelink(url);

			String uniquevoicesidw = StrUtil.getUniqueId();
			vw.setUniquevoiceid(uniquevoicesidw);

			voicesDAO.addOrUpdateRecord(vw);

			Voices justvw = (Voices) voicesDAO
					.getObjectByUniqueId(uniquevoicesidw);
			Long justvwid = justvw.getId();

			if (cList != null && cList.size() > 0) {
				for (int i = 0; i < cList.size(); i++) {
					CrawlerVO cvo = (CrawlerVO) cList.get(i);

					VoicesDetails vdw = new VoicesDetails();

					String comment = StrUtil.nonNull(cvo.getComment());
					vdw.setPosttext(comment);

					String sentimentrating = StrUtil.nonNull(cvo
							.getSentimentstr());
					vdw.setSentimentrating(sentimentrating);

					int sentimentscore = cvo.getSentimentscore();
					vdw.setSentimentscore(new Long(sentimentscore));

					String positivephrase = cvo.getPositivephrase();
					String negativephrase = cvo.getNegativephrase();
					String neutralphrase = cvo.getNeutralphrase();
					vdw.setPositivephrase(positivephrase);
					vdw.setNeutralphrase(neutralphrase);
					vdw.setNegativephrase(negativephrase);

					String timestamp = StrUtil.nonNull(cvo.getTimestamp());
					Date dt = StrUtil.getDate(timestamp, dateformat);
					vdw.setVoicedate(dt);
					voicesDetailsDAO.addOrUpdateRecord(vdw);
				}

				// TODO we have to update the justvoices object here with
				// counts etc and persist it

				// justvw.setCommentscount(commentscount);
				// justvw.setExtcommentscount(extcommentscount);
				// justvw.setLikescount(likescount);
				// justvw.setExtlikescount(extlikescount);
				// justvw.setExtviewscount(extviewscount);
				// justvw.setThumbsdowncount(thumbsdowncount);
				// justvw.setThumbsupcount(thumbsupcount);

				voicesDAO.addOrUpdateRecord(justvw);
			}

		}

		// Faroo Searches
		Map fmap = (Map) sitesearchresult.get("faroo");

		Set fkeyset = fmap.keySet();
		Iterator fwit = fkeyset.iterator();
		while (fwit.hasNext()) {
			FarooResult fr = (FarooResult) fwit.next();
			List cList = (List) fmap.get(fr);

			Voices vw = new Voices();

			String author = cc.getAuthor();
			vw.setAuthor(author);

			Long cardid = cc.getId();
			vw.setCardid(cardid);

			vw.setCardtype(SEARCHTYPE);

			String carduniqueid = cc.getUniqueid();
			vw.setCarduniqueid(carduniqueid);

			vw.setSource(WEB);

			String userid = cc.getUserid();
			vw.setUserid(new Long(userid));

			vw.setVoicesdate(new Date());

			String title = fr.getTitle();
			vw.setVoicetype(title);

			String url = fr.getUrl();
			vw.setSourcelink(url);

			String uniquevoicesidw = StrUtil.getUniqueId();
			vw.setUniquevoiceid(uniquevoicesidw);

			voicesDAO.addOrUpdateRecord(vw);

			Voices justvw = (Voices) voicesDAO
					.getObjectByUniqueId(uniquevoicesidw);
			Long justvwid = justvw.getId();

			if (cList != null && cList.size() > 0) {
				for (int i = 0; i < cList.size(); i++) {
					CrawlerVO cvo = (CrawlerVO) cList.get(i);

					VoicesDetails vdw = new VoicesDetails();

					String comment = StrUtil.nonNull(cvo.getComment());
					vdw.setPosttext(comment);

					String sentimentrating = StrUtil.nonNull(cvo
							.getSentimentstr());
					vdw.setSentimentrating(sentimentrating);

					int sentimentscore = cvo.getSentimentscore();
					vdw.setSentimentscore(new Long(sentimentscore));

					String positivephrase = cvo.getPositivephrase();
					String negativephrase = cvo.getNegativephrase();
					String neutralphrase = cvo.getNeutralphrase();
					vdw.setPositivephrase(positivephrase);
					vdw.setNeutralphrase(neutralphrase);
					vdw.setNegativephrase(negativephrase);

					String timestamp = StrUtil.nonNull(cvo.getTimestamp());
					Date dt = StrUtil.getDate(timestamp, dateformat);
					vdw.setVoicedate(dt);
					voicesDetailsDAO.addOrUpdateRecord(vdw);
				}

				// TODO we have to update the justvoices object here with
				// counts etc and persist it

				// justvw.setCommentscount(commentscount);
				// justvw.setExtcommentscount(extcommentscount);
				// justvw.setLikescount(likescount);
				// justvw.setExtlikescount(extlikescount);
				// justvw.setExtviewscount(extviewscount);
				// justvw.setThumbsdowncount(thumbsdowncount);
				// justvw.setThumbsupcount(thumbsupcount);

				voicesDAO.addOrUpdateRecord(justvw);
			}

		}

		// Youtube
		if (YoutubeList != null && YoutubeList.size() > 0) {
			for (int i = 0; i < YoutubeList.size(); i++) {
				YoutubeVO ytvo = (YoutubeVO) YoutubeList.get(i);
				Voices yv = new Voices();

				String yauthor = cc.getAuthor();
				yv.setAuthor(yauthor);

				Long ycardid = cc.getId();
				yv.setCardid(ycardid);

				yv.setCardtype(SEARCHTYPE);

				String ycarduniqueid = cc.getUniqueid();
				yv.setCarduniqueid(ycarduniqueid);

				yv.setSource(YOUTUBE);

				String yuniquevoiceid = StrUtil.getUniqueId();
				yv.setUniquevoiceid(yuniquevoiceid);

				String yuserid = cc.getUserid();
				yv.setUserid(new Long(yuserid));

				yv.setVoicesdate(new Date());
				yv.setVoicetype(YOUTUBE);

				String youtubetitle = ytvo.getTitle();
				String youtubeurl = ytvo.getYoutubeurl();
				String youtubethumbnailurl = ytvo.getThumbnailurl();
				String youtubevideoid = ytvo.getVideoId();

				voicesDAO.addOrUpdateRecord(yv);
			}
		}

		return cc;
	}

	public static SearchCard spotateSearchCard(SearchCard sc, List twitterList,
			Map sitesearchresult, VoicesDAO voicesDAO,
			VoicesDetailsDAO voicesDetailsDAO, List YoutubeList) {
		// Twitter
		if (twitterList != null && twitterList.size() > 0) {
			for (int g = 0; g < twitterList.size(); g++) {
				SearchVO svotwit = (SearchVO) twitterList.get(g);
				Voices tv = new Voices();

				String author = sc.getAuthor();
				tv.setAuthor(author);

				Long cardid = sc.getId();
				tv.setCardid(cardid);

				tv.setCardtype(SEARCHTYPE);

				String carduniqueid = sc.getUniqueid();
				tv.setCarduniqueid(carduniqueid);

				tv.setSource(TWITTER);

				String uniquevoiceid = StrUtil.getUniqueId();
				tv.setUniquevoiceid(uniquevoiceid);

				String userid = sc.getUserid();
				tv.setUserid(new Long(userid));

				tv.setVoicesdate(new Date());
				tv.setVoicetype(TWITTER);

				String negativephrase = svotwit.getNegativephrase();
				tv.setNegativephrase(negativephrase);

				String neutralphrase = svotwit.getNeutralphrase();
				tv.setNeutralphrase(neutralphrase);

				String positivephrase = svotwit.getPositivephrase();
				tv.setPositivephrase(positivephrase);

				String nameidT = svotwit.getCommenterid();
				tv.setPostauthorid(nameidT);

				String twitT = svotwit.getText();
				tv.setPosttext(twitT);

				String nameT = svotwit.getCommenter();
				tv.setPosttextauthor(nameT);

				String sentimentT = svotwit.getSentiment();
				tv.setSentimentrating(sentimentT);

				int sentimentratingT = svotwit.getSentimentscore();
				tv.setSentimentscore(new Long(sentimentratingT));

				voicesDAO.addOrUpdateRecord(tv);

			}

		}

		// Web Sites
		// Google Searches
		Map gmap = (Map) sitesearchresult.get("google");
		Set keyset = gmap.keySet();
		Iterator wit = keyset.iterator();
		while (wit.hasNext()) {
			Result result = (Result) wit.next();
			List cList = (List) gmap.get(result);

			Voices vw = new Voices();

			String author = sc.getAuthor();
			vw.setAuthor(author);

			Long cardid = sc.getId();
			vw.setCardid(cardid);

			vw.setCardtype(SEARCHTYPE);

			String carduniqueid = sc.getUniqueid();
			vw.setCarduniqueid(carduniqueid);

			vw.setSource(WEB);

			String userid = sc.getUserid();
			vw.setUserid(new Long(userid));

			vw.setVoicesdate(new Date());

			String title = result.getTitle();
			vw.setVoicetype(title);

			String url = result.getUrl();
			vw.setSourcelink(url);

			String uniquevoicesidw = StrUtil.getUniqueId();
			vw.setUniquevoiceid(uniquevoicesidw);

			voicesDAO.addOrUpdateRecord(vw);

			Voices justvw = (Voices) voicesDAO
					.getObjectByUniqueId(uniquevoicesidw);
			Long justvwid = justvw.getId();

			if (cList != null && cList.size() > 0) {
				for (int i = 0; i < cList.size(); i++) {
					CrawlerVO cvo = (CrawlerVO) cList.get(i);

					VoicesDetails vdw = new VoicesDetails();

					String comment = StrUtil.nonNull(cvo.getComment());
					vdw.setPosttext(comment);

					String sentimentrating = StrUtil.nonNull(cvo
							.getSentimentstr());
					vdw.setSentimentrating(sentimentrating);

					int sentimentscore = cvo.getSentimentscore();
					vdw.setSentimentscore(new Long(sentimentscore));

					String positivephrase = cvo.getPositivephrase();
					String negativephrase = cvo.getNegativephrase();
					String neutralphrase = cvo.getNeutralphrase();
					vdw.setPositivephrase(positivephrase);
					vdw.setNeutralphrase(neutralphrase);
					vdw.setNegativephrase(negativephrase);

					String timestamp = StrUtil.nonNull(cvo.getTimestamp());
					Date dt = StrUtil.getDate(timestamp, dateformat);
					vdw.setVoicedate(dt);
					voicesDetailsDAO.addOrUpdateRecord(vdw);
				}

				// TODO we have to update the justvoices object here with
				// counts etc and persist it

				// justvw.setCommentscount(commentscount);
				// justvw.setExtcommentscount(extcommentscount);
				// justvw.setLikescount(likescount);
				// justvw.setExtlikescount(extlikescount);
				// justvw.setExtviewscount(extviewscount);
				// justvw.setThumbsdowncount(thumbsdowncount);
				// justvw.setThumbsupcount(thumbsupcount);

				voicesDAO.addOrUpdateRecord(justvw);
			}

		}

		// Faroo Searches
		Map fmap = (Map) sitesearchresult.get("faroo");

		Set fkeyset = fmap.keySet();
		Iterator fwit = keyset.iterator();
		while (fwit.hasNext()) {
			FarooResult fr = (FarooResult) fwit.next();
			List cList = (List) fmap.get(fr);

			Voices vw = new Voices();

			String author = sc.getAuthor();
			vw.setAuthor(author);

			Long cardid = sc.getId();
			vw.setCardid(cardid);

			vw.setCardtype(SEARCHTYPE);

			String carduniqueid = sc.getUniqueid();
			vw.setCarduniqueid(carduniqueid);

			vw.setSource(WEB);

			String userid = sc.getUserid();
			vw.setUserid(new Long(userid));

			vw.setVoicesdate(new Date());

			String title = fr.getTitle();
			vw.setVoicetype(title);

			String url = fr.getUrl();
			vw.setSourcelink(url);

			String uniquevoicesidw = StrUtil.getUniqueId();
			vw.setUniquevoiceid(uniquevoicesidw);

			voicesDAO.addOrUpdateRecord(vw);

			Voices justvw = (Voices) voicesDAO
					.getObjectByUniqueId(uniquevoicesidw);
			Long justvwid = justvw.getId();

			if (cList != null && cList.size() > 0) {
				for (int i = 0; i < cList.size(); i++) {
					CrawlerVO cvo = (CrawlerVO) cList.get(i);

					VoicesDetails vdw = new VoicesDetails();

					String comment = StrUtil.nonNull(cvo.getComment());
					vdw.setPosttext(comment);

					String sentimentrating = StrUtil.nonNull(cvo
							.getSentimentstr());
					vdw.setSentimentrating(sentimentrating);

					int sentimentscore = cvo.getSentimentscore();
					vdw.setSentimentscore(new Long(sentimentscore));

					String positivephrase = cvo.getPositivephrase();
					String negativephrase = cvo.getNegativephrase();
					String neutralphrase = cvo.getNeutralphrase();
					vdw.setPositivephrase(positivephrase);
					vdw.setNeutralphrase(neutralphrase);
					vdw.setNegativephrase(negativephrase);

					String timestamp = StrUtil.nonNull(cvo.getTimestamp());
					Date dt = StrUtil.getDate(timestamp, dateformat);
					vdw.setVoicedate(dt);
					voicesDetailsDAO.addOrUpdateRecord(vdw);
				}

				// TODO we have to update the justvoices object here with
				// counts etc and persist it

				// justvw.setCommentscount(commentscount);
				// justvw.setExtcommentscount(extcommentscount);
				// justvw.setLikescount(likescount);
				// justvw.setExtlikescount(extlikescount);
				// justvw.setExtviewscount(extviewscount);
				// justvw.setThumbsdowncount(thumbsdowncount);
				// justvw.setThumbsupcount(thumbsupcount);

				voicesDAO.addOrUpdateRecord(justvw);
			}

		}

		// Youtube
		if (YoutubeList != null && YoutubeList.size() > 0) {
			for (int i = 0; i < YoutubeList.size(); i++) {
				YoutubeVO ytvo = (YoutubeVO) YoutubeList.get(i);
				Voices yv = new Voices();

				String yauthor = sc.getAuthor();
				yv.setAuthor(yauthor);

				Long ycardid = sc.getId();
				yv.setCardid(ycardid);

				yv.setCardtype(SEARCHTYPE);

				String ycarduniqueid = sc.getUniqueid();
				yv.setCarduniqueid(ycarduniqueid);

				yv.setSource(YOUTUBE);

				String yuniquevoiceid = StrUtil.getUniqueId();
				yv.setUniquevoiceid(yuniquevoiceid);

				String yuserid = sc.getUserid();
				yv.setUserid(new Long(yuserid));

				yv.setVoicesdate(new Date());
				yv.setVoicetype(YOUTUBE);

				String youtubetitle = ytvo.getTitle();
				String youtubeurl = ytvo.getYoutubeurl();
				String youtubethumbnailurl = ytvo.getThumbnailurl();
				String youtubevideoid = ytvo.getVideoId();

				voicesDAO.addOrUpdateRecord(yv);
			}
		}

		return sc;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
