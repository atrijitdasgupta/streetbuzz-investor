/**
 * 
 */
package com.crowd.streetbuzz.jsoncontroller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
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
import com.crowd.streetbuzz.dao.implementation.CardMasterDAO;
import com.crowd.streetbuzz.dao.implementation.CardShareDAO;
import com.crowd.streetbuzz.dao.implementation.CardVoteModelDAO;
import com.crowd.streetbuzz.dao.implementation.CategoryMasterDAO;
import com.crowd.streetbuzz.dao.implementation.ConversationCardDAO;
import com.crowd.streetbuzz.dao.implementation.DistributionDAO;
import com.crowd.streetbuzz.dao.implementation.GCMDAO;
import com.crowd.streetbuzz.dao.implementation.TaxonomyDAO;
import com.crowd.streetbuzz.dao.implementation.UserCategoryMapDAO;
import com.crowd.streetbuzz.dao.implementation.UserDAO;
import com.crowd.streetbuzz.dao.implementation.UserDuplicateCardMapDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDetailsDAO;
import com.crowd.streetbuzz.json.JsonConversation;
import com.crowd.streetbuzz.json.JsonGetCard;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzz.model.BookMark;
import com.crowd.streetbuzz.model.CardComments;
import com.crowd.streetbuzz.model.CardMaster;
import com.crowd.streetbuzz.model.CardVoteModel;
import com.crowd.streetbuzz.model.CategoryMaster;
import com.crowd.streetbuzz.model.ConversationCard;
import com.crowd.streetbuzz.model.GCM;
import com.crowd.streetbuzz.model.User;
import com.crowd.streetbuzz.model.UserCategoryMap;
import com.crowd.streetbuzz.model.UserDuplicateCardMap;
import com.crowd.streetbuzz.model.Voices;
import com.crowd.streetbuzz.utils.GooglePushMessage;
import com.crowd.streetbuzzalgo.deduplicate.Deduplicator;
import com.crowd.streetbuzzalgo.taxonomytree.TraverseTaxonomy;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;

/**
 * @author Atrijit
 * 
 */
public class ConversationSubmitController implements Controller, Constants {

	private CardMasterDAO cardMasterDAO;

	private ConversationCardDAO conversationCardDAO;

	private UserDAO userDAO;

	private GCMDAO gcmDAO;

	private TaxonomyDAO taxonomyDAO;

	private CategoryMasterDAO categoryMasterDAO;

	private UserCategoryMapDAO userCategoryMapDAO;

	private CardCommentsDAO cardCommentsDAO;

	private DistributionDAO distributionDAO;

	private CardExclusionDAO cardExclusionDAO;

	private UserDuplicateCardMapDAO userDuplicateCardMapDAO;

	private CardVoteModelDAO cardVoteModelDAO;
	
	private CardShareDAO cardShareDAO;
	
	
	private BookMarkDAO bookMarkDAO;
	private VoicesDAO voicesDAO;
	private VoicesDetailsDAO voicesDetailsDAO;

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

	public BookMarkDAO getBookMarkDAO() {
		return bookMarkDAO;
	}

	public void setBookMarkDAO(BookMarkDAO bookMarkDAO) {
		this.bookMarkDAO = bookMarkDAO;
	}

	public CardExclusionDAO getCardExclusionDAO() {
		return cardExclusionDAO;
	}

	public void setCardExclusionDAO(CardExclusionDAO cardExclusionDAO) {
		this.cardExclusionDAO = cardExclusionDAO;
	}

	public DistributionDAO getDistributionDAO() {
		return distributionDAO;
	}

	public void setDistributionDAO(DistributionDAO distributionDAO) {
		this.distributionDAO = distributionDAO;
	}

	public CardCommentsDAO getCardCommentsDAO() {
		return cardCommentsDAO;
	}

	public void setCardCommentsDAO(CardCommentsDAO cardCommentsDAO) {
		this.cardCommentsDAO = cardCommentsDAO;
	}

	public TaxonomyDAO getTaxonomyDAO() {
		return taxonomyDAO;
	}

	public void setTaxonomyDAO(TaxonomyDAO taxonomyDAO) {
		this.taxonomyDAO = taxonomyDAO;
	}

	public GCMDAO getGcmDAO() {
		return gcmDAO;
	}

	public void setGcmDAO(GCMDAO gcmDAO) {
		this.gcmDAO = gcmDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public ConversationCardDAO getConversationCardDAO() {
		return conversationCardDAO;
	}

	public void setConversationCardDAO(ConversationCardDAO conversationCardDAO) {
		this.conversationCardDAO = conversationCardDAO;
	}

	public CardMasterDAO getCardMasterDAO() {
		return cardMasterDAO;
	}

	public void setCardMasterDAO(CardMasterDAO cardMasterDAO) {
		this.cardMasterDAO = cardMasterDAO;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean noerror = true;
		boolean isedit = false;
		System.out
				.println("Inside ConversationSubmitController ready to spotate");
		HttpSession session = request.getSession(true);
		JsonHeader jsonHeader = (JsonHeader) session.getAttribute("jsonHeader");
		String cardid = StrUtil.nonNull(jsonHeader.getCardid());
		boolean isnew = false;
		if ("".equalsIgnoreCase(cardid) || "0".equalsIgnoreCase(cardid)) {
			isnew = true;
		}
		
		String jsonbody = StrUtil.nonNull((String) session
				.getAttribute("jsonbody"));

		User u = (User) userDAO.getObjectById(new Long(jsonHeader.getUserid()));
		String rating = "";
		PrintWriter writer = response.getWriter();
		if ("".equalsIgnoreCase(jsonbody)) {
			noerror = false;
			String errMsg = "empty jsonbody";
			response.setStatus(500);
			writer.write(STANDARERRORRESPONSE + errMsg);
			writer.close();
			return null;

		}
		JsonConversation jsonConversation = new JsonConversation();
		Gson gson = new Gson();
		try {
			jsonConversation = (JsonConversation) gson.fromJson(jsonbody,
					JsonConversation.class);
		} catch (Exception e) {
			e.printStackTrace();
			noerror = false;
			String errMsg = e.getMessage();
			response.setStatus(500);
			writer.write(STANDARERRORRESPONSE + errMsg);
			writer.close();
			return null;
		}
		boolean editcase = true;
		if ("".equalsIgnoreCase(cardid) || "0".equalsIgnoreCase(cardid)){
			editcase = false;
		}
			
		// Check for duplicate
		
		String interestid = StrUtil.nonNull(jsonConversation
				.getInterestid());
		interestid = interestid.trim();
		String conversationtopicdup = StrUtil.nonNull(jsonConversation
				.getConversationtopic());
		if(!editcase){
		
		Long duplicatecardid = Deduplicator.deduplicate(conversationCardDAO,
				categoryMasterDAO, conversationtopicdup, interestid);
		System.out.println("Got Duplicate Card ID:: "
				+ duplicatecardid.toString());
		if (duplicatecardid != null) {
			int dupint = duplicatecardid.intValue();
			if (dupint != 0) {
				ConversationCard cc = (ConversationCard) conversationCardDAO
						.getObjectById(duplicatecardid);
				String action = cc.getAction();
				if (ACTIONNO.equalsIgnoreCase(action)) {
					cc.setAction(ACTIONYES);
					cc.setActiontype(ACTIONTYPEREFRESH);
					conversationCardDAO.addOrUpdateRecord(cc);
				}

				String uniqueid = cc.getUniqueid();
				ConversationCard dupcard = (ConversationCard) conversationCardDAO
						.getObjectByUniqueId(uniqueid);

				// Check if the current user has the interestid and
				// subinterestid in his map
				Long categoryid = dupcard.getInterestid();
				Long subcategoryid = dupcard.getSubinterestid();
				Long userid = u.getId();
				UserCategoryMap ucmdup = (UserCategoryMap) userCategoryMapDAO
						.getObjectByCatIdSubCatIdUserId(categoryid, userid,
								subcategoryid);
				if (ucmdup == null) {
					UserCategoryMap ucmdupnew = new UserCategoryMap();
					ucmdupnew.setCategoryid(categoryid);
					ucmdupnew.setSubcategoryid(subcategoryid);
					CategoryMaster cmdup = (CategoryMaster) categoryMasterDAO
							.getObjectById(subcategoryid);
					if (cmdup != null) {
						ucmdupnew.setCategory(cmdup.getCategoryname());
					}
					ucmdupnew.setUserid(userid);
					ucmdupnew.setReputationscore(new Long(0));
					userCategoryMapDAO.addOrUpdateRecord(ucmdupnew);
					
					
//					 Hitherto missing fields
					dupcard.setUsername(u.getName());
					dupcard.setAvatar(u.getAvatar());
					dupcard.setIsowner(false);
					Long senderid = u.getId();
					Long cardownerid = new Long(dupcard.getUserid());
					int senderidint = senderid.intValue();
					int cardowneridint = cardownerid.intValue();
					if(senderidint == cardowneridint){
						dupcard.setIsowner(true);
					}else{
						dupcard.setIsowner(false);
					}
					String shareurl = CARDSHAREURL + dupcard.getId().toString();
					dupcard.setShareurl(shareurl);
					List sharelist = cardShareDAO.getAllSharesforCardId(cc.getId());
					Long sharecount = new Long(0);
					if(sharelist!=null){
						sharecount = new Long(sharelist.size());
					}
					cc.setSharecount(sharecount);
					dupcard.setIsbookmarked(false);
					
//					 TODO Also to set distribution list size.
					List userList = userDAO.getAllRecords();
					int distributionsize = (userList.size() - 1);
					dupcard.setDistributionsize(new Long(distributionsize));
					
					//Channels etc
					List voicesList = voicesDAO.getAllRecordsbyCardId(dupcard.getId());

					List voicesDetailsList = voicesDetailsDAO
							.getAllRecordsbyCardid(dupcard.getId());

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
					
				
					dupcard.setChannel(channelList);
					
					
					//
					
					dupcard.setUserId(new Long(dupcard.getUserid()));
					BookMark bm = (BookMark) bookMarkDAO.getObjectByUserIdAndCardId(
							new Long(dupcard.getUserid()), dupcard.getId());
					if (bm != null) {
						dupcard.setIsbookmarked(true);
					}
					dupcard.setIsduplicate(new Long(0));

					List userinterestlist = userCategoryMapDAO
							.getAllCategoriesforUser(new Long(dupcard.getUserid()));
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
					dupcard.setUserinterest(interestidlist);
					
					
					List cvmList = cardVoteModelDAO.getAllRecordsByCardidUserid(
							dupcard.getId(), new Long(dupcard.getUserid()));
					dupcard.setIsupvoted(false);
					dupcard.setIsdownvoted(false);
					
					List commentsList = cardCommentsDAO.getAllRecordsbyCardid(dupcard.getId());
					if(commentsList!=null){
						dupcard.setCommentscount(new Long(commentsList.size()));
					}else{
						dupcard.setCommentscount(new Long(0));
					}
					
					List upvotelist = cardVoteModelDAO.getAllUpRecordsByCardid(dupcard.getId());
					List downvotelist = cardVoteModelDAO.getAllDownRecordsByCardid(dupcard.getId());
					
					dupcard.setIsupvoted(false);
					dupcard.setIsdownvoted(false);
					for (int h=0;h<upvotelist.size();h++){
						CardVoteModel cvm = (CardVoteModel) upvotelist.get(h);
						Long useridcvmvote = cvm.getUserid();
						int useridcvmvoteint = useridcvmvote.intValue();
						int useridint = u.getId().intValue();
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
						int useridint = u.getId().intValue();
						if (useridint == useridcvmvoteint) {
							cc.setIsupvoted(false);
							cc.setIsdownvoted(true);
							break;
						}
					}
					
					dupcard.setUpvotecount(new Long(upvotelist.size()));
					dupcard.setDownvotecount(new Long(downvotelist.size()));
					//Upto this for hithero missing values
					
					

				}
				// Make an entry in user duplicate card map IF it does not
				// exist
				UserDuplicateCardMap oldudcm = (UserDuplicateCardMap) userDuplicateCardMapDAO
						.getObjectByUserIdCardIdInterestId(userid, dupcard
								.getId(), dupcard.getInterestid());
				if (oldudcm == null) {
					UserDuplicateCardMap udcm = new UserDuplicateCardMap();
					udcm.setUserid(userid);
					udcm.setCardid(dupcard.getId());
					udcm.setInterestid(dupcard.getInterestid());
					userDuplicateCardMapDAO.addOrUpdateRecord(udcm);
				}
				dupcard.setIsduplicate(new Long(1));
				if(isnew){
					String responseStr = gson.toJson(dupcard);
					
					response.setStatus(200);
					response.addHeader("Access-Control-Allow-Origin", "*");
					writer.write(responseStr);
					writer.close();
					return null;
				}
				

			}
		}}
		// Create the unique id
		CardMaster cardMaster = new CardMaster();

		String uniqueidforPushMsg = "";
		String cardidforPushMsg = "";
		String uniqueeidfromheader = StrUtil.nonNull(jsonHeader
				.getCarduniqueid());
		String uniqueidtopass = "";
		// String time = new Long(System.currentTimeMillis()).toString();
		if ("".equalsIgnoreCase(uniqueeidfromheader)) {
			uniqueidtopass = StrUtil.getUniqueId();
			cardMaster.setType(SEARCHTYPE);
			cardMaster.setUniqueid(uniqueidtopass);
			try {
				cardMasterDAO.addOrUpdateRecord(cardMaster);
			} catch (Exception e) {
				noerror = false;
				String errMsg = e.getMessage();
				response.setStatus(500);
				writer.write(STANDARERRORRESPONSE + errMsg);
				writer.close();
				return null;
			}
		} else {
			uniqueidtopass = uniqueeidfromheader;
		}

		ConversationCard cc = null;
		

		if ("".equalsIgnoreCase(cardid) || "0".equalsIgnoreCase(cardid)) {
			// New Card
			cc = new ConversationCard();
			cc.setUniqueid(uniqueidtopass);
			uniqueidforPushMsg = uniqueidtopass;

			// header values
			/*
			 * String city = StrUtil.nonNull(jsonHeader.getCity());
			 * cc.setCity(city);
			 * 
			 * String country = StrUtil.nonNull(jsonHeader.getCountry());
			 * cc.setCountry(country);
			 */

			String latitude = StrUtil.nonNull(jsonHeader.getLatitude());
			String longitude = StrUtil.nonNull(jsonHeader.getLongitude());
			cc.setLatitude(latitude);
			cc.setLongitude(longitude);

			Map locationMap = new HashMap();
			try {
				// locationMap =
				// GeoCodingReverseLookupUtils.reverseLookup(latitude,longitude);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				System.out.println(e1.getMessage());
			}
			String cardcity = "";
			String cardcountry = "";
			if (locationMap != null && locationMap.size() > 1) {
				cardcity = (String) locationMap.get(CITY);
				cardcountry = (String) locationMap.get(COUNTRY);
			}
			cc.setCardcity(cardcity);
			cc.setCardcountry(cardcountry);

			String userid = StrUtil.nonNull(jsonHeader.getUserid());
			cc.setUserid(userid);

			String socialnetwork = StrUtil.nonNull(jsonHeader
					.getSocialnetwork());
			cc.setSocialnetwork(socialnetwork);

			cc.setCardid(cardid);

			// String cardtype = StrUtil.nonNull(jsonHeader.getCardtype());
			cc.setCardtype(CONVERSATIONTYPE);

			String voiceid = StrUtil.nonNull(jsonHeader.getVoiceid());
			cc.setVoiceid(voiceid);

			String source = StrUtil.nonNull(jsonHeader.getSource());
			cc.setSource(source);

			String lastupdate = StrUtil.nonNull(jsonHeader.getLastupdate());
			cc.setLastupdate(lastupdate);

			/*
			 * String beforeid = StrUtil.nonNull(jsonHeader.getBeforeid());
			 * cc.setBeforeid(beforeid);
			 */

			String afterid = StrUtil.nonNull(jsonHeader.getAfterid());
			cc.setAfterid(afterid);

			// Body values
			String conversationtopic = StrUtil.nonNull(jsonConversation
					.getConversationtopic());
			String link = StrUtil.nonNull(jsonConversation.getLink());
			String mediaid = StrUtil.nonNull(jsonConversation.getMediaid());
			String interesttag = StrUtil.nonNull(jsonConversation
					.getInteresttag());
			System.out.println("Got interesttag:" + interesttag);

			interesttag = interesttag.trim();

			
			interestid = interestid.trim();
			Long interestidLong = new Long(0);
			if (!"".equals(interestid)) {
				interestidLong = new Long(interestid);
			}
			cc.setSubinterestid(new Long(0));

			String moreinteresttag = StrUtil.nonNull(jsonConversation
					.getMoreinteresttag());
			rating = StrUtil.nonNull(jsonConversation.getRating());
			String additionalviews = StrUtil.nonNull(jsonConversation
					.getAdditionalviews());
			String[] socialshareon = jsonConversation.getSocialshareon();
			StringBuffer sbfr = new StringBuffer();
			if (socialshareon != null && socialshareon.length > 0) {
				for (int i = 0; i < socialshareon.length; i++) {
					String temp = socialshareon[i];
					sbfr.append(temp + ",");
				}
			} else {
				sbfr.append("");
			}

			String socialshareonStr = sbfr.toString();

			cc.setTopic(conversationtopic);
			cc.setLink(link);
			cc.setMediaid(mediaid);
			cc.setInteresttag(interesttag);
			cc.setMoreinteresttag(moreinteresttag);
			cc.setRating(rating);
			cc.setAdditionalviews(additionalviews);
			cc.setSocialshareon(socialshareonStr);

			// CategoryMaster CM =
			// (CategoryMaster)categoryMasterDAO.getObjectByCategory(interesttag);

			cc.setInterestid(interestidLong);

			// Set Creation Date
			cc.setCreationdate(new Date());

			cc.setAction(ACTIONYES);

			cc.setActiontype(ACTIONTYPENEEW);
			cc.setIssystemcard("false");

			// create new card entry, with partial values
			try {
				conversationCardDAO.addOrUpdateRecord(cc);
				cc = (ConversationCard) conversationCardDAO
						.getObjectByUniqueId(uniqueidtopass);
				
				Long newid = cc.getId();
				cardidforPushMsg = newid.toString();

				// Create an entry for comment if additional views is not an
				// empty String
				boolean postcomment = false;
				if(!"".equalsIgnoreCase(additionalviews)||!"".equalsIgnoreCase(rating)||!"".equalsIgnoreCase(link)){
					postcomment = true;
				}
				if (postcomment) {
					CardComments cCom = new CardComments();
					cCom.setCardid(newid);
					cCom.setCarduniqueid(uniqueidtopass);
					cCom.setComment(additionalviews);
					cCom.setCommentdate(new Date());
					// User u = (User)userDAO.getObjectById(new
					// Long(cc.getUserid()));
					cCom.setCommenter(u.getName());
					cCom.setSbcommenterid(u.getId());
					cCom.setAvatar(u.getAvatar());
					cCom.setMediaid("");
					cCom.setRating(rating);
					cCom.setLink(link);
					cCom.setExtcommentersource("");
					cCom.setExtcommenterid("");
					cardCommentsDAO.addOrUpdateRecord(cCom);
				}
			} catch (Exception e) {
				e.printStackTrace();
				noerror = false;
				String errMsg = e.getMessage();
				response.setStatus(500);
				writer.write(STANDARERRORRESPONSE + errMsg);
				writer.close();
				return null;
			}

		}
		// EDIT CASE
		else {
			// EDIT CASE
			isedit = true;
			// cc = (ConversationCard)
			// conversationCardDAO.getObjectByUniqueId(uniqueidtopass);
			String idStr = jsonHeader.getCardid();
			Long id = new Long(idStr);
			cc = (ConversationCard) conversationCardDAO.getObjectById(id);
			//Check the topic submitted - if it has changed then only fire a search, else not.
			String submittedconversationtopic = StrUtil.nonNull(jsonConversation.getConversationtopic());
			submittedconversationtopic = submittedconversationtopic.trim();
			String existingconversationtopic = StrUtil.nonNull(cc.getTopic());
			existingconversationtopic = existingconversationtopic.trim();
			boolean freshsearch = true;
			if(submittedconversationtopic.equalsIgnoreCase(existingconversationtopic)){
				freshsearch = false;
			}
			
			uniqueidtopass = StrUtil.nonNull(cc.getUniqueid());
			uniqueidforPushMsg = uniqueidtopass;
			cardidforPushMsg = idStr;
			// header values
			/*
			 * String city = StrUtil.nonNull(jsonHeader.getCity());
			 * cc.setCity(city);
			 * 
			 * String country = StrUtil.nonNull(jsonHeader.getCountry());
			 * cc.setCountry(country);
			 */

			String latitude = StrUtil.nonNull(jsonHeader.getLatitude());
			String longitude = StrUtil.nonNull(jsonHeader.getLongitude());
			cc.setLatitude(latitude);
			cc.setLongitude(longitude);

			Map locationMap = new HashMap();
			try {
				// locationMap =
				// GeoCodingReverseLookupUtils.reverseLookup(latitude,longitude);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				System.out.println(e1.getMessage());
				e1.printStackTrace();
			}
			String cardcity = "";
			String cardcountry = "";
			if (locationMap != null && locationMap.size() > 1) {
				cardcity = (String) locationMap.get(CITY);
				cardcountry = (String) locationMap.get(COUNTRY);
			}
			cc.setCardcity(cardcity);
			cc.setCardcountry(cardcountry);

			String socialnetwork = StrUtil.nonNull(jsonHeader
					.getSocialnetwork());
			cc.setSocialnetwork(socialnetwork);

			String source = StrUtil.nonNull(jsonHeader.getSource());
			cc.setSource(source);

			String lastupdate = StrUtil.nonNull(jsonHeader.getLastupdate());
			cc.setLastupdate(lastupdate);

			/*
			 * String beforeid = StrUtil.nonNull(jsonHeader.getBeforeid());
			 * cc.setBeforeid(beforeid);
			 */

			String afterid = StrUtil.nonNull(jsonHeader.getAfterid());
			cc.setAfterid(afterid);

			// Body values
			String conversationtopic = StrUtil.nonNull(jsonConversation
					.getConversationtopic());
			String link = StrUtil.nonNull(jsonConversation.getLink());
			String mediaid = StrUtil.nonNull(jsonConversation.getMediaid());
			String interesttag = StrUtil.nonNull(jsonConversation
					.getInteresttag());
			System.out.println("Got interesttag:" + interesttag);
			interesttag = interesttag.trim();
			if ("".equalsIgnoreCase(interesttag)) {
				interesttag = "Others";
			}
			
			interestid = interestid.trim();
			Long interestidLong = new Long(0);
			if (!"".equals(interestid)) {
				interestidLong = new Long(interestid);
			}
			String moreinteresttag = StrUtil.nonNull(jsonConversation
					.getMoreinteresttag());
			rating = StrUtil.nonNull(jsonConversation.getRating());
			String additionalviews = StrUtil.nonNull(jsonConversation
					.getAdditionalviews());
			String[] socialshareon = jsonConversation.getSocialshareon();
			StringBuffer sbfr = new StringBuffer();
			if (socialshareon != null && socialshareon.length > 0) {
				for (int i = 0; i < socialshareon.length; i++) {
					String temp = socialshareon[i];
					sbfr.append(temp + ",");
				}
			} else {
				sbfr.append("");
			}
			String socialshareonStr = sbfr.toString();

			cc.setTopic(conversationtopic);
			cc.setLink(link);
			cc.setMediaid(mediaid);
			cc.setInteresttag(interesttag);
			cc.setMoreinteresttag(moreinteresttag);
			cc.setRating(rating);
			cc.setAdditionalviews(additionalviews);
			cc.setSocialshareon(socialshareonStr);
			cc.setIssystemcard("false");

			// CategoryMaster CM =
			// (CategoryMaster)categoryMasterDAO.getObjectByCategory(interesttag);
			// Long catid = new Long(interesttag);
			cc.setInterestid(interestidLong);

			// Set update date
			cc.setUpdatedate(new Date());

			// What action and actiontype
			if(freshsearch){
				cc.setAction(ACTIONYES);
				cc.setActiontype(ACTIONTYPEEDIT);
				
			}
			
			/*
			 * if(interestidLong.equals(0)){ cc.setAction(ACTIONINTEREST);
			 * }else{ cc.setAction(ACTIONYES); }
			 */
			
			
			
			
			// Hitherto missing fields
			cc.setUsername(u.getName());
			cc.setAvatar(u.getAvatar());
			cc.setIsowner(true);
			String shareurl = CARDSHAREURL + cc.getId().toString();
			cc.setShareurl(shareurl);
			cc.setIsbookmarked(false);
			
//			 TODO Also to set distribution list size.
			List userList = userDAO.getAllRecords();
			int distributionsize = (userList.size() - 1);
			cc.setDistributionsize(new Long(distributionsize));
			
			//Channels etc
			List voicesList = voicesDAO.getAllRecordsbyCardId(cc.getId());

			List voicesDetailsList = voicesDetailsDAO
					.getAllRecordsbyCardid(cc.getId());

			/*Map channelMap = new HashMap();

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
			cc.setChannel(channelList);*/
			
			//
			cc.setUsername(u.getName());
			cc.setAvatar(u.getAvatar());
			cc.setUserId(new Long(cc.getUserid()));
			BookMark bm = (BookMark) bookMarkDAO.getObjectByUserIdAndCardId(
					new Long(cc.getUserid()), cc.getId());
			if (bm != null) {
				cc.setIsbookmarked(true);
			}
			cc.setIsduplicate(new Long(0));

			List userinterestlist = userCategoryMapDAO
					.getAllCategoriesforUser(new Long(cc.getUserid()));
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
			cc.setUserinterest(interestidlist);
			
			
			/*List cvmList = cardVoteModelDAO.getAllRecordsByCardidUserid(
					cc.getId(), new Long(cc.getUserid()));*/
			cc.setIsupvoted(false);
			cc.setIsdownvoted(false);
			
			List commentsList = cardCommentsDAO.getAllRecordsbyCardid(cc.getId());
			if(commentsList!=null){
				cc.setCommentscount(new Long(commentsList.size()));
			}else{
				cc.setCommentscount(new Long(0));
			}
			
			List upvotelist = cardVoteModelDAO.getAllUpRecordsByCardid(cc.getId());
			List downvotelist = cardVoteModelDAO.getAllDownRecordsByCardid(cc.getId());
			cc.setUpvotecount(new Long(upvotelist.size()));
			cc.setDownvotecount(new Long(downvotelist.size()));
			//Upto this for hithero missing values

			// Update for Edit
			try {
				conversationCardDAO.addOrUpdateRecord(cc);
				UserCategoryMap ucm = (UserCategoryMap) userCategoryMapDAO
						.getObjectByCatIdSubCatIdUserId(interestidLong,
								new Long(cc.getUserid()), cc.getSubinterestid());
				if (ucm == null) {
					int intcounter = interestidLong.intValue();
					if (intcounter != 0) {
						UserCategoryMap ucmnew = new UserCategoryMap();
						ucmnew.setCategoryid(interestidLong);
						ucmnew.setUserid(new Long(cc.getUserid()));
						ucmnew.setReputationscore(new Long(0));
						CategoryMaster cm = (CategoryMaster) categoryMasterDAO
								.getObjectById(interestidLong);
						if (cm != null) {
							ucmnew.setCategory(cm.getCategoryname());
						} else {
							ucmnew.setCategory("");
						}

						userCategoryMapDAO.addOrUpdateRecord(ucmnew);
					}

				}

				// Create an entry for comment if additional views is not null
				boolean postcomment = false;
				if(!"".equalsIgnoreCase(additionalviews)||!"".equalsIgnoreCase(rating)||!"".equalsIgnoreCase(link)){
					postcomment = true;
				}
				if (postcomment) {
					CardComments cCom = new CardComments();
					cCom.setCardid(cc.getId());
					cCom.setCarduniqueid(uniqueidtopass);
					cCom.setComment(additionalviews);
					cCom.setCommentdate(new Date());
					cCom.setCommenter(u.getName());
					cCom.setSbcommenterid(u.getId());
					cCom.setAvatar(u.getAvatar());
					cCom.setMediaid("");
					cCom.setRating(rating);
					cCom.setLink(link);
					cCom.setExtcommentersource("");
					cCom.setExtcommenterid("");
					cardCommentsDAO.addOrUpdateRecord(cCom);
				}
			} catch (Exception e) {
				e.printStackTrace();
				noerror = false;
				String errMsg = e.getMessage();
				response.setStatus(500);
				writer.write(STANDARERRORRESPONSE + errMsg);
				writer.close();
				return null;
			}
		}

		cc.setIsowner(true);
		cc.setAvatar(StrUtil.nonNull(u.getAvatar()));
		cc.setUsername(StrUtil.nonNull(u.getName()));
		cc.setRating(rating);
		cc.setSystemcard(false);
		cc.setCreationdate(new Date());

		String responseString = "";
		if (noerror) {
			// responseString = SUCCESSESPONSEWTHID + uniqueidtopass;
			if (isedit) {
				ConversationCard ccedit = (ConversationCard) conversationCardDAO
						.getObjectByUniqueId(uniqueidtopass);
				ccedit.setIsowner(true);
				ccedit.setAvatar(StrUtil.nonNull(u.getAvatar()));
				ccedit.setUsername(StrUtil.nonNull(u.getName()));
				ccedit.setRating(rating);
				ccedit.setSystemcard(false);
				ccedit.setCreationdate(new Date());
				ccedit.setShareurl(CARDSHAREURL + ccedit.getId().toString());
				List sharelist = cardShareDAO.getAllSharesforCardId(ccedit.getId());
				Long sharecount = new Long(0);
				if(sharelist!=null){
					sharecount = new Long(sharelist.size());
				}
				ccedit.setSharecount(sharecount);
			//	List voicesList = voicesDAO.getAllRecordsbyCardId(ccedit.getId());
				
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
				
				ccedit.setChannel(channelList);
				
				List userList = userDAO.getAllRecords();
				int distributionsize = (userList.size() - 1);
				ccedit.setDistributionsize(new Long(distributionsize));
				
				List upvotelist = cardVoteModelDAO.getAllUpRecordsByCardid(cc.getId());
				List downvotelist = cardVoteModelDAO.getAllDownRecordsByCardid(cc.getId());
				
				ccedit.setIsupvoted(false);
				ccedit.setIsdownvoted(false);
				
				for (int h=0;h<upvotelist.size();h++){
					CardVoteModel cvm = (CardVoteModel) upvotelist.get(h);
					Long useridcvmvote = cvm.getUserid();
					int useridcvmvoteint = useridcvmvote.intValue();
					int useridint = u.getId().intValue();
					if (useridint == useridcvmvoteint) {
						ccedit.setIsupvoted(true);
						ccedit.setIsdownvoted(false);
						break;
					}
				}
				
				for (int h=0;h<downvotelist.size();h++){
					CardVoteModel cvm = (CardVoteModel) downvotelist.get(h);
					Long useridcvmvote = cvm.getUserid();
					int useridcvmvoteint = useridcvmvote.intValue();
					int useridint = u.getId().intValue();
					if (useridint == useridcvmvoteint) {
						ccedit.setIsupvoted(false);
						ccedit.setIsdownvoted(true);
						break;
					}
				}
				
				ccedit.setUpvotecount(new Long(upvotelist.size()));
				ccedit.setDownvotecount(new Long(downvotelist.size()));
				List commentslist = cardCommentsDAO.getAllRecordsbyCardid(ccedit.getId());
				Long commentscount = new Long(0);
				if(commentslist!=null){
					commentscount = new Long(commentslist.size());
				}
				ccedit.setCommentscount(commentscount);
				
				responseString = gson.toJson(ccedit);
			//	System.out.println("RESPONSE FOR EDIT IS " + responseString);
			} else {
				List commentslist = cardCommentsDAO.getAllRecordsbyCardid(cc.getId());
				Long commentscount = new Long(0);
				if(commentslist!=null){
					commentscount = new Long(commentslist.size());
				}
				cc.setCommentscount(commentscount);
				List<NameValuePair> channelList = new ArrayList<NameValuePair>();
				cc.setChannel(channelList);
				responseString = gson.toJson(cc);
			}

			response.setStatus(200);
		} else {
			responseString = STANDARERRORRESPONSE;
			response.setStatus(500);
		}
		System.out.println("responseString: " + responseString);
		response.addHeader("Access-Control-Allow-Origin", "*");
		writer.write(responseString);
		writer.close();


		return null;
	}

	private List decideCategory(String conversationtopic,
			TaxonomyDAO taxonomyDAO) {
		List catList = TraverseTaxonomy.getCategory(conversationtopic,
				taxonomyDAO);
		return catList;
	}

	private void sendTemporaryPushMessage(String userid, String carduniqueid,
			String cardid) {
		System.out.println("1. " + userid + ", 2. " + userDAO + ", 3. "
				+ carduniqueid + ", 4. " + cardid);
		User user = (User) userDAO.getObjectById(new Long(userid));
		String socialnetwork = user.getSocialnetwork();
		GCM gcm = (GCM) gcmDAO.getObjectByUserIdSocialnetwork(new Long(userid),
				socialnetwork);
		String deviceregid = gcm.getGoogleid();
		JsonGetCard jgc = new JsonGetCard();
		jgc.setCardid(cardid);
		jgc.setUniqueid(carduniqueid);
		jgc.setType(SEARCHTYPE);
		Gson gson = new Gson();
		String userMessage = gson.toJson(jgc);
		System.out.println(userMessage);
		GooglePushMessage.sendPushMessage(deviceregid, userMessage);
	}

	public CategoryMasterDAO getCategoryMasterDAO() {
		return categoryMasterDAO;
	}

	public void setCategoryMasterDAO(CategoryMasterDAO categoryMasterDAO) {
		this.categoryMasterDAO = categoryMasterDAO;
	}

	public UserCategoryMapDAO getUserCategoryMapDAO() {
		return userCategoryMapDAO;
	}

	public void setUserCategoryMapDAO(UserCategoryMapDAO userCategoryMapDAO) {
		this.userCategoryMapDAO = userCategoryMapDAO;
	}

	public UserDuplicateCardMapDAO getUserDuplicateCardMapDAO() {
		return userDuplicateCardMapDAO;
	}

	public void setUserDuplicateCardMapDAO(
			UserDuplicateCardMapDAO userDuplicateCardMapDAO) {
		this.userDuplicateCardMapDAO = userDuplicateCardMapDAO;
	}

	public CardVoteModelDAO getCardVoteModelDAO() {
		return cardVoteModelDAO;
	}

	public void setCardVoteModelDAO(CardVoteModelDAO cardVoteModelDAO) {
		this.cardVoteModelDAO = cardVoteModelDAO;
	}

	public CardShareDAO getCardShareDAO() {
		return cardShareDAO;
	}

	public void setCardShareDAO(CardShareDAO cardShareDAO) {
		this.cardShareDAO = cardShareDAO;
	}
}
