/**
 * 
 */
package com.crowd.streetbuzzalgo.distribution;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.CardExclusionDAO;
import com.crowd.streetbuzz.dao.implementation.CategoryMasterDAO;
import com.crowd.streetbuzz.dao.implementation.DistributionDAO;
import com.crowd.streetbuzz.dao.implementation.UserCategoryMapDAO;
import com.crowd.streetbuzz.dao.implementation.UserDAO;
import com.crowd.streetbuzz.json.JsonGetCard;
import com.crowd.streetbuzz.json.JsonNotification;
import com.crowd.streetbuzz.json.JsonUserDistributionData;
import com.crowd.streetbuzz.model.CardExclusion;
import com.crowd.streetbuzz.model.ConversationCard;
import com.crowd.streetbuzz.model.SearchCard;
import com.crowd.streetbuzz.model.User;
import com.crowd.streetbuzz.utils.ApplePushMessage;
import com.crowd.streetbuzz.utils.GooglePushMessage;
import com.crowd.streetbuzzalgo.utils.GeoCodingReverseLookupUtils;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;

/**
 * @author Atrijit
 * 
 */
public class Distribution implements Constants{

	/**
	 * 
	 */
	public Distribution() {
		// TODO Auto-generated constructor stub
	}
	
	public static List getDistributionList(String requserid, String cardid, UserDAO userDAO){
		List allUsersList = userDAO.getAllRecords();
		User requser = (User)userDAO.getObjectById(new Long(requserid));
		List finalList = new ArrayList();
		for (int i=0;i<allUsersList.size();i++){
			User user = (User)allUsersList.get(i);
			JsonUserDistributionData judd = new JsonUserDistributionData();
			
			/*Long id = user.getId();
			int idint = id.intValue();
			int selfidint = selfid.intValue();
			if(selfidint == idint){
				continue;
			}
			String name = StrUtil.nonNull(user.getName());
			String avatar = StrUtil.nonNull(user.getAvatar());
			String usersocialnetwork = StrUtil.nonNull(user.getSocialnetwork());
			
			String city = StrUtil.nonNull(user.getCity());
			String country = StrUtil.nonNull(user.getCountry());
			String distance = "City: "+city+", Country: "+country;
			
			judd.setAvatar(avatar);
			judd.setId(id);
			judd.setName(name);
			judd.setUsersocialnetwork(usersocialnetwork);
			judd.setDistance(distance);*/
			
			String avatar = StrUtil.nonNull(user.getAvatar());
			Long userid = user.getId();
			judd.setId(userid);
			judd.setAvatar(avatar);
			String name = StrUtil.nonNull(user.getName());
			String usersocialnetwork = StrUtil.nonNull(user.getSocialnetwork());
			String city = StrUtil.nonNull(user.getCity());
			String country = StrUtil.nonNull(user.getCountry());
			String location = city;
			
			String mylat = requser.getLatitude();
			String mylon = requser.getLongitude();
			String yourlat = user.getLatitude();
			String yourlon = user.getLongitude();
			
			String distancestr = GeoCodingReverseLookupUtils.distance(mylat,mylon,yourlat,yourlon);

			judd.setName(name);
			judd.setLocation(location);
			judd.setUsersocialnetwork(usersocialnetwork);
			judd.setDistance(distancestr);
			
			finalList.add(judd);
		}
		return finalList;
	}
	

	public static void distribute(SearchCard obj, UserDAO userDAO,
			UserCategoryMapDAO userCategoryMapDAO,
			CategoryMasterDAO categoryMasterDAO) {
		String useridStr = obj.getUserid();
		String socialnetwork = obj.getSocialnetwork();
		/*User user = (User) userDAO.getObjectByUserIdSocialnetwork(useridStr,
				socialnetwork);*/
		/*User user = (User) userDAO.getObjectByUserIdSocialnetwork("testuser@facebook.com",
				"FB");*/
		User user = (User)userDAO.getObjectById(new Long(1));
		Long uid = user.getId();
		
		List userCatMapList = userCategoryMapDAO.getAllCategoriesforUser(uid);
		List distributionList = new ArrayList();
		distributionList.add(new Long(1));
		Gson gson = new Gson();
		/*for (int i = 0; i < userCatMapList.size(); i++) {
			UserCategoryMap ucm = (UserCategoryMap) userCatMapList.get(i);
			Long categoryid = ucm.getCategoryid();
			List userList = userCategoryMapDAO
					.getAllUsersforCategory(categoryid);
			for (int j = 0; j < userList.size(); j++) {
				Long userid = (Long) userList.get(j);
				if (!distributionList.contains(userid)) {
					distributionList.add(userid);
				}
			}
		}
		distributionList = spotateDistributionList(distributionList);*/
		
		
		for (int k = 0; k < distributionList.size(); k++) {
			Long id = (Long) distributionList.get(k);
			JsonGetCard jgc = new JsonGetCard();
			jgc.setType(CONVERSATIONTYPE);
			jgc.setCardid(obj.getId().toString());
			jgc.setVoices("N");
			jgc.setUniqueid(obj.getUniqueid());
			User toUser = (User) userDAO.getObjectById(id);
			String googleid = StrUtil.nonNull(toUser.getGoogledeviceid());
			String appleid = StrUtil.nonNull(toUser.getAppledeviceid());
			String userMsg = gson.toJson(jgc);
			if (!"".equalsIgnoreCase(googleid)) {
				GooglePushMessage.sendPushMessage(googleid, userMsg);
			}
			if (!"".equalsIgnoreCase(appleid)) {
				GooglePushMessage.sendPushMessage(appleid, userMsg);
			}
		}

	}
	/*
	 * Method to do special omissions / additions to list - currently left blank.
	 */
	private static List spotateDistributionList(List distributionList){
		return distributionList;
	}

	public static void distribute(ConversationCard obj, UserDAO userDAO,
			UserCategoryMapDAO userCategoryMapDAO,
			CategoryMasterDAO categoryMasterDAO, DistributionDAO distributionDAO,CardExclusionDAO cardExclusionDAO) {
		String useridStr = obj.getUserid();
		Long userid = new Long(useridStr);
		List exclusionlist = cardExclusionDAO.getAllRecordsByUserId(userid);
		Long cardid = obj.getId();
		int cardidint = cardid.intValue();
		for (int i=0;i<exclusionlist.size();i++){
			CardExclusion ce = (CardExclusion)exclusionlist.get(i);
			Long ceid = ce.getCardid();
			int ceidint = ceid.intValue();
			if(ceidint == cardidint){
				System.out.println("Exclusion match with card id:: "+ cardidint);
				return;
			}
			
		}
		/*User user = (User) userDAO.getObjectById(userid);
		Long uid = user.getId();
		List userCatMapList = userCategoryMapDAO.getAllCategoriesforUser(uid);*/
		List distributionList = userDAO.getAllRecords();
		Gson gson = new Gson();
		/*for (int i = 0; i < userCatMapList.size(); i++) {
			UserCategoryMap ucm = (UserCategoryMap) userCatMapList.get(i);
			Long categoryid = ucm.getCategoryid();
			List userList = userCategoryMapDAO
					.getAllUsersforCategory(categoryid);
			for (int j = 0; j < userList.size(); j++) {
				Long userid = (Long) userList.get(j);
				if (!distributionList.contains(userid)) {
					distributionList.add(userid);
				}
			}
		}*/
		distributionList = spotateDistributionList(distributionList);
		
		for (int k = 0; k < distributionList.size(); k++) {
			User usernew = (User)distributionList.get(k);
			Long id = usernew.getId();
			//Long id = (Long) distributionList.get(k);
			JsonNotification jn = new JsonNotification();
			jn.setId(obj.getId().toString());
			jn.setInterestid(obj.getInterestid().toString());
			jn.setType("CARD");
			/*JsonGetCard jgc = new JsonGetCard();
			jgc.setType(CONVERSATIONTYPE);
			jgc.setCardid(obj.getId().toString());
			jgc.setVoices("N");
			jgc.setUniqueid(jgc.getUniqueid());*/
			User toUser = (User) userDAO.getObjectById(id);
			String googleid = StrUtil.nonNull(toUser.getGoogledeviceid());
			String appleid = StrUtil.nonNull(toUser.getAppledeviceid());
			String userMsg = gson.toJson(jn);
			if (!"".equalsIgnoreCase(googleid)) {
				GooglePushMessage.sendPushMessage(googleid, userMsg);
			}
			if (!"".equalsIgnoreCase(appleid)) {
				ApplePushMessage.sendPushMessage(appleid, userMsg);
			}
			com.crowd.streetbuzz.model.Distribution dist = new com.crowd.streetbuzz.model.Distribution();
			dist.setCardid(obj.getId());
			dist.setDestinationuserid(toUser.getId());
			dist.setDistributiondate(new Date());
			dist.setInterestid(obj.getInterestid());
			dist.setSourceuserid(userid);
			dist.setNewflag("");
			dist.setFlag("");
			distributionDAO.addOrUpdateRecord(dist);
			
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
