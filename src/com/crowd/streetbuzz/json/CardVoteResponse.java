/**
 * 
 */
package com.crowd.streetbuzz.json;

/**
 * @author Atrijit
 * 
 */
public class CardVoteResponse {
	private Long upcount;

	private Long downcount;

	private boolean isupvoted;

	private boolean isdownvoted;

	public Long getDowncount() {
		return downcount;
	}

	public void setDowncount(Long downcount) {
		this.downcount = downcount;
	}

	public boolean isIsdownvoted() {
		return isdownvoted;
	}

	public void setIsdownvoted(boolean isdownvoted) {
		this.isdownvoted = isdownvoted;
	}

	public boolean isIsupvoted() {
		return isupvoted;
	}

	public void setIsupvoted(boolean isupvoted) {
		this.isupvoted = isupvoted;
	}

	public Long getUpcount() {
		return upcount;
	}

	public void setUpcount(Long upcount) {
		this.upcount = upcount;
	}

}
