/**
 * 
 */
package com.crowd.streetbuzz.json;

/**
 * @author Atrijit
 *
 */
public class CardUpDownVoteResponse {
private boolean isupvoted;
private boolean isdownvoted;
private Long upvotecount;
private Long downvotecount;
public Long getDownvotecount() {
	return downvotecount;
}
public void setDownvotecount(Long downvotecount) {
	this.downvotecount = downvotecount;
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
public Long getUpvotecount() {
	return upvotecount;
}
public void setUpvotecount(Long upvotecount) {
	this.upvotecount = upvotecount;
}

}
