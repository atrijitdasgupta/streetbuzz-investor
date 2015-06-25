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
import com.crowd.streetbuzz.dao.implementation.AccessTokenDAO;
import com.crowd.streetbuzz.dao.implementation.CardExclusionDAO;
import com.crowd.streetbuzz.dao.implementation.CategoryMasterDAO;
import com.crowd.streetbuzz.dao.implementation.ConversationCardDAO;
import com.crowd.streetbuzz.dao.implementation.DistributionDAO;
import com.crowd.streetbuzz.dao.implementation.MediaFilesDAO;
import com.crowd.streetbuzz.dao.implementation.SentimentQueueDAO;
import com.crowd.streetbuzz.dao.implementation.UserCategoryMapDAO;
import com.crowd.streetbuzz.dao.implementation.UserDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDetailsDAO;
import com.crowd.streetbuzz.dao.implementation.WordCloudStoreDAO;
import com.crowd.streetbuzz.model.AccessToken;
import com.crowd.streetbuzz.model.ConversationCard;
import com.crowd.streetbuzzalgo.utils.ConversationProcessingUtils;
import com.crowd.streetbuzzalgo.utils.ProcessingUtils;

/**
 * @author Atrijit
 * 
 */
public class RefreshConversationController implements Controller, Constants {
	private AccessTokenDAO accessTokenDAO;

	private ConversationCardDAO conversationCardDAO;

	private UserDAO userDAO;

	private UserCategoryMapDAO userCategoryMapDAO;

	private CategoryMasterDAO categoryMasterDAO;

	private VoicesDAO voicesDAO;

	private VoicesDetailsDAO voicesDetailsDAO;

	private DistributionDAO distributionDAO;

	private CardExclusionDAO cardExclusionDAO;

	private SentimentQueueDAO sentimentQueueDAO;

	private MediaFilesDAO mediaFilesDAO;

	private WordCloudStoreDAO wordCloudStoreDAO;

	public WordCloudStoreDAO getWordCloudStoreDAO() {
		return wordCloudStoreDAO;
	}

	public void setWordCloudStoreDAO(WordCloudStoreDAO wordCloudStoreDAO) {
		this.wordCloudStoreDAO = wordCloudStoreDAO;
	}

	public MediaFilesDAO getMediaFilesDAO() {
		return mediaFilesDAO;
	}

	public void setMediaFilesDAO(MediaFilesDAO mediaFilesDAO) {
		this.mediaFilesDAO = mediaFilesDAO;
	}

	public SentimentQueueDAO getSentimentQueueDAO() {
		return sentimentQueueDAO;
	}

	public void setSentimentQueueDAO(SentimentQueueDAO sentimentQueueDAO) {
		this.sentimentQueueDAO = sentimentQueueDAO;
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

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public AccessTokenDAO getAccessTokenDAO() {
		return accessTokenDAO;
	}

	public void setAccessTokenDAO(AccessTokenDAO accessTokenDAO) {
		this.accessTokenDAO = accessTokenDAO;
	}

	public ConversationCardDAO getConversationCardDAO() {
		return conversationCardDAO;
	}

	public void setConversationCardDAO(ConversationCardDAO conversationCardDAO) {
		this.conversationCardDAO = conversationCardDAO;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		run();
		return null;
	}

	private void run() {
		List accessTokenList = accessTokenDAO.getAllRecords();
		AccessToken accessToken = (AccessToken) accessTokenList.get(0);
		String fbtoken = accessToken.getAccesstoken();
		// first get the list of Refreshed Conversation cards to be spotated
		List ccList = conversationCardDAO.getAllPendingRefreshedRecords();
		for (int i = 0; i < ccList.size(); i++) {
			ConversationCard cc = (ConversationCard) ccList.get(i);
			// Update the conversation card as under processing
			cc.setAction(UNDERREFRESHPROCESSING);
			conversationCardDAO.addOrUpdateRecord(cc);
			ConversationProcessingUtils.processConversation(cc, fbtoken,
					conversationCardDAO, userDAO, userCategoryMapDAO,
					categoryMasterDAO, voicesDAO, voicesDetailsDAO,
					distributionDAO, cardExclusionDAO, sentimentQueueDAO,
					mediaFilesDAO,wordCloudStoreDAO);
		}
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
