/**
 * 
 */
package com.crowd.streetbuzz.json;

/**
 * @author Atrijit
 * 
 */
public class JsonVoiceCommentsIncoming {

	private String voiceid;

	private String comment;

	private String link;

	private String mediaid;

	private String rating;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getMediaid() {
		return mediaid;
	}

	public void setMediaid(String mediaid) {
		this.mediaid = mediaid;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getVoiceid() {
		return voiceid;
	}

	public void setVoiceid(String voiceid) {
		this.voiceid = voiceid;
	}
}
