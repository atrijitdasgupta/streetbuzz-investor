/**
 * 
 */
package com.crowd.streetbuzz.asynccontroller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.CategoryMasterDAO;
import com.crowd.streetbuzz.dao.implementation.SearchCardDAO;
import com.crowd.streetbuzz.dao.implementation.UserCategoryMapDAO;
import com.crowd.streetbuzz.dao.implementation.UserDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDetailsDAO;
import com.crowd.streetbuzz.model.SearchCard;
import com.crowd.streetbuzz.processhelperutils.ProtoSearch;
import com.crowd.streetbuzzalgo.constants.SystemConstants;

/**
 * @author Atrijit
 *
 */
public class ProtoNewSearchController implements Controller, Constants, SystemConstants{
	private SearchCardDAO searchCardDAO;

	private UserDAO userDAO;

	private UserCategoryMapDAO userCategoryMapDAO;

	private CategoryMasterDAO categoryMasterDAO;

	private VoicesDAO voicesDAO;

	private VoicesDetailsDAO voicesDetailsDAO;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List scList = searchCardDAO.getAllPendingNewRecords();
		if(scList!=null && scList.size()>0){
			System.out.println("Processing "+scList.size()+" results.");
			for (int i = 0; i < scList.size(); i++) {
				SearchCard sc = (SearchCard) scList.get(i);
				// Update the search card as under processing
				sc.setAction(UNDERPROCESSING);
				searchCardDAO.addOrUpdateRecord(sc);
			}
			for (int i = 0; i < scList.size(); i++) {
				SearchCard sc = (SearchCard) scList.get(i);
				
				
				ProtoSearch.doSearch(sc, searchCardDAO, userDAO,
						userCategoryMapDAO, categoryMasterDAO, voicesDAO,
						voicesDetailsDAO);

			}
		}else{
			System.out.println("No results fetched");
		}
		return null;
	}

	public CategoryMasterDAO getCategoryMasterDAO() {
		return categoryMasterDAO;
	}

	public void setCategoryMasterDAO(CategoryMasterDAO categoryMasterDAO) {
		this.categoryMasterDAO = categoryMasterDAO;
	}

	public SearchCardDAO getSearchCardDAO() {
		return searchCardDAO;
	}

	public void setSearchCardDAO(SearchCardDAO searchCardDAO) {
		this.searchCardDAO = searchCardDAO;
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
