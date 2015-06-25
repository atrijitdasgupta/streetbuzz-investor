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
import com.crowd.streetbuzz.dao.implementation.ConversationCardDAO;
import com.crowd.streetbuzz.dao.implementation.DistributionDAO;
import com.crowd.streetbuzz.dao.implementation.PrivateMessageDAO;
import com.crowd.streetbuzz.dao.implementation.UserCategoryMapDAO;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzz.json.StatisticsJson;
import com.crowd.streetbuzz.model.Distribution;
import com.crowd.streetbuzz.model.UserCategoryMap;
import com.google.gson.Gson;

/**
 * @author Atrijit Need count of cards per interest, total messages, total
 *         bookmarks, total friends Refer StatisticsJson class
 * 
 * JSON:
 * {"messages":234,"friends":12,"bookmarks":23,"interestcountmap":{"1":22,"12":33}}
 */
public class GetStatisticsController implements Controller, Constants {
	private PrivateMessageDAO privateMessageDAO;

	private DistributionDAO distributionDAO;
	
	private BookMarkDAO bookMarkDAO;
	
	private UserCategoryMapDAO userCategoryMapDAO;
	
	private ConversationCardDAO conversationCardDAO;

	public ConversationCardDAO getConversationCardDAO() {
		return conversationCardDAO;
	}

	public void setConversationCardDAO(ConversationCardDAO conversationCardDAO) {
		this.conversationCardDAO = conversationCardDAO;
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

	public PrivateMessageDAO getPrivateMessageDAO() {
		return privateMessageDAO;
	}

	public void setPrivateMessageDAO(PrivateMessageDAO privateMessageDAO) {
		this.privateMessageDAO = privateMessageDAO;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(true);
		JsonHeader jh = (JsonHeader) session.getAttribute("jsonHeader");
		String userid = jh.getUserid();
		Long useridLong = new Long(userid);
		List messagesList = privateMessageDAO
				.getAllRecordsforUserId(useridLong);
		// List newdistList =
		// distributionDAO.getAllNewRecordsForUser(useridLong);
		List distList = distributionDAO.getAllRecordsForUser(useridLong);
		Gson gson = new Gson();
		//fix for initial cases
		if (distList == null) {
			Map defaultInitMap = defaultInitMap();
			StatisticsJson SJ = new StatisticsJson();
			SJ.setMessages(new Long(0));

			// TODO Set bookmarks count
			List bmlist = bookMarkDAO.getAllRecordsForUser(useridLong);
			int count = 0;
			if(bmlist!=null){
				count = bmlist.size();
			}
			SJ.setBookmarks(new Long(count));
			SJ.setFriends(new Long(0));
			Set keyset = defaultInitMap.keySet();
			Iterator it = keyset.iterator();
			List<NameValuePair> interestList = new ArrayList<NameValuePair>();
			while (it.hasNext()) {
				Long interestid = (Long) it.next();
				Long countinterest = (Long) defaultInitMap.get(interestid);
				if (count > 0) {
					interestList.add(new BasicNameValuePair(interestid
							.toString(), countinterest.toString()));
				}

			}
			SJ.setInterestcountList(interestList);
			String responseStr = gson.toJson(SJ);

			PrintWriter writer = response.getWriter();
			response.setStatus(200);
			response.addHeader("Access-Control-Allow-Origin", "*");
			writer.write(responseStr);
			writer.close();
			return null;
		}

		// Set messages count
		int msgscount = 0;
		if (messagesList != null) {
			msgscount = messagesList.size();
		}
		StatisticsJson SJ = new StatisticsJson();
		SJ.setMessages(new Long(msgscount));

		// TODO Set bookmarks count
		List bmlist = bookMarkDAO.getAllRecordsForUser(useridLong);
		int count = 0;
		if(bmlist!=null){
			count = bmlist.size();
		}
		SJ.setBookmarks(new Long(count));

		// Set friends count
		List friends = new ArrayList();
		for (int i = 0; i < distList.size(); i++) {
			Distribution dist = (Distribution) distList.get(i);
			Long touserid = dist.getDestinationuserid();
			if (!touserid.equals(useridLong)) {
				if (!friends.contains(touserid)) {
					friends.add(touserid);
				}
			}
		}
		int friendsize = friends.size();
		SJ.setFriends(new Long(friendsize));

		// Set interest specific count
		Map map = new HashMap();
		
		
		for (int i=0;i<16;i++){
			Long interestid = new Long(i+1);
			List cardlist = conversationCardDAO.getAllReadyRecordsInterest(interestid);
			int size = 0;
			if(cardlist!=null){
				size = cardlist.size();
			}
			
		//	System.out.println("cardlist.size(): "+size);
			Long sizelong = new Long(size);
			map.put(interestid, sizelong);
		}
		
		
		// SJ.setInterestcountmap(map);
		Set keyset = map.keySet();
		Iterator it = keyset.iterator();
		List<NameValuePair> interestList = new ArrayList<NameValuePair>();
		while (it.hasNext()) {
			Long interestid = (Long) it.next();
			Long countnow = (Long) map.get(interestid);
			//System.out.println(interestid+" #### "+countnow);
			
				interestList.add(new BasicNameValuePair(interestid.toString(),
						countnow.toString()));
			

		}
		SJ.setInterestcountList(interestList);

		String responseStr = gson.toJson(SJ);
		//System.out.println("Statistics: "+responseStr);

		PrintWriter writer = response.getWriter();
		response.setStatus(200);
		response.addHeader("Access-Control-Allow-Origin", "*");
		writer.write(responseStr);
		writer.close();
		return null;
	}

	private static Map defaultInitMap() {
		Map map = new HashMap();
		map.put(new Long(1), new Long(0));
		map.put(new Long(2), new Long(0));
		map.put(new Long(3), new Long(0));
		map.put(new Long(4), new Long(0));

		map.put(new Long(5), new Long(0));
		map.put(new Long(6), new Long(0));
		map.put(new Long(7), new Long(0));
		map.put(new Long(8), new Long(0));

		map.put(new Long(9), new Long(0));
		map.put(new Long(10), new Long(0));
		map.put(new Long(11), new Long(0));
		map.put(new Long(12), new Long(0));

		map.put(new Long(13), new Long(0));
		map.put(new Long(14), new Long(0));
		map.put(new Long(15), new Long(0));
		map.put(new Long(16), new Long(0));
		return map;
	}

	public BookMarkDAO getBookMarkDAO() {
		return bookMarkDAO;
	}

	public void setBookMarkDAO(BookMarkDAO bookMarkDAO) {
		this.bookMarkDAO = bookMarkDAO;
	}
}
