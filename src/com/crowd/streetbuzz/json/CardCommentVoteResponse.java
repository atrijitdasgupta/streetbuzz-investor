/**
 * 
 */
package com.crowd.streetbuzz.json;

/**
 * @author Atrijit
 *
 */
public class CardCommentVoteResponse {
	private Long likecount;
	
	//private Long upcount;

	//private Long downcount;
	
	private boolean isliked;

	public boolean isIsliked() {
		return isliked;
	}

	public void setIsliked(boolean isliked) {
		this.isliked = isliked;
	}

	public Long getLikecount() {
		return likecount;
	}

	public void setLikecount(Long likecount) {
		this.likecount = likecount;
	}

//	private boolean isupvoted;

//	private boolean isdownvoted;

	
	
}
