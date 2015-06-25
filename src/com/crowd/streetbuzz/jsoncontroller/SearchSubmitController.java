/**
 * 
 */
package com.crowd.streetbuzz.jsoncontroller;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.CardMasterDAO;
import com.crowd.streetbuzz.dao.implementation.GCMDAO;
import com.crowd.streetbuzz.dao.implementation.SearchCardDAO;
import com.crowd.streetbuzz.dao.implementation.UserDAO;
import com.crowd.streetbuzz.json.JsonGetCard;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzz.json.JsonSearch;
import com.crowd.streetbuzz.model.CardMaster;
import com.crowd.streetbuzz.model.GCM;
import com.crowd.streetbuzz.model.SearchCard;
import com.crowd.streetbuzz.model.User;
import com.crowd.streetbuzz.utils.GooglePushMessage;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;

/**
 * @author Atrijit
 * 
 */
public class SearchSubmitController implements Controller, Constants {
	private CardMasterDAO cardMasterDAO;

	private SearchCardDAO searchCardDAO;
	
	private UserDAO userDAO;
	
	private GCMDAO gcmDAO;

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	

	public SearchCardDAO getSearchCardDAO() {
		return searchCardDAO;
	}

	public void setSearchCardDAO(SearchCardDAO searchCardDAO) {
		this.searchCardDAO = searchCardDAO;
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
		System.out.println("Inside SearchSubmitController ready to spotate");
		HttpSession session = request.getSession(true);
		JsonHeader jsonHeader = (JsonHeader) session.getAttribute("jsonHeader");
		String jsonbody = StrUtil.nonNull((String) session
				.getAttribute("jsonbody"));
		PrintWriter writer = response.getWriter();
		if ("".equalsIgnoreCase(jsonbody)) {
			noerror = false;
			String errMsg = "Empty jsonbody";
			writer.write(STANDARERRORRESPONSE+errMsg);
			writer.close();
			return null;

		}
		JsonSearch jsonSearch = new JsonSearch();
		Gson gson = new Gson();
		try {
			jsonSearch = (JsonSearch) gson.fromJson(jsonbody, JsonSearch.class);
		} catch (Exception e) {
			noerror = false;
			String errMsg = e.getMessage();
			response.setStatus(500);
			writer.write(STANDARERRORRESPONSE+errMsg);
			writer.close();
			return null;
		}
		// Create the unique id
		CardMaster cardMaster = new CardMaster();
		
		String uniqueidforPushMsg = "";
		String cardidforPushMsg = "";
		String uniqueeidfromheader = StrUtil.nonNull(jsonHeader.getCarduniqueid());
		String uniqueidtopass = "";
		if("".equalsIgnoreCase(uniqueeidfromheader)){
			uniqueidtopass = StrUtil.getUniqueId();
			cardMaster.setType(SEARCHTYPE);
			cardMaster.setUniqueid(uniqueidtopass);
			try {
				cardMasterDAO.addOrUpdateRecord(cardMaster);
			} catch (Exception e) {
				noerror = false;
				String errMsg = e.getMessage();
				response.setStatus(500);
				writer.write(STANDARERRORRESPONSE+errMsg);
				writer.close();
				return null;
			}
		}else{
			uniqueidtopass = uniqueeidfromheader;
		}
		
		SearchCard sc = null;
		String cardid = StrUtil.nonNull(jsonHeader.getCardid());
		/*if ("".equalsIgnoreCase(cardid)||"0".equalsIgnoreCase(cardid)) {*/
			// New Card
			sc = new SearchCard();

			sc.setUniqueid(uniqueidtopass);
			uniqueidforPushMsg = uniqueidtopass;

			// header values
			/*String city = StrUtil.nonNull(jsonHeader.getCity());
			sc.setCity(city);

			String country = StrUtil.nonNull(jsonHeader.getCountry());
			sc.setCountry(country);*/

			String latitude = StrUtil.nonNull(jsonHeader.getLatitude());
			String longitude = StrUtil.nonNull(jsonHeader.getLongitude());
			sc.setLatitude(latitude);
			sc.setLongitude(longitude);
			
			Map locationMap = new HashMap();
			try {
				//locationMap = GeoCodingReverseLookupUtils.reverseLookup(latitude,longitude);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				System.out.println(e1.getMessage());
			}
			String cardcity = "";
			String cardcountry = "";
			if(locationMap!=null && locationMap.size()>1){
				cardcity = (String)locationMap.get(CITY);
				cardcountry = (String)locationMap.get(COUNTRY);
			}
			
			sc.setCardcity(cardcity);
			sc.setCardcountry(cardcountry);

			String userid = StrUtil.nonNull(jsonHeader.getUserid());
			sc.setUserid(userid);

			String socialnetwork = StrUtil.nonNull(jsonHeader
					.getSocialnetwork());
			sc.setSocialnetwork(socialnetwork);
			
			
			//sc.setCardid(cardid);

			//String cardtype = StrUtil.nonNull(jsonHeader.getCardtype());
			sc.setCardtype(SEARCHTYPE);

			String voiceid = StrUtil.nonNull(jsonHeader.getVoiceid());
			sc.setVoiceid(voiceid);

			String source = StrUtil.nonNull(jsonHeader.getSource());
			sc.setSource(source);

			String lastupdate = StrUtil.nonNull(jsonHeader.getLastupdate());
			sc.setLastupdate(lastupdate);

			/*String beforeid = StrUtil.nonNull(jsonHeader.getBeforeid());
			sc.setBeforeid(beforeid);*/

			String afterid = StrUtil.nonNull(jsonHeader.getAfterid());
			sc.setAfterid(afterid);

			// body values now
			String searchtopic = StrUtil.nonNull(jsonSearch.getSearchtopic());
			String interesttag = StrUtil.nonNull(jsonSearch.getInteresttag());
			String moreinteresttag = StrUtil.nonNull(jsonSearch
					.getMoreinteresttag());
			String postonsocial = StrUtil.nonNull(jsonSearch.getPostonsocial());
			String facebook = StrUtil.nonNull(jsonSearch.getFacebook());
			String twitter = StrUtil.nonNull(jsonSearch.getTwitter());
			String googleplus = StrUtil.nonNull(jsonSearch.getGoogleplus());
			String postanonymous = StrUtil.nonNull(jsonSearch
					.getPostanonymous());

			sc.setTopic(searchtopic);
			sc.setInteresttag(interesttag);
			sc.setMoreinteresttag(moreinteresttag);
			sc.setPostonsocial(postonsocial);
			sc.setFacebook(facebook);
			sc.setTwitter(twitter);
			sc.setGoogleplus(googleplus);
			sc.setPostanonymous(postanonymous);

			// Set Creation Date
			sc.setCreationdate(new Date());

			// What action and actiontype
			sc.setAction(ACTIONYES);
			sc.setActiontype(ACTIONTYPENEEW);
			// create new card entry, with partial values
			try {
				searchCardDAO.addOrUpdateRecord(sc);
				sc = (SearchCard)searchCardDAO.getObjectByUniqueId(uniqueidtopass);
				Long newid = sc.getId();
				cardidforPushMsg = newid.toString();
			} catch (Exception e) {
				noerror = false;
				String errMsg = e.getMessage();
				response.setStatus(500);
				PrintWriter writ = response.getWriter();
				writ.write(STANDARERRORRESPONSE+errMsg);
				writ.close();
				return null;
			}

		/*} else {// Edit Request
			isedit = true;
			//sc = (SearchCard) searchCardDAO.getObjectByUniqueId(uniqueidtopass);
			String idStr = jsonHeader.getCardid();
			Long id = new Long(idStr);
			sc = (SearchCard)searchCardDAO.getObjectById(id);
			uniqueidtopass = StrUtil.nonNull(sc.getUniqueid());
			uniqueidforPushMsg = uniqueidtopass;
			cardidforPushMsg = idStr;
			// header values
			String city = StrUtil.nonNull(jsonHeader.getCity());
			sc.setCity(city);

			String country = StrUtil.nonNull(jsonHeader.getCountry());
			sc.setCountry(country);

			String latitude = StrUtil.nonNull(jsonHeader.getLatitude());
			String longitude = StrUtil.nonNull(jsonHeader.getLongitude());
			sc.setLatitude(latitude);
			sc.setLongitude(longitude);
			
			Map locationMap = new HashMap();
			try {
				//locationMap = GeoCodingReverseLookupUtils.reverseLookup(latitude,longitude);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				System.out.println(e1.getMessage());
			}
			String cardcity = "";
			String cardcountry = "";
			if(locationMap!=null && locationMap.size()>1){
				cardcity = (String)locationMap.get(CITY);
				cardcountry = (String)locationMap.get(COUNTRY);
			}
			
			sc.setCardcity(cardcity);
			sc.setCardcountry(cardcountry);

			String socialnetwork = StrUtil.nonNull(jsonHeader
					.getSocialnetwork());
			sc.setSocialnetwork(socialnetwork);

			String source = StrUtil.nonNull(jsonHeader.getSource());
			sc.setSource(source);

			String lastupdate = StrUtil.nonNull(jsonHeader.getLastupdate());
			sc.setLastupdate(lastupdate);

			String beforeid = StrUtil.nonNull(jsonHeader.getBeforeid());
			sc.setBeforeid(beforeid);

			String afterid = StrUtil.nonNull(jsonHeader.getAfterid());
			sc.setAfterid(afterid);

			// body values now
			String searchtopic = StrUtil.nonNull(jsonSearch.getSearchtopic());
			String interesttag = StrUtil.nonNull(jsonSearch.getInteresttag());
			String moreinteresttag = StrUtil.nonNull(jsonSearch
					.getMoreinteresttag());
			String postonsocial = StrUtil.nonNull(jsonSearch.getPostonsocial());
			String facebook = StrUtil.nonNull(jsonSearch.getFacebook());
			String twitter = StrUtil.nonNull(jsonSearch.getTwitter());
			String googleplus = StrUtil.nonNull(jsonSearch.getGoogleplus());
			String postanonymous = StrUtil.nonNull(jsonSearch
					.getPostanonymous());

			sc.setTopic(searchtopic);
			sc.setInteresttag(interesttag);
			sc.setMoreinteresttag(moreinteresttag);
			sc.setPostonsocial(postonsocial);
			sc.setFacebook(facebook);
			sc.setTwitter(twitter);
			sc.setGoogleplus(googleplus);
			sc.setPostanonymous(postanonymous);

			// Set update date
			sc.setUpdatedate(new Date());

			// What action and actiontype
			sc.setAction(ACTIONYES);
			sc.setActiontype(ACTIONTYPEEDIT);
			// Update for Edit
			try {
				searchCardDAO.addOrUpdateRecord(sc);
			} catch (Exception e) {
				noerror = false;
				String errMsg = e.getMessage();
				response.setStatus(500);
				writer.write(STANDARERRORRESPONSE+errMsg);
				writer.close();
				return null;
			}
		}*/
		String responseString = "";
		if (noerror) {
			//responseString = SUCCESSESPONSEWTHID + uniqueidtopass;
			/*if(isedit){
				responseString = uniqueidtopass;
			}else{*/
				responseString = gson.toJson(sc);
			//}
				
			response.setStatus(200);
		} else {
			responseString = STANDARERRORRESPONSE;
			response.setStatus(500);
		}
		System.out.println("responseString: "+responseString);
		response.addHeader("Access-Control-Allow-Origin", "*");
		writer.write(responseString);
		writer.close();
		
		/*Thread.sleep(30000);
		String userid = jsonHeader.getUserid();
		
		sendTemporaryPushMessage(userid, uniqueidforPushMsg, cardidforPushMsg);*/

		return null;
	}
	
	private void sendTemporaryPushMessage(String userid, String carduniqueid, String cardid){
		System.out.println("1. "+userid+", 2. "+userDAO+", 3. "+carduniqueid+", 4. "+cardid);
		User user = (User)userDAO.getObjectById(new Long(userid));
		String socialnetwork = user.getSocialnetwork();
		GCM gcm = (GCM)gcmDAO.getObjectByUserIdSocialnetwork(new Long(userid), socialnetwork);
		String deviceregid = gcm.getGoogleid(); 
		//String deviceregid = user.getGoogledeviceid();
		JsonGetCard jgc = new JsonGetCard();
		jgc.setCardid(cardid);
		jgc.setUniqueid(carduniqueid);
		jgc.setType(SEARCHTYPE);
		Gson gson = new Gson();
		String userMessage = gson.toJson(jgc);
		System.out.println(userMessage);
		GooglePushMessage.sendPushMessage(deviceregid, userMessage);
	}

	public GCMDAO getGcmDAO() {
		return gcmDAO;
	}

	public void setGcmDAO(GCMDAO gcmDAO) {
		this.gcmDAO = gcmDAO;
	}
}
