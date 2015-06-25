/**
 * 
 */
package com.crowd.streetbuzz.jsoncontroller;

import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.BookMarkDAO;
import com.crowd.streetbuzz.dao.implementation.ConversationCardDAO;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzz.model.BookMark;
import com.crowd.streetbuzz.model.ConversationCard;
import com.crowd.streetbuzzalgo.utils.StrUtil;

/**
 * @author Atrijit
 * 
 */
public class BookmarkCardController implements Controller, Constants {
	private ConversationCardDAO conversationCardDAO;
	private BookMarkDAO bookMarkDAO;

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(true);
		JsonHeader jsonHeader = (JsonHeader) session.getAttribute("jsonHeader");
		String cardidStr = StrUtil.nonNull(jsonHeader.getCardid());
		Long cardid = new Long(0);
		if (!"".equalsIgnoreCase(cardidStr)) {
			cardid = new Long(cardidStr);
		}
		ConversationCard cc = (ConversationCard)conversationCardDAO.getObjectById(cardid);
		Long interestid = new Long(0);
		if(cc!=null){
			interestid = cc.getInterestid();
		}
		String useridStr = StrUtil.nonNull(jsonHeader.getUserid());
		Long userid = new Long(0);
		if(!"".equalsIgnoreCase(useridStr)){
			userid = new Long(useridStr);
		}
		//Check if it exists
		BookMark existsbm = (BookMark)bookMarkDAO.getObjectByUserIdAndCardId(userid, cardid);
		boolean exists = false;
		if(existsbm!=null){
			exists = true;
		}
		if(!exists){
			BookMark bm = new BookMark();
			bm.setBookmarkdate(new Date());
			bm.setEntityid(cardid);
			bm.setEntitytype("CARD");
			bm.setInterestid(interestid);
			bm.setUserid(userid);
			bookMarkDAO.addOrUpdateRecord(bm);
		}
				
		response.setStatus(200);
		response.addHeader("Access-Control-Allow-Origin", "*");
		PrintWriter writer = response.getWriter();
		writer.write("");
		writer.close();
		return null;
	}

	public ConversationCardDAO getConversationCardDAO() {
		return conversationCardDAO;
	}

	public void setConversationCardDAO(ConversationCardDAO conversationCardDAO) {
		this.conversationCardDAO = conversationCardDAO;
	}
	
	public BookMarkDAO getBookMarkDAO() {
		return bookMarkDAO;
	}

	public void setBookMarkDAO(BookMarkDAO bookMarkDAO) {
		this.bookMarkDAO = bookMarkDAO;
	}

}
