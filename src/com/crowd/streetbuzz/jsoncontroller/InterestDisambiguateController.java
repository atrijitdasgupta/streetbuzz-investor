/**
 * 
 */
package com.crowd.streetbuzz.jsoncontroller;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.CardInterestDetectedMapDAO;
import com.crowd.streetbuzz.dao.implementation.CategoryMasterDAO;
import com.crowd.streetbuzz.dao.implementation.ConversationCardDAO;
import com.crowd.streetbuzz.dao.implementation.UserCategoryMapDAO;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzz.json.JsonInterestDisambiguate;
import com.crowd.streetbuzz.model.CardInterestDetectedMap;
import com.crowd.streetbuzz.model.CategoryMaster;
import com.crowd.streetbuzz.model.ConversationCard;
import com.crowd.streetbuzz.model.UserCategoryMap;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;

/**
 * @author Atrijit
 * 
 */
public class InterestDisambiguateController implements Controller, Constants {
	private ConversationCardDAO conversationCardDAO;

	private CardInterestDetectedMapDAO cardInterestDetectedMapDAO;

	private UserCategoryMapDAO userCategoryMapDAO;

	private CategoryMasterDAO categoryMasterDAO;

	public CardInterestDetectedMapDAO getCardInterestDetectedMapDAO() {
		return cardInterestDetectedMapDAO;
	}

	public void setCardInterestDetectedMapDAO(
			CardInterestDetectedMapDAO cardInterestDetectedMapDAO) {
		this.cardInterestDetectedMapDAO = cardInterestDetectedMapDAO;
	}

	public ConversationCardDAO getConversationCardDAO() {
		return conversationCardDAO;
	}

	public void setConversationCardDAO(ConversationCardDAO conversationCardDAO) {
		this.conversationCardDAO = conversationCardDAO;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(true);
		JsonHeader jsonHeader = (JsonHeader) session.getAttribute("jsonHeader");
		String jsonbody = (String) session.getAttribute("jsonbody");

		String useridstr = StrUtil.nonNull(jsonHeader.getUserid());
		// String cardidstr = StrUtil.nonNull(jsonHeader.getCardid());
		Long userid = new Long(0);

		if (!"".equalsIgnoreCase(useridstr)) {
			userid = new Long(useridstr);
		}

		Gson gson = new Gson();
		JsonInterestDisambiguate jid = gson.fromJson(jsonbody,
				JsonInterestDisambiguate.class);
		String interestidstr = StrUtil.nonNull(jid.getInterestid());
		// String cardidstr = StrUtil.nonNull(jid.getId().toString());
		Integer cardidInt = jid.getId();
		int cint = cardidInt.intValue();
		Long cardid = new Long(0);
		if (cint != 0) {
			try {
				cardid = new Long(cint);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
		}
		Long interestid = new Long(0);
		if (!"".equalsIgnoreCase(interestidstr)) {
			interestid = new Long(interestidstr);
		}
		ConversationCard cc = (ConversationCard) conversationCardDAO
				.getObjectById(cardid);
		int interestidint = interestid.intValue();
		int ccidint = cc.getInterestid().intValue();
		System.out.println("***************DEBUG RUN FOR INTEREST DISAMBIGUATION********************");
		System.out.println("Was passed interestid: "+interestidint+"(int),"+interestid.toString()+"(Long)");
		System.out.println("Card's interestid was "+ccidint+"(int),"+cc.getInterestid().toString()+"(Long)");
		
		
		if (interestidint != 0 && ccidint == 0) {
			System.out.println("Poosatike Condition matched!!!");
			CategoryMaster cm = (CategoryMaster)categoryMasterDAO.getObjectById(interestid);
			Long parentid = cm.getParentid();
			int parentidint = parentid.intValue();
			if(parentidint == 0){
				cc.setInterestid(interestid);
				cc.setSubinterestid(interestid);
			}else{
				cc.setInterestid(parentid);
				cc.setSubinterestid(interestid);
			}
			//cc.setInterestid(interestid);
			try {
				System.out.println("Updating conversation card with interestid: "+interestid);
				conversationCardDAO.addOrUpdateRecord(cc);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List list = cardInterestDetectedMapDAO
					.getAllRecordsByCardIdUnProcessed(cardid);
			for (int i = 0; i < list.size(); i++) {
				CardInterestDetectedMap ce = (CardInterestDetectedMap) list
						.get(i);
				ce.setProcessed(new Long(1));
				cardInterestDetectedMapDAO.addOrUpdateRecord(ce);
			}
			// Update the unique interest in user category map table if set
			//CategoryMaster cmnew = (CategoryMaster)categoryMasterDAO.getObjectById(interestid);
			UserCategoryMap ucm = (UserCategoryMap) userCategoryMapDAO
					.getObjectByCatIdSubCatIdUserId(interestid, userid, parentid);
			if (ucm == null) {
				UserCategoryMap ucmnew = new UserCategoryMap();
				
				if(parentidint==0){
					ucmnew.setCategoryid(interestid);
					ucmnew.setSubcategoryid(interestid);
				}else{
					ucmnew.setCategoryid(parentid);
					ucmnew.setSubcategoryid(interestid);
				}
				//ucmnew.setCategoryid(interestid);
				ucmnew.setUserid(userid);
				ucmnew.setReputationscore(new Long(0));
				CategoryMaster cmnew = (CategoryMaster) categoryMasterDAO
						.getObjectById(interestid);
				String category = cmnew.getCategoryname();
				ucmnew.setCategory(category);
				userCategoryMapDAO.addOrUpdateRecord(ucmnew);
			}

		}else{
			System.out.println("Poosatike Condition not matched");
		}

		response.setStatus(200);
		response.addHeader("Access-Control-Allow-Origin", "*");
		PrintWriter writer = response.getWriter();
		writer.write("");
		writer.close();
		return null;
	}

	public UserCategoryMapDAO getUserCategoryMapDAO() {
		return userCategoryMapDAO;
	}

	public void setUserCategoryMapDAO(UserCategoryMapDAO userCategoryMapDAO) {
		this.userCategoryMapDAO = userCategoryMapDAO;
	}

	public CategoryMasterDAO getCategoryMasterDAO() {
		return categoryMasterDAO;
	}

	public void setCategoryMasterDAO(CategoryMasterDAO categoryMasterDAO) {
		this.categoryMasterDAO = categoryMasterDAO;
	}
}
