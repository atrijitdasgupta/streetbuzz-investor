/**
 * 
 */
package com.crowd.streetbuzz.json;

import java.util.List;

/**
 * @author Atrijit
 * 
 */
public class StatisticsJson {
	private Long messages;

	private Long friends;

	private Long bookmarks;

	private List interestcountList;

	public Long getBookmarks() {
		return bookmarks;
	}

	public void setBookmarks(Long bookmarks) {
		this.bookmarks = bookmarks;
	}

	public Long getFriends() {
		return friends;
	}

	public void setFriends(Long friends) {
		this.friends = friends;
	}

	public List getInterestcountList() {
		return interestcountList;
	}

	public void setInterestcountList(List interestcountList) {
		this.interestcountList = interestcountList;
	}

	public Long getMessages() {
		return messages;
	}

	public void setMessages(Long messages) {
		this.messages = messages;
	}

}
