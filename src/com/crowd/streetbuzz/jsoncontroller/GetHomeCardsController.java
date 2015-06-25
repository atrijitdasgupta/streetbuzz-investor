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
import com.crowd.streetbuzz.dao.implementation.CardExclusionDAO;
import com.crowd.streetbuzz.dao.implementation.CardShareDAO;
import com.crowd.streetbuzz.dao.implementation.CardVoteModelDAO;
import com.crowd.streetbuzz.dao.implementation.ConversationCardDAO;
import com.crowd.streetbuzz.dao.implementation.DistributionDAO;
import com.crowd.streetbuzz.dao.implementation.SearchCardDAO;
import com.crowd.streetbuzz.dao.implementation.UserCategoryMapDAO;
import com.crowd.streetbuzz.dao.implementation.UserDAO;
import com.crowd.streetbuzz.dao.implementation.UserDuplicateCardMapDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDetailsDAO;
import com.crowd.streetbuzz.json.GetReadyCardsJson;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzz.model.BookMark;
import com.crowd.streetbuzz.model.CardExclusion;
import com.crowd.streetbuzz.model.CardVoteModel;
import com.crowd.streetbuzz.model.ConversationCard;
import com.crowd.streetbuzz.model.User;
import com.crowd.streetbuzz.model.UserCategoryMap;
import com.crowd.streetbuzz.model.UserDuplicateCardMap;
import com.crowd.streetbuzz.model.Voices;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;

/**
 * @author Atrijit
 * 
 */
public class GetHomeCardsController implements Controller, Constants {

	private ConversationCardDAO conversationCardDAO;

	private SearchCardDAO searchCardDAO;

	private VoicesDAO voicesDAO;

	private VoicesDetailsDAO voicesDetailsDAO;

	private DistributionDAO distributionDAO;

	private UserDAO userDAO;

	private UserCategoryMapDAO userCategoryMapDAO;

	private BookMarkDAO bookMarkDAO;

	private CardVoteModelDAO cardVoteModelDAO;

	private CardExclusionDAO cardExclusionDAO;

	private CardCommentsDAO cardCommentsDAO;

	private UserDuplicateCardMapDAO userDuplicateCardMapDAO;
	
	private CardShareDAO cardShareDAO;

	public CardShareDAO getCardShareDAO() {
		return cardShareDAO;
	}

	public void setCardShareDAO(CardShareDAO cardShareDAO) {
		this.cardShareDAO = cardShareDAO;
	}

	public UserDuplicateCardMapDAO getUserDuplicateCardMapDAO() {
		return userDuplicateCardMapDAO;
	}

	public void setUserDuplicateCardMapDAO(
			UserDuplicateCardMapDAO userDuplicateCardMapDAO) {
		this.userDuplicateCardMapDAO = userDuplicateCardMapDAO;
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

	public DistributionDAO getDistributionDAO() {
		return distributionDAO;
	}

	public void setDistributionDAO(DistributionDAO distributionDAO) {
		this.distributionDAO = distributionDAO;
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
		System.out.println("Inside GetHomeCardsController ready to spotate");

		HttpSession session = request.getSession(true);
		JsonHeader jsonHeader = (JsonHeader) session.getAttribute("jsonHeader");
		String jsonbody = (String) session.getAttribute("jsonbody");
		Gson gson = new Gson();
		GetReadyCardsJson grcj = gson.fromJson(jsonbody,
				GetReadyCardsJson.class);

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
		

		// TODO add filters here
		List finalList = new ArrayList();
		List convList = new ArrayList();
		try {
			convList = callQuery(grcj, userid);
			System.out.println("convList size: "+convList.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String interestidstr = grcj.getInterestid();
		Long interestid = new Long(interestidstr);
		List duplist = userDuplicateCardMapDAO.getAllRecordsforUserByInterest(userid, interestid);
		List dupIdList = new ArrayList();
		for (int i=0;i<duplist.size();i++){
			UserDuplicateCardMap udcm = (UserDuplicateCardMap)duplist.get(i);
			Long cardid = udcm.getCardid();
			ConversationCard dupcc = (ConversationCard) conversationCardDAO.getObjectById(cardid);
			if(!convList.contains(dupcc)){
				convList.add(dupcc);
			}
		}
		
		for (int i = 0; i < convList.size(); i++) {
			ConversationCard cc = (ConversationCard) convList.get(i);
			Long cardid = new Long(0);
			if(cc!=null){
				cardid = cc.getId();
			}
			
			boolean exclude = false;
			
			if (exclusionidlist.contains(cardid)) {
				exclude = true;
				
			}

			List voicesList = voicesDAO.getAllRecordsbyCardId(cardid);

			List voicesDetailsList = voicesDetailsDAO
					.getAllRecordsbyCardid(cardid);
			
			List<NameValuePair> channelList = new ArrayList<NameValuePair>();
			
			Long reviewcountL = new Long(0);
			try {
				reviewcountL = cc.getReviewcount();
			} catch (Exception e) {
				reviewcountL = new Long(0);
			}
			int reviewcount = reviewcountL.intValue();
			
			Long forumcountL = new Long(0);
			try {
				forumcountL = cc.getForumcount();
			} catch (Exception e) {
				forumcountL = new Long(0);
			}
			int forumcount = forumcountL.intValue();
			
			Long blogcountL = new Long(0);
			try {
				blogcountL = cc.getBlogcount();
			} catch (Exception e) {
				blogcountL = new Long(0);
			}
			int blogcount = blogcountL.intValue();
			
			Long fbcountL = new Long(0);
			try {
				fbcountL = cc.getFacebookcount();
			} catch (Exception e) {
				fbcountL = new Long(0);
			}
			int fbcount = fbcountL.intValue();
			
			Long tweetcountL = new Long(0);
			try {
				tweetcountL = cc.getTweetcount();
			} catch (Exception e) {
				tweetcountL = new Long(0);
			}
			int tweetcount = tweetcountL.intValue();
			
			Long articlecountL = new Long(0);
			try {
				articlecountL = cc.getArticlecount();
			} catch (Exception e) {
				articlecountL = new Long(0);
			}
			int articlecount = articlecountL.intValue();
			
			Long videocountL = new Long(0);
			try {
				videocountL = cc.getVideocount();
			} catch (Exception e) {
				videocountL = new Long(0);
			}
			int videocount = videocountL.intValue();
			
			Long imagecountL = new Long(0);
			try {
				imagecountL = cc.getImagecount();
			} catch (Exception e) {
				imagecountL = new Long(0);
			}
			int imagecount = imagecountL.intValue();
			
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
					Long interestidnew = ucm.getCategoryid();
					int interestidnewint = interestidnew.intValue();
					if (!interestidlist.contains(interestidnew)) {
						if (interestidnewint < 17) {
							interestidlist.add(interestidnew);
						}

					}
				}
			}

			cc.setUserinterest(interestidlist);

			// check if bookmarked by this user
			cc.setIsbookmarked(false);
			BookMark existsbm = (BookMark) bookMarkDAO
					.getObjectByUserIdAndCardId(userid, cc.getId());
			if (existsbm != null) {
				cc.setIsbookmarked(true);
			}

			List upvotelist = cardVoteModelDAO.getAllUpTypeRecordsByCardid(cardid);
			List downvotelist = cardVoteModelDAO
					.getAllDownTypeRecordsByCardid(cardid);
			
			cc.setIsupvoted(false);
			cc.setIsdownvoted(false);
			
			for (int h=0;h<upvotelist.size();h++){
				CardVoteModel cvm = (CardVoteModel) upvotelist.get(h);
				Long useridcvmvote = cvm.getUserid();
				int useridcvmvoteint = useridcvmvote.intValue();
				int useridint = userid.intValue();
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
				int useridint = userid.intValue();
				if (useridint == useridcvmvoteint) {
					cc.setIsupvoted(false);
					cc.setIsdownvoted(true);
					break;
				}
			}
			
			cc.setUpvotecount(new Long(upvotelist.size()));
			cc.setDownvotecount(new Long(downvotelist.size()));
			List commentsList = cardCommentsDAO.getAllRecordsbyCardid(cardid);
			if (commentsList != null) {
				cc.setCommentscount(new Long(commentsList.size()));
			} else {
				cc.setCommentscount(new Long(0));
			}
			String shareurl = CARDSHAREURL + cardid.toString();
			cc.setShareurl(shareurl);
			
			List sharelist = cardShareDAO.getAllSharesforCardId(cc.getId());
			Long sharecount = new Long(0);
			if(sharelist!=null){
				sharecount = new Long(sharelist.size());
			}
			cc.setSharecount(sharecount);
			
			
			if (!exclude) {
				finalList.add(cc);
			}
			

		}
		
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.setStatus(200);
		PrintWriter writer = response.getWriter();
		String retStr = gson.toJson(finalList);
		writer.write(retStr);
		writer.close();
		return null;
	}

	
	private List callQuery(GetReadyCardsJson grcj, Long userid) {
		List convList = new ArrayList();

		String interestidStr = StrUtil.nonNull(grcj.getInterestid());
		if ("".equalsIgnoreCase(interestidStr)) {
			return new ArrayList();
		}
		String perpage = StrUtil.nonNull(grcj.getPerpage());
		String listtype = StrUtil.nonNull(grcj.getListtype());
		String friendidStr = StrUtil.nonNull(grcj.getFriendid());
		String beforeid = StrUtil.nonNull(grcj.getBeforeid());
		String afterid = StrUtil.nonNull(grcj.getAfterid());
		String query = StrUtil.nonNull(grcj.getQuery());
		
		//int perpageint = new Long(perpage).intValue();
		int perpageint = PERPAGECARDS;
		beforeid = beforeid.trim();
		afterid = afterid.trim();
		System.out.println("beforeid: "+beforeid);
		System.out.println("afterid: "+afterid);

		Long interestid = new Long(0);
		Long friendid = new Long(0);
		if (!"".equalsIgnoreCase(interestidStr)) {
			interestid = new Long(interestidStr);
		}
		int interestidint = interestid.intValue();
		if (interestidint == 0) {
			return new ArrayList();
		}
		if (!"".equalsIgnoreCase(friendidStr)) {
			friendid = new Long(friendidStr);
		}
		System.out.println("interestid Long: "+interestid);
		
		
		//Check if it is a Search Request
		if("search".equalsIgnoreCase(listtype)){
			System.out.println("query: "+query);
			if(!"".equalsIgnoreCase(query)){
				query = query.toLowerCase();
				List cardlist = conversationCardDAO.searchCards(interestid,query);
				return cardlist;
			}else{
				return new ArrayList();
			}
		}
		
		
		// Check if it is a bookmark request
		List bookmarklist = new ArrayList();
		if ("bookmark".equalsIgnoreCase(listtype)) {
			System.out.println("Get Home Cards Case Bookmark");
			bookmarklist = bookMarkDAO.getAllRecordsForUserByInterest(userid,
					interestid);
			for (int i = 0; i < bookmarklist.size(); i++) {
				BookMark bm = (BookMark) bookmarklist.get(i);
				Long cardid = bm.getEntityid();
				ConversationCard cc = (ConversationCard) conversationCardDAO
						.getObjectById(cardid);
				String action = StrUtil.nonNull(cc.getAction());
				if (ACTIONNO.equalsIgnoreCase(action)) {
					convList.add(cc);
				}

			}
			return convList;
		}

		// Check if it is a all request
		if ("all".equalsIgnoreCase(listtype) || "".equalsIgnoreCase(listtype)) {
			System.out.println("Get Home Cards Case All");
			if ("".equalsIgnoreCase(afterid)
					&& "".equalsIgnoreCase(beforeid)) {
				convList = conversationCardDAO
						.getAllReadyRecordsPerPageInterest(perpageint,
								new Long(interestid));
				System.out.println("case all convList size:: "
						+ convList.size());
				return convList;
			}
			//scroll down
			if (!"".equalsIgnoreCase(afterid)
					&& "".equalsIgnoreCase(beforeid)) {
				System.out.println("Case all afterid scrolldown");
				convList = conversationCardDAO
						.getAllReadyRecordsPerPageAfterIdInterest(perpageint,
								new Long(interestid), new Long(afterid));
				
				return convList;
			}
			//scroll up
			if ("".equalsIgnoreCase(afterid)
					&& !"".equalsIgnoreCase(beforeid)) {
				System.out.println("Case all beforeid scrollup");
				convList = conversationCardDAO
						.getAllReadyRecordsPerPageBeforeIdInterest(perpageint,
								new Long(interestid), new Long(beforeid));
				return convList;
			}

		}

		// Check if it is a mine request
		if ("mine".equalsIgnoreCase(listtype)) {
			System.out.println("Get Home Cards Case Mine");
			if ("".equalsIgnoreCase(afterid)
					&& "".equalsIgnoreCase(beforeid)) {
				convList = conversationCardDAO
						.getAllReadyRecordsOwnUserPerPageInterest(userid
								.toString(), perpageint, new Long(interestid));
				return convList;
			}
			if (!"".equalsIgnoreCase(afterid)
					&& "".equalsIgnoreCase(beforeid)) {
				convList = conversationCardDAO
						.getAllReadyRecordsOwnUserPerPageAfterIdInterest(userid
								.toString(), perpageint, new Long(afterid),
								new Long(interestid));
				return convList;
			}
			if ("".equalsIgnoreCase(afterid)
					&& !"".equalsIgnoreCase(beforeid)) {
				convList = conversationCardDAO
						.getAllReadyRecordsOwnUserPerPageBeforeIdInterest(
								userid.toString(), perpageint, new Long(
										beforeid), new Long(interestid));
				return convList;
			}

		}

		// Check if it is a friends request
		if ("friends".equalsIgnoreCase(listtype)) {
			System.out.println("Get Home Cards Case Friends");
			if ("".equalsIgnoreCase(friendidStr)) {
				if ("".equalsIgnoreCase(afterid)
						&& "".equalsIgnoreCase(beforeid)) {
					convList = conversationCardDAO
							.getAllReadyRecordsOtherUsersPerPageInterest(userid
									.toString(), perpageint, new Long(
									interestid));
					return convList;
				}
				if (!"".equalsIgnoreCase(afterid)
						&& "".equalsIgnoreCase(beforeid)) {
					convList = conversationCardDAO
							.getAllReadyRecordsOtherUsersPerPageAfterIdInterest(
									userid.toString(), perpageint, new Long(
											afterid), new Long(interestid));
					return convList;
				}
				if ("".equalsIgnoreCase(afterid)
						&& !"".equalsIgnoreCase(beforeid)) {
					convList = conversationCardDAO
							.getAllReadyRecordsOtherUsersPerPageBeforeIdInterest(
									userid.toString(), perpageint, new Long(
											beforeid), new Long(interestid));
					return convList;
				}
			} else {
				if (!"".equalsIgnoreCase(perpage)
						&& "".equalsIgnoreCase(afterid)
						&& "".equalsIgnoreCase(beforeid)) {

				}
				if (!"".equalsIgnoreCase(perpage)
						&& !"".equalsIgnoreCase(afterid)
						&& "".equalsIgnoreCase(beforeid)) {

				}
				if (!"".equalsIgnoreCase(perpage)
						&& "".equalsIgnoreCase(afterid)
						&& !"".equalsIgnoreCase(beforeid)) {

				}
			}

		}

		// Check if it is a acted on request
		if ("discussed".equalsIgnoreCase(listtype)) {
			System.out.println("Get Home Cards Case Discussed");
			if (!"".equalsIgnoreCase(perpage) && "".equalsIgnoreCase(afterid)
					&& "".equalsIgnoreCase(beforeid)) {

			}
			if (!"".equalsIgnoreCase(perpage) && !"".equalsIgnoreCase(afterid)
					&& "".equalsIgnoreCase(beforeid)) {

			}
			if (!"".equalsIgnoreCase(perpage) && "".equalsIgnoreCase(afterid)
					&& !"".equalsIgnoreCase(beforeid)) {

			}
		}

		// Check if it is a follow request
		if ("follow".equalsIgnoreCase(listtype)) {
			System.out.println("Get Home Cards Case Follow");
			if (!"".equalsIgnoreCase(perpage) && "".equalsIgnoreCase(afterid)
					&& "".equalsIgnoreCase(beforeid)) {

			}
			if (!"".equalsIgnoreCase(perpage) && !"".equalsIgnoreCase(afterid)
					&& "".equalsIgnoreCase(beforeid)) {

			}
			if (!"".equalsIgnoreCase(perpage) && "".equalsIgnoreCase(afterid)
					&& !"".equalsIgnoreCase(beforeid)) {

			}
		}

		return convList;
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

	public BookMarkDAO getBookMarkDAO() {
		return bookMarkDAO;
	}

	public void setBookMarkDAO(BookMarkDAO bookMarkDAO) {
		this.bookMarkDAO = bookMarkDAO;
	}

}
