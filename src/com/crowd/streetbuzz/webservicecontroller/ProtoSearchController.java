/**
 * 
 */
package com.crowd.streetbuzz.webservicecontroller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.CardMasterDAO;
import com.crowd.streetbuzz.dao.implementation.SearchCardDAO;
import com.crowd.streetbuzz.model.CardMaster;
import com.crowd.streetbuzz.model.SearchCard;
import com.crowd.streetbuzzalgo.utils.StrUtil;

/**
 * @author Atrijit
 *
 */
public class ProtoSearchController implements Controller, Constants{
	private String returnView = "";
	private CardMasterDAO cardMasterDAO;

	private SearchCardDAO searchCardDAO;
	
	public CardMasterDAO getCardMasterDAO() {
		return cardMasterDAO;
	}

	public void setCardMasterDAO(CardMasterDAO cardMasterDAO) {
		this.cardMasterDAO = cardMasterDAO;
	}

	public SearchCardDAO getSearchCardDAO() {
		return searchCardDAO;
	}

	public void setSearchCardDAO(SearchCardDAO searchCardDAO) {
		this.searchCardDAO = searchCardDAO;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(true);
		String searchtopic = StrUtil.nonNull(request.getParameter("entry"));
		String interesttag = StrUtil.nonNull(request.getParameter("category"));
		
//		 Create the unique id
		CardMaster cardMaster = new CardMaster();
		String uniqueidtopass = StrUtil.getUniqueId();
		cardMaster.setType(SEARCHTYPE);
		cardMaster.setUniqueid(uniqueidtopass);
		cardMasterDAO.addOrUpdateRecord(cardMaster);
		
		SearchCard sc = new SearchCard();
		sc.setUniqueid(uniqueidtopass);
		sc.setLatitude("");
		sc.setLongitude("");
		sc.setUserid("proto@street.buzz");
		sc.setSocialnetwork("FACEBOOK");
		sc.setCardtype(SEARCHTYPE);
		sc.setTopic(searchtopic);
		sc.setInteresttag(interesttag);
		sc.setCreationdate(new Date());
		sc.setAction(ACTIONYES);
		sc.setActiontype(ACTIONTYPENEEW);
		searchCardDAO.addOrUpdateRecord(sc);
		
		returnView = "pages/protoentry.jsp?p=";
		return new ModelAndView(returnView);
	}


}
