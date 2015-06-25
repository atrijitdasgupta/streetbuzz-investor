/**
 * 
 */
package com.crowd.streetbuzz.asynccontroller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.CardInterestDetectedMapDAO;
import com.crowd.streetbuzz.dao.implementation.CategoryMasterDAO;
import com.crowd.streetbuzz.dao.implementation.ConversationCardDAO;
import com.crowd.streetbuzz.dao.implementation.UserCategoryMapDAO;
import com.crowd.streetbuzz.dao.implementation.UserDAO;
import com.crowd.streetbuzz.json.JsonDisambiguationPush;
import com.crowd.streetbuzz.model.CardInterestDetectedMap;
import com.crowd.streetbuzz.model.CategoryMaster;
import com.crowd.streetbuzz.model.ConversationCard;
import com.crowd.streetbuzz.model.User;
import com.crowd.streetbuzz.model.UserCategoryMap;
import com.crowd.streetbuzz.utils.ApplePushMessage;
import com.crowd.streetbuzz.utils.GooglePushMessage;
import com.crowd.streetbuzzalgo.interestdetection.InterestDetector;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;

/**
 * @author Atrijit
 * 
 */
public class InterestController implements Controller, Constants {
	private ConversationCardDAO conversationCardDAO;

	private UserDAO userDAO;

	private CardInterestDetectedMapDAO cardInterestDetectedMapDAO;

	private UserCategoryMapDAO userCategoryMapDAO;

	private CategoryMasterDAO categoryMasterDAO;

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

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// System.out.println("inside InterestController");
		run();

		return null;
	}

	private void run() {
		// System.out.println("inside InterestController run method");
		// first get the list of new Conversation cards to be interest detected
		List ccList = conversationCardDAO.getAllPendingNewInterestRecords();
		List idlist = new ArrayList();
		for (int i = 0; i < ccList.size(); i++) {
			ConversationCard cc = (ConversationCard) ccList.get(i);
			Long id = cc.getId();
			idlist.add(id);
			cc.setAction(ACTIONINTERESTPROCESSING);
			conversationCardDAO.addOrUpdateRecord(cc);

		}
		for (int i = 0; i < idlist.size(); i++) {
			// System.out.println("Inside the processing loop " + i);
			Long id = (Long) idlist.get(i);
			ConversationCard cc = (ConversationCard) conversationCardDAO
					.getObjectById(id);
			String topic = StrUtil.nonNull(cc.getTopic());
			boolean updateusercatmap = false;
			try {
				List interestlist = new ArrayList();
				try {
					interestlist = InterestDetector.detectInterest(topic);
					System.out
							.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
					System.out.println("interestlist from InterestController: "
							+ interestlist);
					System.out
							.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				if (interestlist != null) {
					if (interestlist.size() == 1) {
						updateusercatmap = true;
						Long interestid = (Long) interestlist.get(0);
						// Change to set to parentid
						CategoryMaster cm = (CategoryMaster) categoryMasterDAO
								.getObjectById(interestid);
						Long parentid = cm.getParentid();
						int parentidint = parentid.intValue();
						if (parentidint == 0) {
							cc.setInterestid(interestid);
							cc.setSubinterestid(interestid);
						} else {
							cc.setInterestid(parentid);
							cc.setSubinterestid(interestid);
						}

						// cc.setAction(ACTIONYES);
						// conversationCardDAO.addOrUpdateRecord(cc);
					}
				}
				boolean sendnotification = false;
				if (interestlist == null) {
					sendnotification = true;
				}
				if (interestlist != null) {
					if (interestlist.size() < 1) {
						sendnotification = true;
					}
					if (interestlist.size() > 1) {
						sendnotification = true;
					}
				}
				String pushmessage = "";
				if (sendnotification) {
					try {
						sendPushMessage(cc, interestlist);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println(e.getMessage());
					}
				}
				// Commit the interest to DB for future reference
				if (interestlist != null) {
					if (interestlist.size() > 1) {
						for (int j = 0; j < interestlist.size(); j++) {
							Long interestid = (Long) interestlist.get(j);
							Long cardid = cc.getId();
							CardInterestDetectedMap cidm = new CardInterestDetectedMap();
							cidm.setCardid(cardid);
							cidm.setInterestid(interestid);
							cidm.setProcessed(new Long(0));
							cardInterestDetectedMapDAO.addOrUpdateRecord(cidm);
						}
					}
				}

				boolean insertzero = false;
				if (interestlist == null) {
					insertzero = true;
				} else if (interestlist.size() == 0) {
					insertzero = true;
				}
				if (insertzero) {
					CardInterestDetectedMap cidm = new CardInterestDetectedMap();
					cidm.setCardid(cc.getId());
					cidm.setInterestid(new Long(0));
					cidm.setProcessed(new Long(1));
					cardInterestDetectedMapDAO.addOrUpdateRecord(cidm);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			cc.setAction(ACTIONYES);
			conversationCardDAO.addOrUpdateRecord(cc);

			// Update the unique interest in user category map table if set
			int interestidint = 0;
			int subinterestidint = 0;
			Long interestid = cc.getInterestid();
			if (interestid != null) {
				interestidint = interestid.intValue();
			}
			if(interestid==null){
				interestid = new Long(0);
			}
			Long subinterestid = cc.getSubinterestid();
			if (subinterestid != null) {
				subinterestidint = subinterestid.intValue();
			}
			if(subinterestid==null){
				subinterestid = new Long(0);
			}
			if(updateusercatmap){
				if (interestidint != 0 && subinterestidint !=0) {
					Long uidlong = new Long(cc.getUserid());
					UserCategoryMap ucm = (UserCategoryMap) userCategoryMapDAO
							.getObjectByCatIdSubCatIdUserId(interestid, uidlong,
									subinterestid);
					if (ucm == null) {
						UserCategoryMap ucmnew = new UserCategoryMap();
						CategoryMaster cm = (CategoryMaster) categoryMasterDAO
								.getObjectById(interestid);
						Long parentid = cm.getParentid();
						int parentidint = parentid.intValue();
						if (parentidint == 0) {
							ucmnew.setCategoryid(interestid);
							ucmnew.setSubcategoryid(interestid);
						} else {
							ucmnew.setCategoryid(parentid);
							ucmnew.setSubcategoryid(interestid);
						}

						ucmnew.setUserid(uidlong);
						ucmnew.setReputationscore(new Long(0));
						CategoryMaster cmnew = (CategoryMaster) categoryMasterDAO
								.getObjectById(interestid);
						String category = cmnew.getCategoryname();
						ucmnew.setCategory(category);
						userCategoryMapDAO.addOrUpdateRecord(ucmnew);
					}
				}
			}
		}
	}

	private void sendPushMessage(ConversationCard cc, List interestlist) {
		JsonDisambiguationPush jdp = new JsonDisambiguationPush();
		jdp.setType(disambiguationtype);
		Long cardid = cc.getId();
		jdp.setId(cardid);
		String topic = StrUtil.nonNull(cc.getTopic());
		jdp.setTopic(topic);
		
		List interestids = new ArrayList();
		if (interestlist != null) {
			for (int i = 0; i < interestlist.size(); i++) {
				Long interestid = (Long) interestlist.get(i);
				interestids.add(interestid);
			}
		}
		jdp.setInterestids(interestids);
		Long userid = new Long(0);
		try {
			userid = new Long(cc.getUserid());
			// System.out.println("got useridxxxxx " + userid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int useridint = userid.intValue();
		String googleid = "";
		String appleid = "";
		String userMsg = "";
		if (useridint != 0) {
			User user = (User) userDAO.getObjectById(userid);
			googleid = StrUtil.nonNull(user.getGoogledeviceid());
			appleid = StrUtil.nonNull(user.getAppledeviceid());
			Gson gson = new Gson();
			userMsg = gson.toJson(jdp);
		}

		if (!"".equalsIgnoreCase(googleid)) {
			GooglePushMessage.sendPushMessage(googleid, userMsg);
		}
		if (!"".equalsIgnoreCase(appleid)) {
		//	ApplePushMessage.sendPushMessage(appleid, userMsg);
		}

	}

	public ConversationCardDAO getConversationCardDAO() {
		return conversationCardDAO;
	}

	public void setConversationCardDAO(ConversationCardDAO conversationCardDAO) {
		this.conversationCardDAO = conversationCardDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public CardInterestDetectedMapDAO getCardInterestDetectedMapDAO() {
		return cardInterestDetectedMapDAO;
	}

	public void setCardInterestDetectedMapDAO(
			CardInterestDetectedMapDAO cardInterestDetectedMapDAO) {
		this.cardInterestDetectedMapDAO = cardInterestDetectedMapDAO;
	}
}
