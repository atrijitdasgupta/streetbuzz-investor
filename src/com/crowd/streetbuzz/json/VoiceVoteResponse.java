/**
 * 
 */
package com.crowd.streetbuzz.json;

/**
 * @author Atrijit
 *
 */
public class VoiceVoteResponse {
	private Long likecount;

//	private Long dislikecount;

	private boolean isliked;

//	private boolean isdisliked;


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
	
}
