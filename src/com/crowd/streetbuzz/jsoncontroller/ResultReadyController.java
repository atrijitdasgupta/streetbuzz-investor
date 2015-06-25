/**
 * 
 */
package com.crowd.streetbuzz.jsoncontroller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.BookMarkDAO;
import com.crowd.streetbuzz.dao.implementation.CardCommentsDAO;
import com.crowd.streetbuzz.dao.implementation.CardExclusionDAO;
import com.crowd.streetbuzz.dao.implementation.CardInterestDetectedMapDAO;
import com.crowd.streetbuzz.dao.implementation.CardShareDAO;
import com.crowd.streetbuzz.dao.implementation.CardVoteModelDAO;
import com.crowd.streetbuzz.dao.implementation.ConversationCardDAO;
import com.crowd.streetbuzz.dao.implementation.ResultReadyDAO;
import com.crowd.streetbuzz.dao.implementation.SearchCardDAO;
import com.crowd.streetbuzz.dao.implementation.UserCategoryMapDAO;
import com.crowd.streetbuzz.dao.implementation.UserDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDetailsDAO;
import com.crowd.streetbuzz.json.JSONdisambiguationtype;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzz.json.ResultReadyJSON;
import com.crowd.streetbuzz.json.ResultReadyJSONResponse;
import com.crowd.streetbuzz.json.ResultReadyJSONResponseCards;
import com.crowd.streetbuzz.model.BookMark;
import com.crowd.streetbuzz.model.CardExclusion;
import com.crowd.streetbuzz.model.CardInterestDetectedMap;
import com.crowd.streetbuzz.model.CardVoteModel;
import com.crowd.streetbuzz.model.ConversationCard;
import com.crowd.streetbuzz.model.ResultReady;
import com.crowd.streetbuzz.model.User;
import com.crowd.streetbuzz.model.UserCategoryMap;
import com.crowd.streetbuzz.model.Voices;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;

/**
 * @author Atrijit
 * 
 */
public class ResultReadyController implements Controller, Constants {
	private ConversationCardDAO conversationCardDAO;

	private SearchCardDAO searchCardDAO;

	private VoicesDAO voicesDAO;

	private UserDAO userDAO;

	private UserCategoryMapDAO userCategoryMapDAO;

	private VoicesDetailsDAO voicesDetailsDAO;

	private BookMarkDAO bookMarkDAO;

	private CardVoteModelDAO cardVoteModelDAO;

	private CardInterestDetectedMapDAO cardInterestDetectedMapDAO;

	private CardExclusionDAO cardExclusionDAO;
	
	private CardCommentsDAO cardCommentsDAO;
	
	private CardShareDAO cardShareDAO;
	
	private ResultReadyDAO resultReadyDAO;

	public CardShareDAO getCardShareDAO() {
		return cardShareDAO;
	}

	public void setCardShareDAO(CardShareDAO cardShareDAO) {
		this.cardShareDAO = cardShareDAO;
	}

	public CardCommentsDAO getCardCommentsDAO() {
		return cardCommentsDAO;
	}

	public void setCardCommentsDAO(CardCommentsDAO cardCommentsDAO) {
		this.cardCommentsDAO = cardCommentsDAO;
	}

	public CardExclusionDAO getCardExclusionDAO() {
		return cardExclusionDAO;
	}

	public void setCardExclusionDAO(CardExclusionDAO cardExclusionDAO) {
		this.cardExclusionDAO = cardExclusionDAO;
	}

	public BookMarkDAO getBookMarkDAO() {
		return bookMarkDAO;
	}

	public void setBookMarkDAO(BookMarkDAO bookMarkDAO) {
		this.bookMarkDAO = bookMarkDAO;
	}

	public CardVoteModelDAO getCardVoteModelDAO() {
		return cardVoteModelDAO;
	}

	public void setCardVoteModelDAO(CardVoteModelDAO cardVoteModelDAO) {
		this.cardVoteModelDAO = cardVoteModelDAO;
	}

	public VoicesDAO getVoicesDAO() {
		return voicesDAO;
	}

	public void setVoicesDAO(VoicesDAO voicesDAO) {
		this.voicesDAO = voicesDAO;
	}

	public VoicesDetailsDAO getVoicesDetailsDAO() {
		return voicesDetailsDAO;
	}

	public void setVoicesDetailsDAO(VoicesDetailsDAO voicesDetailsDAO) {
		this.voicesDetailsDAO = voicesDetailsDAO;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(true);
		JsonHeader jsonHeader = (JsonHeader) session.getAttribute("jsonHeader");
		String jsonbody = (String) session.getAttribute("jsonbody");
		String responseString = "";
		Gson gson = new Gson();
		String useridStr = StrUtil.nonNull(jsonHeader.getUserid());
		Long userid = new Long(0);
		if (!"".equalsIgnoreCase(useridStr)) {
			userid = new Long(useridStr);
		}

		List exclusionlist = cardExclusionDAO.getAllRecordsByUserId(userid);
		List exclusionidlist = new ArrayList();
		for (int j = 0; j < exclusionlist.size(); j++) {
			CardExclusion ce = (CardExclusion) exclusionlist.get(j);
			Long cardid = ce.getCardid();
			exclusionidlist.add(cardid);
		}

		ResultReadyJSON rrj = gson.fromJson(jsonbody, ResultReadyJSON.class);
		List usercatmap = userCategoryMapDAO.getAllCategoriesforUser(userid);

		String ccidstr = StrUtil.nonNull(rrj.getId());
		Long ccid = new Long(0);
		if (!"".equalsIgnoreCase(ccidstr)) {
			ccid = new Long(ccidstr);
		}

		ConversationCard cc = (ConversationCard) conversationCardDAO
				.getObjectById(ccid);
		Long ccinterestid = cc.getInterestid();
		String ccaction = cc.getAction();
		int ccinterestidint = ccinterestid.intValue();

		// SEND INTEREST DATA CASE 1
		if (ccinterestidint == 0) {
			// Send interest data
			List list = cardInterestDetectedMapDAO
					.getAllRecordsByCardId(ccid);
			System.out.println("1. list is: "+list);
			if(list==null){
				System.out.println("1. list is null");
				
			}else{
				System.out.println("1. list is not null");
			}
			List interestlist = new ArrayList();
			boolean nullcase = false;
			if (list != null) {
				System.out.println("FOUND LIST NOT NULL CASE 1");
				
				for (int i = 0; i < list.size(); i++) {
					CardInterestDetectedMap cidm = (CardInterestDetectedMap) list
							.get(i);
					Long processed = cidm.getProcessed();
					int pint = processed.intValue();
					if(pint==1){
						nullcase=true;
						break;
					}
					Long interestid = cidm.getInterestid();
					int check = interestid.intValue();
					if (check != 0) {
						interestlist.add(interestid);
					}

				}
				

			}
		

			ResultReadyJSONResponse rrjr = new ResultReadyJSONResponse();
			rrjr.setType(disambiguationtype);
			JSONdisambiguationtype jd = new JSONdisambiguationtype();
			jd.setId(ccid);
			jd.setInterests(interestlist);
			jd.setTopic(cc.getTopic());
			rrjr.setData(jd);
			responseString = gson.toJson(rrjr);

			response.addHeader("Access-Control-Allow-Origin", "*");
			response.setStatus(200);
			PrintWriter writer = response.getWriter();

			System.out.println("ResultReadyController: " + responseString);
			writer.write(responseString);
			writer.close();
			return null;

		}
		
		if (ccinterestidint != 0 && !ACTIONNO.equalsIgnoreCase(ccaction)) {
			//SEND BLANK
			System.out.println("FOUND LIST NULL CASE 2");
			ResultReadyJSONResponse rrjr = new ResultReadyJSONResponse();
			response.addHeader("Access-Control-Allow-Origin", "*");
			response.setStatus(200);
			PrintWriter writer = response.getWriter();
			responseString = gson.toJson(rrjr);
			System.out.println("ResultReadyController: "
					+ responseString);
			writer.write(responseString);
			writer.close();
			return null;
			
		}
		List rrlist = resultReadyDAO.getAllRecordsByCardIdUserId(userid,ccid);	
		int rrsize = 0;
		if(rrlist!=null){
			rrsize = rrlist.size();
		}
		if(rrsize>0){
			System.out.println("RESULT READY RESPONSE HAS ALREADY BEEN SENT");
			ResultReadyJSONResponseCards rrjrc = new ResultReadyJSONResponseCards();
			response.addHeader("Access-Control-Allow-Origin", "*");
			response.setStatus(200);
			PrintWriter writer = response.getWriter();
			responseString = gson.toJson(rrjrc);
			System.out.println("ResultReadyController: "
					+ responseString);
			writer.write(responseString);
			writer.close();
			return null;
		}
		
		// SEND CARD DATA
		if(ccinterestidint != 0 && ACTIONNO.equalsIgnoreCase(ccaction) ) {
			System.out.println("SHOULD SEND CARD DATA NOW");
			// Send card data
			List convList = callQuery(rrj);
			List finalList = new ArrayList();
			for (int i = 0; i < convList.size(); i++) {
				ConversationCard ccon = (ConversationCard) convList.get(i);
				Long cardid = ccon.getId();
				boolean exclude = false;
				if (exclusionidlist.contains(cardid)) {
					exclude=true;
				}

				List voicesList = voicesDAO.getAllRecordsbyCardId(cardid);

				List voicesDetailsList = voicesDetailsDAO
						.getAllRecordsbyCardid(cardid);

				List<NameValuePair> channelList = new ArrayList<NameValuePair>();
				int reviewcount = cc.getReviewcount().intValue();
				int forumcount = cc.getForumcount().intValue();
				int blogcount = cc.getBlogcount().intValue();
				int fbcount = cc.getFacebookcount().intValue();
				int tweetcount = cc.getTweetcount().intValue();
				int articlecount = cc.getArticlecount().intValue();
				int videocount = cc.getVideocount().intValue();
				int imagecount = cc.getImagecount().intValue();
				
				if(reviewcount>0){
					channelList.add(new BasicNameValuePair(REVIEWCH,new Integer(reviewcount).toString()));
				}
				if(forumcount>0){
					channelList.add(new BasicNameValuePair(FORUMCH,new Integer(forumcount).toString()));
				}
				if(blogcount>0){
					channelList.add(new BasicNameValuePair(BLOGCH,new Integer(blogcount).toString()));
				}
				if(fbcount>0){
					channelList.add(new BasicNameValuePair(FACEBOOKCH,new Integer(fbcount).toString()));
				}
				if(tweetcount>0){
					channelList.add(new BasicNameValuePair(TWEETCH,new Integer(tweetcount).toString()));
				}
				if(articlecount>0){
					channelList.add(new BasicNameValuePair(ARTICLECH,new Integer(articlecount).toString()));
				}
				if(videocount>0){
					channelList.add(new BasicNameValuePair(VIDEOCH,new Integer(videocount).toString()));
				}
				if(imagecount>0){
					channelList.add(new BasicNameValuePair(IMAGECH,new Integer(imagecount).toString()));
				}
				
			
				ccon.setChannel(channelList);
				String ccuserid = cc.getUserid();
				String senderuserid = jsonHeader.getUserid();
				ccuserid = ccuserid.trim();
				senderuserid = senderuserid.trim();

				if (ccuserid.equalsIgnoreCase(senderuserid)) {
					ccon.setIsowner(true);
				} else {
					ccon.setIsowner(false);
				}

				ccon.setSystemcard(false);
				ccon.setUserId(new Long(ccuserid));
				User user = (User) userDAO.getObjectById(new Long(ccuserid));
				ccon.setUsername(StrUtil.nonNull(user.getName()));
				ccon.setAvatar(StrUtil.nonNull(user.getAvatar()));
				ccon.setUserrating(StrUtil.getuserrating());

				// TODO Also to set distribution list size.
				List userList = userDAO.getAllRecords();
				int distributionsize = (userList.size() - 1);
				ccon.setDistributionsize(new Long(distributionsize));

				List userinterestlist = userCategoryMapDAO
						.getAllCategoriesforUser(new Long(ccuserid));
				List interestidlist = new ArrayList();
				if (userinterestlist != null) {
					for (int j = 0; j < userinterestlist.size(); j++) {
						UserCategoryMap ucm = (UserCategoryMap) userinterestlist
								.get(j);
						Long interestid = ucm.getCategoryid();
						int interestidint = interestid.intValue();

						if (!interestidlist.contains(interestid)) {
							if(interestidint<17){
								interestidlist.add(interestid);
							}
							
						}
					}
				}
				ccon.setUserinterest(interestidlist);

				// check if bookmarked by this user
				ccon.setIsbookmarked(false);
				BookMark bm = (BookMark) bookMarkDAO
						.getObjectByUserIdAndCardId(userid, ccon.getId());
				if (bm != null) {
					ccon.setIsbookmarked(true);
				}

				// Check if upvoted by this user

				List cvmList = cardVoteModelDAO.getAllRecordsByCardidUserid(
						cardid, userid);
				ccon.setIsupvoted(false);
				ccon.setIsdownvoted(false);
				int isvoted = 0;
				if (cvmList != null && cvmList.size() > 0) {
					CardVoteModel cvm = (CardVoteModel) cvmList.get(0);
					Integer gotvote = cvm.getVote();
					int gotvoteint = gotvote.intValue();
					if (gotvoteint == 1) {
						ccon.setIsupvoted(true);
					}
					if (gotvoteint == 0) {
						ccon.setIsdownvoted(true);
					}
				}
				List commentsList = cardCommentsDAO.getAllRecordsbyCardid(cardid);
				if(commentsList!=null){
					ccon.setCommentscount(new Long(commentsList.size()));
				}else{
					ccon.setCommentscount(new Long(0));
				}
				String shareurl = CARDSHAREURL+cardid.toString();
				
				ccon.setShareurl(shareurl);
				List sharelist = cardShareDAO.getAllSharesforCardId(cc.getId());
				Long sharecount = new Long(0);
				if(sharelist!=null){
					sharecount = new Long(sharelist.size());
				}
				cc.setSharecount(sharecount);
				
				if(!exclude){
					finalList.add(ccon);
				}
				
			}

			// here we prepare the responseString

			ResultReadyJSONResponseCards rrjrc = new ResultReadyJSONResponseCards();
			rrjrc.setType("CARD");
			rrjrc.setData(finalList);
			responseString = gson.toJson(rrjrc);

		}

		List finalList = new ArrayList();
		Collections.reverse(finalList);
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.setStatus(200);
		PrintWriter writer = response.getWriter();

		System.out.println("ResultReadyController: " + responseString);
		writer.write(responseString);
		writer.close();
		//Set result ready already sent
		ResultReady resultReady = new ResultReady();
		resultReady.setCardid(ccid);
		resultReady.setUserid(userid);
		resultReadyDAO.addOrUpdateRecord(resultReady);
		return null;
	}

	public ModelAndView handleRequestOld(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(true);
		JsonHeader jsonHeader = (JsonHeader) session.getAttribute("jsonHeader");
		String jsonbody = (String) session.getAttribute("jsonbody");
		Gson gson = new Gson();
		ResultReadyJSON rrj = gson.fromJson(jsonbody, ResultReadyJSON.class);

		String useridStr = StrUtil.nonNull(jsonHeader.getUserid());
		Long userid = new Long(0);
		if (!"".equalsIgnoreCase(useridStr)) {
			userid = new Long(useridStr);
		}
		String type = jsonHeader.getCardtype();
		String retStr = "";
		// List searchList = new ArrayList();
		// List convList = conversationCardDAO.getAllReadyRecords();
		List usercatmap = userCategoryMapDAO.getAllCategoriesforUser(userid);
		List convList = callQuery(rrj);
		for (int i = 0; i < convList.size(); i++) {
			ConversationCard cc = (ConversationCard) convList.get(i);
			Long cardid = cc.getId();

			List voicesList = voicesDAO.getAllRecordsbyCardId(cardid);

			List voicesDetailsList = voicesDetailsDAO
					.getAllRecordsbyCardid(cardid);

			Map channelMap = new HashMap();

			for (int j = 0; j < voicesList.size(); j++) {
				Voices temp = (Voices) voicesList.get(j);

				String channel = StrUtil.nonNull(temp.getChannel());
				if (channelMap.containsKey(channel)) {
					Long count = (Long) channelMap.get(channel);
					count = count + 1;
					channelMap.put(channel, count);
				} else {
					channelMap.put(channel, new Long(0));
				}

			}

			// Set channels as well
			Set keyset = channelMap.keySet();
			Iterator it = keyset.iterator();
			List<NameValuePair> channelList = new ArrayList<NameValuePair>();
			while (it.hasNext()) {
				String channel = StrUtil.nonNull((String) it.next());
				Long count = new Long(0);
				if (!"".equalsIgnoreCase(channel)) {
					count = (Long) channelMap.get(channel);
				}
				if (count > 0) {
					channelList.add(new BasicNameValuePair(channel, count
							.toString()));
				}

			}
			cc.setChannel(channelList);
			String ccuserid = cc.getUserid();
			String senderuserid = jsonHeader.getUserid();
			ccuserid = ccuserid.trim();
			senderuserid = senderuserid.trim();

			if (ccuserid.equalsIgnoreCase(senderuserid)) {
				cc.setIsowner(true);
			} else {
				cc.setIsowner(false);
			}

			cc.setSystemcard(false);
			cc.setUserId(new Long(ccuserid));
			User user = (User) userDAO.getObjectById(new Long(ccuserid));
			cc.setUsername(StrUtil.nonNull(user.getName()));
			cc.setAvatar(StrUtil.nonNull(user.getAvatar()));
			cc.setUserrating(StrUtil.getuserrating());

			// TODO Also to set distribution list size.
			List userList = userDAO.getAllRecords();
			int distributionsize = (userList.size() - 1);
			cc.setDistributionsize(new Long(distributionsize));

			List userinterestlist = userCategoryMapDAO
					.getAllCategoriesforUser(new Long(ccuserid));
			List interestidlist = new ArrayList();
			if (userinterestlist != null) {
				for (int j = 0; j < userinterestlist.size(); j++) {
					UserCategoryMap ucm = (UserCategoryMap) userinterestlist
							.get(j);
					Long interestid = ucm.getCategoryid();
					String interestidStr = interestid.toString();

					if (!interestidlist.contains(interestid)) {
						interestidlist.add(interestid);
					}
				}
			}
			cc.setUserinterest(interestidlist);

			// check if bookmarked by this user
			cc.setIsbookmarked(false);
			BookMark bm = (BookMark) bookMarkDAO.getObjectByUserIdAndCardId(
					userid, cc.getId());
			if (bm != null) {
				cc.setIsbookmarked(true);
			}

			// Check if upvoted by this user

			List cvmList = cardVoteModelDAO.getAllRecordsByCardidUserid(cardid,
					userid);
			cc.setIsupvoted(false);
			cc.setIsdownvoted(false);
			int isvoted = 0;
			if (cvmList != null && cvmList.size() > 0) {
				CardVoteModel cvm = (CardVoteModel) cvmList.get(0);
				Integer gotvote = cvm.getVote();
				int gotvoteint = gotvote.intValue();
				if (gotvoteint == 1) {
					cc.setIsupvoted(true);
				}
				if (gotvoteint == 0) {
					cc.setIsdownvoted(true);
				}
			}
			List upvotelist = cardVoteModelDAO.getAllUpRecordsByCardid(cardid);
			List downvotelist = cardVoteModelDAO.getAllDownRecordsByCardid(cardid);
			cc.setUpvotecount(new Long(upvotelist.size()));
			cc.setDownvotecount(new Long(downvotelist.size()));
		}

		List finalList = new ArrayList();

		// finalList.addAll(convList);
		finalList.addAll(convList);
		Collections.reverse(finalList);

		response.addHeader("Access-Control-Allow-Origin", "*");
		response.setStatus(200);
		PrintWriter writer = response.getWriter();
		retStr = gson.toJson(finalList);
		System.out.println("ResultReadyController: " + retStr);
		writer.write(retStr);
		writer.close();
		return null;

	}

	// private List callQuery(ResultReadyJSON rrj, Long userid, List usercatmap)
	// {
	private List callQuery(ResultReadyJSON rrj) {
		// String perpage = StrUtil.nonNull(grcj.getPerpage());
		/*
		 * String perpage = "5"; List list = new ArrayList(); for (int i = 0; i <
		 * usercatmap.size(); i++) { UserCategoryMap ucm = (UserCategoryMap)
		 * usercatmap.get(i); Long interestid = ucm.getCategoryid(); List temp =
		 * conversationCardDAO.getAllReadyRecordsPerPageInterest( new
		 * Long(perpage).intValue(), interestid); boolean bool =
		 * list.addAll(temp); }
		 */
		List list = new ArrayList();
		String idStr = StrUtil.nonNull(rrj.getId());
		Long id = new Long(0);
		if (!"".equalsIgnoreCase(idStr)) {
			id = new Long(idStr);
		}
		ConversationCard cc = (ConversationCard) conversationCardDAO
				.getObjectById(id);
		String action = "";
		if (cc != null) {
			action = StrUtil.nonNull(cc.getAction());
		}

		if (ACTIONNO.equalsIgnoreCase(action)) {
			list.add(cc);
		}

		return list;
	}

	public ConversationCardDAO getConversationCardDAO() {
		return conversationCardDAO;
	}

	public void setConversationCardDAO(ConversationCardDAO conversationCardDAO) {
		this.conversationCardDAO = conversationCardDAO;
	}

	public SearchCardDAO getSearchCardDAO() {
		return searchCardDAO;
	}

	public void setSearchCardDAO(SearchCardDAO searchCardDAO) {
		this.searchCardDAO = searchCardDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public UserCategoryMapDAO getUserCategoryMapDAO() {
		return userCategoryMapDAO;
	}

	public void setUserCategoryMapDAO(UserCategoryMapDAO userCategoryMapDAO) {
		this.userCategoryMapDAO = userCategoryMapDAO;
	}

	public CardInterestDetectedMapDAO getCardInterestDetectedMapDAO() {
		return cardInterestDetectedMapDAO;
	}

	public void setCardInterestDetectedMapDAO(
			CardInterestDetectedMapDAO cardInterestDetectedMapDAO) {
		this.cardInterestDetectedMapDAO = cardInterestDetectedMapDAO;
	}

	public ResultReadyDAO getResultReadyDAO() {
		return resultReadyDAO;
	}

	public void setResultReadyDAO(ResultReadyDAO resultReadyDAO) {
		this.resultReadyDAO = resultReadyDAO;
	}

}
