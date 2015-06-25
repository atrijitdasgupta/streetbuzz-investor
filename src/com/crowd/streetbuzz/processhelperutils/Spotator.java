/**
 * 
 */
package com.crowd.streetbuzz.processhelperutils;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.VoicesDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDetailsDAO;
import com.crowd.streetbuzz.model.SearchCard;
import com.crowd.streetbuzz.model.Voices;
import com.crowd.streetbuzz.model.VoicesDetails;
import com.crowd.streetbuzzalgo.constants.SystemConstants;
import com.crowd.streetbuzzalgo.crawler.vo.CrawlerVO;
import com.crowd.streetbuzzalgo.faroosearch.vo.FarooResult;
import com.crowd.streetbuzzalgo.googlesearch.vo.Result;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.crowd.streetbuzzalgo.vo.SearchVO;
import com.crowd.streetbuzzalgo.vo.YoutubeVO;
import com.crowd.streetbuzzalgo.yandexsearch.YandexVO;

/**
 * @author Atrijit
 * 
 */
public class Spotator implements Constants, SystemConstants {

	/**
	 * 
	 */
	public Spotator() {
		// TODO Auto-generated constructor stub
	}

	public static SearchCard spotateTwitter(SearchCard sc, List twitterList,
			VoicesDAO voicesDAO, VoicesDetailsDAO voicesDetailsDAO) {
		// Twitter
		System.out.println("Spotating Twitter");
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

				Date dt = svotwit.getCommentdate();
				if (dt != null) {
					tv.setVoicesdate(new Date());
				} else {
					tv.setVoicesdate(new Date());
				}

				tv.setVoicetype(TWITTER);

				tv.setChannel(NETWORKCHANNEL);

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

				String sentimentStr = sentimentT.toLowerCase();

				// Update sc
				if (sentimentStr.indexOf("positive") > 0) {
					Long p = sc.getPositivevoices();
					if (p == null) {
						p = new Long(0);
					}
					p = p + 1;
					sc.setPositivevoices(p);
				}
				if (sentimentStr.indexOf("negative") > 0) {
					Long n = sc.getNegativevoices();
					if (n == null) {
						n = new Long(0);
					}
					n = n + 1;
					sc.setNegativevoices(n);
				}
				if (sentimentStr.indexOf("neutral") > 0) {
					Long ne = sc.getNeutralvoices();
					if (ne == null) {
						ne = new Long(0);
					}
					ne = ne + 1;
					sc.setNeutralvoices(ne);
				}

				voicesDAO.addOrUpdateRecord(tv);

			}
			Long voicescount = sc.getVoicescount();
			int twittervoicescount = twitterList.size();
			Long twittervoicescountLong = new Long(twittervoicescount);
			Long currentcount = voicescount + twittervoicescountLong;
			sc.setVoicescount(currentcount);
		}
		
		return sc;
	}
	
	private static String getChannel(String url){
		String channel = WEBSITECHANNEL;
		
		if(url.indexOf(BLOGSPOT)>-1){
			channel = BLOGCH;
		}else if(url.indexOf(WORDPRESS)>-1){
			channel = BLOGCH;
		}else if(url.indexOf(TUMBLR)>-1){
			channel = BLOGCH;
		}
		return channel;
	}

	public static SearchCard spotateGoogleSites(SearchCard sc,
			Map sitesearchresult, VoicesDAO voicesDAO,
			VoicesDetailsDAO voicesDetailsDAO) {
		System.out.println("Spotating Google sites");
		// Google Searches
		Map gmap = (Map) sitesearchresult.get("google");
		Set keyset = gmap.keySet();
		Iterator wit = keyset.iterator();
		int count = 0;
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
			
			String channel = getChannel(url);
			vw.setChannel(channel);

			String uniquevoicesidw = StrUtil.getUniqueId();
			vw.setUniquevoiceid(uniquevoicesidw);
			
			//Because there's no text belonging to a website.
			//String topic = sc.getTopic();
			vw.setPosttext(title);

			voicesDAO.addOrUpdateRecord(vw);
			System.out.println("updated google voice");
			Voices justvw = (Voices) voicesDAO
					.getObjectByUniqueId(uniquevoicesidw);
			Long justvwid = justvw.getId();

			if (cList != null && cList.size() > 0) {
				for (int i = 0; i < cList.size(); i++) {
					CrawlerVO cvo = (CrawlerVO) cList.get(i);

					VoicesDetails vdw = new VoicesDetails();

					String comment = StrUtil.nonNull(cvo.getComment());
					if(comment.length()>255){
						comment = comment.substring(0,250);
					}
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

					Date dt = new Date();
					String timestamp = StrUtil.nonNull(cvo.getTimestamp());
					if(!"".equalsIgnoreCase(timestamp)){
						dt = StrUtil.getDate(timestamp, dateformat);
					}
					
					vdw.setVoicedate(dt);
					voicesDetailsDAO.addOrUpdateRecord(vdw);
					count++;
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
		if(gmap!=null && gmap.size()>0){
			Long googlevoicescount = new Long(count);
			Long spcount = sc.getVoicescount();
			if(spcount!=null){
				Long currentcount = spcount + googlevoicescount;
				sc.setVoicescount(currentcount);
			}else{
				sc.setVoicescount(googlevoicescount);
			}
			
		}
		
		return sc;
	}
	
	public static SearchCard spotateYandexSites(SearchCard sc,
			Map sitesearchresult, VoicesDAO voicesDAO,
			VoicesDetailsDAO voicesDetailsDAO) {
		System.out.println("Spotating Yandex sites");
		Map ymap = (Map)sitesearchresult.get("yandex");
		Set ykeyset = ymap.keySet();
		Iterator ywit = ykeyset.iterator();
		int count = 0;
		while (ywit.hasNext()){
			YandexVO yndxvo = (YandexVO)ywit.next();
			List cList = (List)ymap.get(yndxvo);
			
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
			
			Date voicesdate = yndxvo.getModdate();
			
			vw.setVoicesdate(voicesdate);
			
			String title = yndxvo.getTitle();
			vw.setVoicetype(title);
			
			String url = yndxvo.getUrl();
			vw.setSourcelink(url);
			
			String channel = getChannel(url);
			vw.setChannel(channel);
			
			String uniquevoicesidw = StrUtil.getUniqueId();
			vw.setUniquevoiceid(uniquevoicesidw);
			
			vw.setPosttext(title);
			
			voicesDAO.addOrUpdateRecord(vw);

			Voices justvw = (Voices) voicesDAO
					.getObjectByUniqueId(uniquevoicesidw);
			Long justvwid = justvw.getId();
			
			if (cList != null && cList.size() > 0) {
				for (int i = 0; i < cList.size(); i++) {
					CrawlerVO cvo = (CrawlerVO) cList.get(i);

					VoicesDetails vdw = new VoicesDetails();
					
					String comment = StrUtil.nonNull(cvo.getComment());
					if(comment.length()>255){
						comment = comment.substring(0,250);
					}
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

					Date dt = new Date();
					String timestamp = StrUtil.nonNull(cvo.getTimestamp());
					if(!"".equalsIgnoreCase(timestamp)){
						dt = StrUtil.getDate(timestamp, dateformat);
					}
					
					vdw.setVoicedate(dt);
					voicesDetailsDAO.addOrUpdateRecord(vdw);
					count++;
				}
			}
			
		}
		if(ymap!=null && ymap.size()>0){
			Long yandexvoicescount = new Long(count);
			Long spcount = sc.getVoicescount();
			if(spcount!=null){
				Long currentcount = spcount + yandexvoicescount;
				sc.setVoicescount(currentcount);
			}else{
				sc.setVoicescount(yandexvoicescount);
			}
			
		}
		return sc;
	}

	public static SearchCard spotateFarooSites(SearchCard sc,
			Map sitesearchresult, VoicesDAO voicesDAO,
			VoicesDetailsDAO voicesDetailsDAO) {
		System.out.println("Spotating Faroo sites");
		// Faroo Searches
		Map fmap = (Map) sitesearchresult.get("faroo");

		Set fkeyset = fmap.keySet();
		Iterator fwit = fkeyset.iterator();
		int count = 0;
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
			
			Date fdate = new Date();
			String fdatestr = StrUtil.nonNull(fr.getDate());
			if(!"".equalsIgnoreCase(fdatestr)){
				fdate = StrUtil.getFarooDate(fdatestr);
			}
			vw.setVoicesdate(fdate);

			String title = fr.getTitle();
			vw.setVoicetype(title);

			String url = fr.getUrl();
			vw.setSourcelink(url);
			
			String channel = getChannel(url);
			vw.setChannel(channel);

			String uniquevoicesidw = StrUtil.getUniqueId();
			vw.setUniquevoiceid(uniquevoicesidw);
			
//			Because there's no text belonging to a website.
			//String topic = sc.getTopic();
			vw.setPosttext(title);

			voicesDAO.addOrUpdateRecord(vw);

			Voices justvw = (Voices) voicesDAO
					.getObjectByUniqueId(uniquevoicesidw);
			Long justvwid = justvw.getId();

			if (cList != null && cList.size() > 0) {
				for (int i = 0; i < cList.size(); i++) {
					CrawlerVO cvo = (CrawlerVO) cList.get(i);

					VoicesDetails vdw = new VoicesDetails();

					String comment = StrUtil.nonNull(cvo.getComment());
					if(comment.length()>255){
						comment = comment.substring(0,250);
					}
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

					Date dt = new Date();
					String timestamp = StrUtil.nonNull(cvo.getTimestamp());
					if(!"".equalsIgnoreCase(timestamp)){
						dt = StrUtil.getDate(timestamp, dateformat);
					}
					
					vdw.setVoicedate(dt);
					voicesDetailsDAO.addOrUpdateRecord(vdw);
					count++;
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
		if(fmap!=null && fmap.size()>0){
			Long faroovoicescount = new Long(count);
			Long spcount = sc.getVoicescount();
			if(spcount!=null){
				Long currentcount = spcount + faroovoicescount;
				sc.setVoicescount(currentcount);
			}else{
				sc.setVoicescount(faroovoicescount);
			}
			
		}
		
		return sc;
	}

	public static SearchCard spotateYoutube(SearchCard sc, VoicesDAO voicesDAO,
			VoicesDetailsDAO voicesDetailsDAO, List YoutubeList) {
		System.out.println("Spotating Youtube sites");
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

				yv.setChannel(VIDEOCH);

				String negativephrase = ytvo.getNegativephrase();
				yv.setNegativephrase(negativephrase);

				String neutralphrase = ytvo.getNeutralphrase();
				yv.setNeutralphrase(neutralphrase);

				String positivephrase = ytvo.getPositivephrase();
				yv.setPositivephrase(positivephrase);

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

				yv.setThumb(youtubethumbnailurl);
				yv.setSourcelink(youtubeurl);
				yv.setPostid(youtubevideoid);
				if(youtubetitle.length()>1000){
					youtubetitle = youtubetitle.substring(0,900);
				}
				yv.setPosttext(youtubetitle);

				String sentimentT = ytvo.getSentimentrating();
				yv.setSentimentrating(sentimentT);

				int sentimentratingT = ytvo.getSentimentscore();
				yv.setSentimentscore(new Long(sentimentratingT));

				String sentimentStr = sentimentT.toLowerCase();

				// Update sc
				if (sentimentStr.indexOf("positive") > 0) {
					Long p = sc.getPositivevoices();
					if (p == null) {
						p = new Long(0);
					}
					p = p + 1;
					sc.setPositivevoices(p);
				}
				if (sentimentStr.indexOf("negative") > 0) {
					Long n = sc.getNegativevoices();
					if (n == null) {
						n = new Long(0);
					}
					n = n + 1;
					sc.setNegativevoices(n);
				}
				if (sentimentStr.indexOf("neutral") > 0) {
					Long ne = sc.getNeutralvoices();
					if (ne == null) {
						ne = new Long(0);
					}
					ne = ne + 1;
					sc.setNeutralvoices(ne);
				}

				voicesDAO.addOrUpdateRecord(yv);
			}
			Long voicescount = sc.getVoicescount();
			int youtubevoicescount = YoutubeList.size();
			Long youtubevoicescountLong = new Long(youtubevoicescount);
			if(voicescount!=null){
				Long currentcount = voicescount + youtubevoicescountLong;
				sc.setVoicescount(currentcount);
			}else{
				sc.setVoicescount(youtubevoicescountLong);
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
