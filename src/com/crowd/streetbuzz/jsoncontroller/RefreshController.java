/**
 * 
 */
package com.crowd.streetbuzz.jsoncontroller;

import java.io.PrintWriter;
import java.util.ArrayList;
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
import com.crowd.streetbuzz.dao.implementation.CardVoteModelDAO;
import com.crowd.streetbuzz.dao.implementation.ConversationCardDAO;
import com.crowd.streetbuzz.dao.implementation.UserCategoryMapDAO;
import com.crowd.streetbuzz.dao.implementation.UserDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDetailsDAO;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzz.model.BookMark;
import com.crowd.streetbuzz.model.CardVoteModel;
import com.crowd.streetbuzz.model.ConversationCard;
import com.crowd.streetbuzz.model.User;
import com.crowd.streetbuzz.model.UserCategoryMap;
import com.crowd.streetbuzz.model.Voices;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;

/**
 * @author Atrijit
 *
 */
public class RefreshController implements Controller, Constants{
	private ConversationCardDAO conversationCardDAO;
	
	private UserDAO userDAO;
	private BookMarkDAO bookMarkDAO;
	private VoicesDAO voicesDAO;
	private VoicesDetailsDAO voicesDetailsDAO;
	private UserCategoryMapDAO userCategoryMapDAO;
	private CardVoteModelDAO cardVoteModelDAO;
	private CardCommentsDAO cardCommentsDAO;
	
	public ConversationCardDAO getConversationCardDAO() {
		return conversationCardDAO;
	}

	public void setConversationCardDAO(ConversationCardDAO conversationCardDAO) {
		this.conversationCardDAO = conversationCardDAO;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(true);
		JsonHeader jsonHeader = (JsonHeader)session.getAttribute("jsonHeader");
		
		String useridStrReq = StrUtil.nonNull(jsonHeader.getUserid());
		Long useridReq = new Long(useridStrReq);
		User reqUser = (User)userDAO.getObjectById(useridReq);
		String jsonbody = StrUtil.nonNull((String) session
				.getAttribute("jsonbody"));
		String cardidstr = StrUtil.nonNull(jsonHeader.getCardid());
		Long cardid = new Long(0);
		if(!"".equalsIgnoreCase(cardidstr)){
			cardid = new Long(cardidstr);
		}
		ConversationCard cc = (ConversationCard)conversationCardDAO.getObjectById(cardid);
		cc.setActiontype(ACTIONTYPEREFRESH);
		cc.setAction(ACTIONYES);
		conversationCardDAO.addOrUpdateRecord(cc);
		ConversationCard newcc = (ConversationCard)conversationCardDAO.getObjectByUniqueId(cc.getUniqueid());
		
//		 Hitherto missing fields
		String shareurl = CARDSHAREURL + newcc.getId().toString();
		newcc.setShareurl(shareurl);
		newcc.setIsbookmarked(false);
		newcc.setUsername(reqUser.getName());
		newcc.setAvatar(reqUser.getAvatar());
		
//		 TODO Also to set distribution list size.
		List userList = userDAO.getAllRecords();
		int distributionsize = (userList.size() - 1);
		newcc.setDistributionsize(new Long(distributionsize));
		
		//Channels etc
		List voicesList = voicesDAO.getAllRecordsbyCardId(newcc.getId());

		List voicesDetailsList = voicesDetailsDAO
				.getAllRecordsbyCardid(newcc.getId());

		Map channelMap = new HashMap();

		for (int j = 0; j < voicesList.size(); j++) {
			Voices temp = (Voices) voicesList.get(j);

			String channel = StrUtil.nonNull(temp.getChannel());
			if (channelMap.containsKey(channel)) {
				Long count = (Long) channelMap.get(channel);
				count = count + 1;
				channelMap.put(channel, count);
			} else {
				channelMap.put(channel, new Long(1));
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
		newcc.setChannel(channelList);
		
		//
		//newcc.setUsername("");
		newcc.setUserId(new Long(newcc.getUserid()));
		BookMark bm = (BookMark) bookMarkDAO.getObjectByUserIdAndCardId(
				new Long(cc.getUserid()), newcc.getId());
		if (bm != null) {
			newcc.setIsbookmarked(true);
		}
		newcc.setIsduplicate(new Long(0));

		List userinterestlist = userCategoryMapDAO
				.getAllCategoriesforUser(new Long(newcc.getUserid()));
		List interestidlist = new ArrayList();
		if (userinterestlist != null) {
			for (int j = 0; j < userinterestlist.size(); j++) {
				UserCategoryMap ucm = (UserCategoryMap) userinterestlist
						.get(j);
				Long interestidcc = ucm.getCategoryid();
				int interestidint = interestidcc.intValue();

				if (!interestidlist.contains(interestidcc)) {
					if (interestidint < 17) {
						interestidlist.add(interestidcc);
					}

				}
			}
		}
		newcc.setUserinterest(interestidlist);
		
		
		/*List cvmList = cardVoteModelDAO.getAllRecordsByCardidUserid(
				newcc.getId(), new Long(cc.getUserid()));*/
		
		/*int isvoted = 0;
		if (cvmList != null && cvmList.size() > 0) {
			CardVoteModel cvm = (CardVoteModel) cvmList.get(0);
			Integer gotvote = cvm.getVote();
			int gotvoteint = gotvote.intValue();
			if (gotvoteint == 1) {
				newcc.setIsupvoted(true);
			}
			if (gotvoteint == 0) {
				newcc.setIsdownvoted(true);
			}
		}*/
		List commentsList = cardCommentsDAO.getAllRecordsbyCardid(cc.getId());
		if(commentsList!=null){
			newcc.setCommentscount(new Long(commentsList.size()));
		}else{
			newcc.setCommentscount(new Long(0));
		}
		
		List upvotelist = cardVoteModelDAO.getAllUpRecordsByCardid(newcc.getId());
		List downvotelist = cardVoteModelDAO.getAllDownRecordsByCardid(newcc.getId());
		newcc.setIsupvoted(false);
		newcc.setIsdownvoted(false);
		
		for (int h=0;h<upvotelist.size();h++){
			CardVoteModel cvm = (CardVoteModel) upvotelist.get(h);
			Long useridcvmvote = cvm.getUserid();
			int useridcvmvoteint = useridcvmvote.intValue();
			int useridint = useridReq.intValue();
			if (useridint == useridcvmvoteint) {
				cc.setIsupvoted(true);
				cc.setIsdownvoted(false);
				break;
			}
		}
		
		for (int h=0;h<downvotelist.size();h++){
			CardVoteModel cvm = (CardVoteModel) downvotelist.get(h);
			Long useridcvmvote = cvm.getUserid();
			int useridcvmvoteint = useridcvmvote.intValue();
			int useridint = useridReq.intValue();
			if (useridint == useridcvmvoteint) {
				cc.setIsupvoted(false);
				cc.setIsdownvoted(true);
				break;
			}
		}
		
		newcc.setUpvotecount(new Long(upvotelist.size()));
		newcc.setDownvotecount(new Long(downvotelist.size()));
		newcc.setIsowner(true);
		//Upto this for hithero missing values
		
		
		Gson gson = new Gson();
		String jsonresponsestr = gson.toJson(newcc); 
		response.setStatus(200);
		response.addHeader("Access-Control-Allow-Origin", "*");
		PrintWriter writer = response.getWriter();
		writer.write(jsonresponsestr);
		writer.close();
		return null;
	}

	public BookMarkDAO getBookMarkDAO() {
		return bookMarkDAO;
	}

	public void setBookMarkDAO(BookMarkDAO bookMarkDAO) {
		this.bookMarkDAO = bookMarkDAO;
	}

	public CardCommentsDAO getCardCommentsDAO() {
		return cardCommentsDAO;
	}

	public void setCardCommentsDAO(CardCommentsDAO cardCommentsDAO) {
		this.cardCommentsDAO = cardCommentsDAO;
	}

	public CardVoteModelDAO getCardVoteModelDAO() {
		return cardVoteModelDAO;
	}

	public void setCardVoteModelDAO(CardVoteModelDAO cardVoteModelDAO) {
		this.cardVoteModelDAO = cardVoteModelDAO;
	}

	public UserCategoryMapDAO getUserCategoryMapDAO() {
		return userCategoryMapDAO;
	}

	public void setUserCategoryMapDAO(UserCategoryMapDAO userCategoryMapDAO) {
		this.userCategoryMapDAO = userCategoryMapDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
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

}
