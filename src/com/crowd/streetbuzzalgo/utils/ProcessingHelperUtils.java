/**
 * 
 */
package com.crowd.streetbuzzalgo.utils;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.VoicesDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDetailsDAO;
import com.crowd.streetbuzz.model.ConversationCard;
import com.crowd.streetbuzz.model.SearchCard;
import com.crowd.streetbuzz.model.Voices;
import com.crowd.streetbuzz.model.VoicesDetails;
import com.crowd.streetbuzzalgo.crawler.vo.CrawlerVO;
import com.crowd.streetbuzzalgo.vo.SearchVO;
import com.restfb.types.Group;

/**
 * @author Atrijit
 * 
 */
public class ProcessingHelperUtils implements Constants {

	/**
	 * 
	 */
	public ProcessingHelperUtils() {
		// TODO Auto-generated constructor stub
	}

	public static SearchCard spotateSearchCard(SearchCard sc, List twitterList,
			Map convertedFBMYPost, Map converedFBGrouppost,
			Map sitesearchresult, VoicesDAO voicesDAO,
			VoicesDetailsDAO voicesDetailsDAO) {
		// Process Facebook posts

		Set set = convertedFBMYPost.keySet();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			SearchVO svopost = (SearchVO) it.next();

			Voices voices = new Voices();

			String author = sc.getAuthor();
			voices.setAuthor(author);

			String userid = sc.getUserid();
			voices.setUserid(new Long(userid));

			voices.setCardtype(SEARCHTYPE);
			
			String carduniqueid = sc.getUniqueid();
			voices.setCarduniqueid(carduniqueid);

			Long cardid = sc.getId();
			voices.setCardid(cardid);

			voices.setSource(FACEBOOK);
			voices.setVoicetype("FBPOST");

			String posttext = svopost.getText();
			voices.setPosttext(posttext);

			String sentiment = svopost.getSentiment();
			int sentimentscore = svopost.getSentimentscore();

			voices.setSentimentrating(sentiment);
			voices.setSentimentscore(new Long(sentimentscore));

			String posttextauthor = svopost.getCommenter();
			String postauthorid = svopost.getCommenterid();
			voices.setPosttextauthor(posttextauthor);
			voices.setPostauthorid(postauthorid);

			String postid = svopost.getId();
			voices.setPostid(postid);

			Date voicesdate = svopost.getCommentdate();
			if (voicesdate != null) {
				voices.setVoicesdate(voicesdate);
			} else {
				voices.setVoicesdate(new Date());
			}

			String uniquevoiceid = StrUtil.getUniqueId();
			voices.setUniquevoiceid(uniquevoiceid);

			voicesDAO.addOrUpdateRecord(voices);
			// The Voices object just created
			Voices justvoices = (Voices) voicesDAO
					.getObjectByUniqueId(uniquevoiceid);
			Long justvoicesid = justvoices.getId();

			// now get the comments
			List commentsHoldingList = (List) convertedFBMYPost.get(svopost);
			if (commentsHoldingList != null && commentsHoldingList.size() > 0) {
				for (int i = 0; i < commentsHoldingList.size(); i++) {
					SearchVO svocomment = (SearchVO) commentsHoldingList.get(i);
					VoicesDetails vd = new VoicesDetails();

					String positivephrase = svocomment.getPositivephrase();
					vd.setPositivephrase(positivephrase);

					String neutralphrase = svocomment.getNeutralphrase();
					vd.setNeutralphrase(neutralphrase);

					String negativephrase = svocomment.getNegativephrase();
					vd.setNegativephrase(negativephrase);

					String postauthoridc = svocomment.getCommenterid();
					vd.setPostauthorid(postauthoridc);

					String postc = svocomment.getText();
					vd.setPosttext(postc);

					String personc = svocomment.getCommenter();
					vd.setPosttextauthor(personc);

					String sentimentc = svocomment.getSentiment();
					vd.setSentimentrating(sentimentc);

					int sentimentscorec = svocomment.getSentimentscore();
					vd.setSentimentscore(new Long(sentimentscorec));

					Date datec = svocomment.getCommentdate();
					if (datec != null) {
						vd.setVoicedate(datec);
					} else {
						vd.setVoicedate(new Date());
					}

					vd.setVoicesid(justvoicesid);

					voicesDetailsDAO.addOrUpdateRecord(vd);

				}
				// TODO we have to update the justvoices object here with counts
				// etc and persist it

				// justvoices.setCommentscount(commentscount);
				// justvoices.setExtcommentscount(extcommentscount);
				// justvoices.setLikescount(likescount);
				// justvoices.setExtlikescount(extlikescount);
				// justvoices.setExtviewscount(extviewscount);
				// justvoices.setThumbsdowncount(thumbsdowncount);
				// justvoices.setThumbsupcount(thumbsupcount);

				voicesDAO.addOrUpdateRecord(justvoices);
			}
		}

		// FB Groups
		Set gset = converedFBGrouppost.keySet();
		Iterator git = gset.iterator();
		while (git.hasNext()) {
			Group group = (Group) it.next();
			String fbgroupid = group.getId();
			Map postMap = (Map) converedFBGrouppost.get(group);
			Set pset = postMap.keySet();
			Iterator pit = pset.iterator();
			while (pit.hasNext()) {
				SearchVO svogpost = (SearchVO) pit.next();

				Voices voices = new Voices();

				String author = sc.getAuthor();
				voices.setAuthor(author);

				String userid = sc.getUserid();
				voices.setUserid(new Long(userid));

				voices.setCardtype(SEARCHTYPE);
				
				String carduniqueid = sc.getUniqueid();
				voices.setCarduniqueid(carduniqueid);

				Long cardid = sc.getId();
				voices.setCardid(cardid);

				voices.setSource(FACEBOOK);
				voices.setVoicetype("FBPOST");

				String posttext = svogpost.getText();
				voices.setPosttext(posttext);

				String sentiment = svogpost.getSentiment();
				int sentimentscore = svogpost.getSentimentscore();

				voices.setSentimentrating(sentiment);
				voices.setSentimentscore(new Long(sentimentscore));

				String posttextauthor = svogpost.getCommenter();
				String postauthorid = svogpost.getCommenterid();
				voices.setPosttextauthor(posttextauthor);
				voices.setPostauthorid(postauthorid);

				String postid = svogpost.getId();
				voices.setPostid(postid);

				Date voicesdate = svogpost.getCommentdate();
				if (voicesdate != null) {
					voices.setVoicesdate(voicesdate);
				} else {
					voices.setVoicesdate(new Date());
				}

				voices.setFbgroupid(fbgroupid);

				String uniquevoiceid = StrUtil.getUniqueId();
				voices.setUniquevoiceid(uniquevoiceid);

				voicesDAO.addOrUpdateRecord(voices);
				// The Voices object just created
				Voices justgrvoices = (Voices) voicesDAO
						.getObjectByUniqueId(uniquevoiceid);
				Long justgrvoicesid = justgrvoices.getId();

				// now get the comments
				List commentsHoldingList = (List) postMap.get(svogpost);
				if (commentsHoldingList != null
						&& commentsHoldingList.size() > 0) {
					for (int k = 0; k < commentsHoldingList.size(); k++) {
						SearchVO svogc = (SearchVO) commentsHoldingList.get(k);

						VoicesDetails vd = new VoicesDetails();

						String positivephrase = svogc.getPositivephrase();
						vd.setPositivephrase(positivephrase);

						String neutralphrase = svogc.getNeutralphrase();
						vd.setNeutralphrase(neutralphrase);

						String negativephrase = svogc.getNegativephrase();
						vd.setNegativephrase(negativephrase);

						String postauthoridc = svogc.getCommenterid();
						vd.setPostauthorid(postauthoridc);

						String postc = svogc.getText();
						vd.setPosttext(postc);

						String personc = svogc.getCommenter();
						vd.setPosttextauthor(personc);

						String sentimentc = svogc.getSentiment();
						vd.setSentimentrating(sentimentc);

						int sentimentscorec = svogc.getSentimentscore();
						vd.setSentimentscore(new Long(sentimentscorec));

						Date datec = svogc.getCommentdate();
						if (datec != null) {
							vd.setVoicedate(datec);
						} else {
							vd.setVoicedate(new Date());
						}

						vd.setVoicesid(justgrvoicesid);

						voicesDetailsDAO.addOrUpdateRecord(vd);
					}

					// TODO we have to update the justvoices object here with
					// counts etc and persist it

					// justgrvoices.setCommentscount(commentscount);
					// justgrvoices.setExtcommentscount(extcommentscount);
					// justgrvoices.setLikescount(likescount);
					// justgrvoices.setExtlikescount(extlikescount);
					// justgrvoices.setExtviewscount(extviewscount);
					// justgrvoices.setThumbsdowncount(thumbsdowncount);
					// justgrvoices.setThumbsupcount(thumbsupcount);

					voicesDAO.addOrUpdateRecord(justgrvoices);
				}

			}
		}

		// Twitter
		if (twitterList != null && twitterList.size() > 0) {
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
			tv.setVoicetype("TWITTER");

			voicesDAO.addOrUpdateRecord(tv);
			// The Voices object just created
			Voices justtv = (Voices) voicesDAO
					.getObjectByUniqueId(uniquevoiceid);
			Long justtvid = justtv.getId();

			for (int g = 0; g < twitterList.size(); g++) {
				SearchVO svotwit = (SearchVO) twitterList.get(g);
				VoicesDetails vdt = new VoicesDetails();

				String negativephrase = svotwit.getNegativephrase();
				vdt.setNegativephrase(negativephrase);

				String neutralphrase = svotwit.getNeutralphrase();
				vdt.setNeutralphrase(neutralphrase);

				String positivephrase = svotwit.getPositivephrase();
				vdt.setPositivephrase(positivephrase);

				String nameidT = svotwit.getCommenterid();
				vdt.setPostauthorid(nameidT);

				String twitT = svotwit.getText();
				vdt.setPosttext(twitT);

				String nameT = svotwit.getCommenter();
				vdt.setPosttextauthor(nameT);

				String sentimentT = svotwit.getSentiment();
				vdt.setSentimentrating(sentimentT);

				int sentimentratingT = svotwit.getSentimentscore();
				vdt.setSentimentscore(new Long(sentimentratingT));

				Date dateT = svotwit.getCommentdate();
				if (dateT != null) {
					vdt.setVoicedate(dateT);
				} else {
					vdt.setVoicedate(new Date());
				}

				vdt.setVoicesid(justtvid);

			}
			// TODO we have to update the justvoices object here with counts etc
			// and persist it

			// justtv.setCommentscount(commentscount);
			// justtv.setExtcommentscount(extcommentscount);
			// justtv.setLikescount(likescount);
			// justtv.setExtlikescount(extlikescount);
			// justtv.setExtviewscount(extviewscount);
			// justtv.setThumbsdowncount(thumbsdowncount);
			// justtv.setThumbsupcount(thumbsupcount);

			voicesDAO.addOrUpdateRecord(justtv);
		}

		// Web Sites
		Set keyset = sitesearchresult.keySet();
		Iterator wit = keyset.iterator();
		while (wit.hasNext()) {
			String label = (String) it.next();
			Map map = (Map) sitesearchresult.get(label);

			Set innerkeyset = map.keySet();
			Iterator innerit = innerkeyset.iterator();
			while (innerit.hasNext()) {
				String url = (String) innerit.next();
				List list = (List) map.get(url);
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
				vw.setVoicetype(label);
				vw.setSourcelink(url);

				String uniquevoicesidw = StrUtil.getUniqueId();
				vw.setUniquevoiceid(uniquevoicesidw);

				voicesDAO.addOrUpdateRecord(vw);
				Voices justvw = (Voices) voicesDAO
						.getObjectByUniqueId(uniquevoicesidw);
				Long justvwid = justvw.getId();

				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						CrawlerVO cvo = (CrawlerVO) list.get(i);
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
		}
		return sc;
	}

	public static ConversationCard spotateConversationCard(ConversationCard cc,
			List twitterList, Map convertedFBMYPost, Map converedFBGrouppost,
			Map sitesearchresult, VoicesDAO voicesDAO,
			VoicesDetailsDAO voicesDetailsDAO) {
		// Process Facebook posts

		Set set = convertedFBMYPost.keySet();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			SearchVO svopost = (SearchVO) it.next();

			Voices voices = new Voices();

			String author = cc.getAuthor();
			voices.setAuthor(author);

			String userid = cc.getUserid();
			voices.setUserid(new Long(userid));

			voices.setCardtype(CONVERSATIONTYPE);
			
			String carduniqueid = cc.getUniqueid();
			voices.setCarduniqueid(carduniqueid);

			Long cardid = cc.getId();
			voices.setCardid(cardid);

			voices.setSource(FACEBOOK);
			voices.setVoicetype("FBPOST");

			String posttext = svopost.getText();
			voices.setPosttext(posttext);

			String sentiment = svopost.getSentiment();
			int sentimentscore = svopost.getSentimentscore();

			voices.setSentimentrating(sentiment);
			voices.setSentimentscore(new Long(sentimentscore));

			String posttextauthor = svopost.getCommenter();
			String postauthorid = svopost.getCommenterid();
			voices.setPosttextauthor(posttextauthor);
			voices.setPostauthorid(postauthorid);

			String postid = svopost.getId();
			voices.setPostid(postid);

			Date voicesdate = svopost.getCommentdate();
			if (voicesdate != null) {
				voices.setVoicesdate(voicesdate);
			} else {
				voices.setVoicesdate(new Date());
			}

			String uniquevoiceid = StrUtil.getUniqueId();
			voices.setUniquevoiceid(uniquevoiceid);

			voicesDAO.addOrUpdateRecord(voices);
			// The Voices object just created
			Voices justvoices = (Voices) voicesDAO
					.getObjectByUniqueId(uniquevoiceid);
			Long justvoicesid = justvoices.getId();

			// now get the comments
			List commentsHoldingList = (List) convertedFBMYPost.get(svopost);
			if (commentsHoldingList != null && commentsHoldingList.size() > 0) {
				for (int i = 0; i < commentsHoldingList.size(); i++) {
					SearchVO svocomment = (SearchVO) commentsHoldingList.get(i);
					VoicesDetails vd = new VoicesDetails();

					String positivephrase = svocomment.getPositivephrase();
					vd.setPositivephrase(positivephrase);

					String neutralphrase = svocomment.getNeutralphrase();
					vd.setNeutralphrase(neutralphrase);

					String negativephrase = svocomment.getNegativephrase();
					vd.setNegativephrase(negativephrase);

					String postauthoridc = svocomment.getCommenterid();
					vd.setPostauthorid(postauthoridc);

					String postc = svocomment.getText();
					vd.setPosttext(postc);

					String personc = svocomment.getCommenter();
					vd.setPosttextauthor(personc);

					String sentimentc = svocomment.getSentiment();
					vd.setSentimentrating(sentimentc);

					int sentimentscorec = svocomment.getSentimentscore();
					vd.setSentimentscore(new Long(sentimentscorec));

					Date datec = svocomment.getCommentdate();
					if (datec != null) {
						vd.setVoicedate(datec);
					} else {
						vd.setVoicedate(new Date());
					}

					vd.setVoicesid(justvoicesid);

					voicesDetailsDAO.addOrUpdateRecord(vd);

				}
				// TODO we have to update the justvoices object here with counts
				// etc and persist it

				// justvoices.setCommentscount(commentscount);
				// justvoices.setExtcommentscount(extcommentscount);
				// justvoices.setLikescount(likescount);
				// justvoices.setExtlikescount(extlikescount);
				// justvoices.setExtviewscount(extviewscount);
				// justvoices.setThumbsdowncount(thumbsdowncount);
				// justvoices.setThumbsupcount(thumbsupcount);

				voicesDAO.addOrUpdateRecord(justvoices);
			}
		}

		// FB Groups
		Set gset = converedFBGrouppost.keySet();
		Iterator git = gset.iterator();
		while (git.hasNext()) {
			Group group = (Group) it.next();
			String fbgroupid = group.getId();
			Map postMap = (Map) converedFBGrouppost.get(group);
			Set pset = postMap.keySet();
			Iterator pit = pset.iterator();
			while (pit.hasNext()) {
				SearchVO svogpost = (SearchVO) pit.next();

				Voices voices = new Voices();

				String author = cc.getAuthor();
				voices.setAuthor(author);

				String userid = cc.getUserid();
				voices.setUserid(new Long(userid));

				voices.setCardtype(CONVERSATIONTYPE);
				
				String carduniqueid = cc.getUniqueid();
				voices.setCarduniqueid(carduniqueid);

				Long cardid = cc.getId();
				voices.setCardid(cardid);

				voices.setSource(FACEBOOK);
				voices.setVoicetype("FBPOST");

				String posttext = svogpost.getText();
				voices.setPosttext(posttext);

				String sentiment = svogpost.getSentiment();
				int sentimentscore = svogpost.getSentimentscore();

				voices.setSentimentrating(sentiment);
				voices.setSentimentscore(new Long(sentimentscore));

				String posttextauthor = svogpost.getCommenter();
				String postauthorid = svogpost.getCommenterid();
				voices.setPosttextauthor(posttextauthor);
				voices.setPostauthorid(postauthorid);

				String postid = svogpost.getId();
				voices.setPostid(postid);

				Date voicesdate = svogpost.getCommentdate();
				if (voicesdate != null) {
					voices.setVoicesdate(voicesdate);
				} else {
					voices.setVoicesdate(new Date());
				}

				voices.setFbgroupid(fbgroupid);

				String uniquevoiceid = StrUtil.getUniqueId();
				voices.setUniquevoiceid(uniquevoiceid);

				voicesDAO.addOrUpdateRecord(voices);
				// The Voices object just created
				Voices justgrvoices = (Voices) voicesDAO
						.getObjectByUniqueId(uniquevoiceid);
				Long justgrvoicesid = justgrvoices.getId();

				// now get the comments
				List commentsHoldingList = (List) postMap.get(svogpost);
				if (commentsHoldingList != null
						&& commentsHoldingList.size() > 0) {
					for (int k = 0; k < commentsHoldingList.size(); k++) {
						SearchVO svogc = (SearchVO) commentsHoldingList.get(k);

						VoicesDetails vd = new VoicesDetails();

						String positivephrase = svogc.getPositivephrase();
						vd.setPositivephrase(positivephrase);

						String neutralphrase = svogc.getNeutralphrase();
						vd.setNeutralphrase(neutralphrase);

						String negativephrase = svogc.getNegativephrase();
						vd.setNegativephrase(negativephrase);

						String postauthoridc = svogc.getCommenterid();
						vd.setPostauthorid(postauthoridc);

						String postc = svogc.getText();
						vd.setPosttext(postc);

						String personc = svogc.getCommenter();
						vd.setPosttextauthor(personc);

						String sentimentc = svogc.getSentiment();
						vd.setSentimentrating(sentimentc);

						int sentimentscorec = svogc.getSentimentscore();
						vd.setSentimentscore(new Long(sentimentscorec));

						Date datec = svogc.getCommentdate();
						if (datec != null) {
							vd.setVoicedate(datec);
						} else {
							vd.setVoicedate(new Date());
						}

						vd.setVoicesid(justgrvoicesid);

						voicesDetailsDAO.addOrUpdateRecord(vd);
					}

					// TODO we have to update the justvoices object here with
					// counts etc and persist it

					// justgrvoices.setCommentscount(commentscount);
					// justgrvoices.setExtcommentscount(extcommentscount);
					// justgrvoices.setLikescount(likescount);
					// justgrvoices.setExtlikescount(extlikescount);
					// justgrvoices.setExtviewscount(extviewscount);
					// justgrvoices.setThumbsdowncount(thumbsdowncount);
					// justgrvoices.setThumbsupcount(thumbsupcount);

					voicesDAO.addOrUpdateRecord(justgrvoices);
				}

			}
		}

		// Twitter
		if (twitterList != null && twitterList.size() > 0) {
			Voices tv = new Voices();

			String author = cc.getAuthor();
			tv.setAuthor(author);

			Long cardid = cc.getId();
			tv.setCardid(cardid);

			tv.setCardtype(CONVERSATIONTYPE);
			
			String carduniqueid = cc.getUniqueid();
			tv.setCarduniqueid(carduniqueid);

			tv.setSource(TWITTER);

			String uniquevoiceid = StrUtil.getUniqueId();
			tv.setUniquevoiceid(uniquevoiceid);

			String userid = cc.getUserid();
			tv.setUserid(new Long(userid));

			tv.setVoicesdate(new Date());
			tv.setVoicetype("TWITTER");

			voicesDAO.addOrUpdateRecord(tv);
			// The Voices object just created
			Voices justtv = (Voices) voicesDAO
					.getObjectByUniqueId(uniquevoiceid);
			Long justtvid = justtv.getId();

			for (int g = 0; g < twitterList.size(); g++) {
				SearchVO svotwit = (SearchVO) twitterList.get(g);
				VoicesDetails vdt = new VoicesDetails();

				String negativephrase = svotwit.getNegativephrase();
				vdt.setNegativephrase(negativephrase);

				String neutralphrase = svotwit.getNeutralphrase();
				vdt.setNeutralphrase(neutralphrase);

				String positivephrase = svotwit.getPositivephrase();
				vdt.setPositivephrase(positivephrase);

				String nameidT = svotwit.getCommenterid();
				vdt.setPostauthorid(nameidT);

				String twitT = svotwit.getText();
				vdt.setPosttext(twitT);

				String nameT = svotwit.getCommenter();
				vdt.setPosttextauthor(nameT);

				String sentimentT = svotwit.getSentiment();
				vdt.setSentimentrating(sentimentT);

				int sentimentratingT = svotwit.getSentimentscore();
				vdt.setSentimentscore(new Long(sentimentratingT));

				Date dateT = svotwit.getCommentdate();
				if (dateT != null) {
					vdt.setVoicedate(dateT);
				} else {
					vdt.setVoicedate(new Date());
				}

				vdt.setVoicesid(justtvid);

			}
			// TODO we have to update the justvoices object here with counts etc
			// and persist it

			// justtv.setCommentscount(commentscount);
			// justtv.setExtcommentscount(extcommentscount);
			// justtv.setLikescount(likescount);
			// justtv.setExtlikescount(extlikescount);
			// justtv.setExtviewscount(extviewscount);
			// justtv.setThumbsdowncount(thumbsdowncount);
			// justtv.setThumbsupcount(thumbsupcount);

			voicesDAO.addOrUpdateRecord(justtv);
		}

		// Web Sites
		Set keyset = sitesearchresult.keySet();
		Iterator wit = keyset.iterator();
		while (wit.hasNext()) {
			String label = (String) it.next();
			Map map = (Map) sitesearchresult.get(label);

			Set innerkeyset = map.keySet();
			Iterator innerit = innerkeyset.iterator();
			while (innerit.hasNext()) {
				String url = (String) innerit.next();
				List list = (List) map.get(url);
				Voices vw = new Voices();

				String author = cc.getAuthor();
				vw.setAuthor(author);

				Long cardid = cc.getId();
				vw.setCardid(cardid);

				vw.setCardtype(CONVERSATIONTYPE);
				
				String carduniqueid = cc.getUniqueid();
				vw.setCarduniqueid(carduniqueid);

				vw.setSource(WEB);

				String userid = cc.getUserid();
				vw.setUserid(new Long(userid));

				vw.setVoicesdate(new Date());
				vw.setVoicetype(label);
				vw.setSourcelink(url);

				String uniquevoicesidw = StrUtil.getUniqueId();
				vw.setUniquevoiceid(uniquevoicesidw);

				voicesDAO.addOrUpdateRecord(vw);
				Voices justvw = (Voices) voicesDAO
						.getObjectByUniqueId(uniquevoicesidw);
				Long justvwid = justvw.getId();

				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						CrawlerVO cvo = (CrawlerVO) list.get(i);
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
		}
		return cc;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
