/**
 * 
 */
package com.crowd.streetbuzzalgo.deduplicate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.CategoryMasterDAO;
import com.crowd.streetbuzz.dao.implementation.ConversationCardDAO;
import com.crowd.streetbuzz.model.ConversationCard;
import com.crowd.streetbuzzalgo.constants.SystemConstants;

/**
 * @author Atrijit
 * 
 */
public class Deduplicator implements Constants, SystemConstants {
	// private static final double threshold = 0.95;

	/**
	 * 
	 */
	public Deduplicator() {
		// TODO Auto-generated constructor stub
	}

	public static Long deduplicate(ConversationCardDAO conversationCardDAO,
			CategoryMasterDAO categoryMasterDAO, String topic, String interestid) {

		List selectedcclist = new ArrayList();

		List topics = new ArrayList();
		topics.add(topic);

		Long catid = new Long(interestid);
		List cclist = conversationCardDAO
				.getAllReadyRecordsInterestDedup(catid);
		for (int j = 0; j < cclist.size(); j++) {
			ConversationCard cc = (ConversationCard) cclist.get(j);
			String topiccompare = cc.getTopic();
			for (int k = 0; k < topics.size(); k++) {
				String comparable = (String) topics.get(k);
				
				boolean bool = LetterPairSimilarity.stringCompare(topiccompare,
						comparable);
				if (bool) {
					selectedcclist.add(cc.getId());
				}
			}

		}

		Collections.sort(selectedcclist);
		int size = selectedcclist.size();
		Long selectlong = new Long(0);
		if (size > 0) {
			selectlong = (Long) selectedcclist.get(size - 1);
			// selectlong = (Long)selectedcclist.get(0);

		}

		return selectlong;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
