/**
 * 
 */
package com.crowd.streetbuzz.json;

/**
 * @author Atrijit
 *
 */
/*
 * Remove uniqueid from current notification.
Include type.
Make "cardid" to "id"
Notifications type to be included:
CARD, VOICE, MESSAGE
Based on type, connect to:
GetHomePageCardsController / GetMessagesController / 

For refresh individual voice, pick voicesid from jsonheader
 */
public class JsonNotification {
	private String id;
	/*
	 * CARD
	 * VOICE
	 * MESSAGE
	 */
	private String type;
	private String interestid;
	public String getInterestid() {
		return interestid;
	}
	public void setInterestid(String interestid) {
		this.interestid = interestid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
