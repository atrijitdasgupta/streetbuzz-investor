/**
 * 
 */
package com.crowd.streetbuzz.json;

import java.io.Serializable;

/**
 * @author Atrijit
 *
 */
public class JsonSubComments implements Serializable{
	static final long serialVersionUID = 1000000007L;
	private String postid;
	private String commentid;
	private String subcommentid;
	private String subcommenttext;
//	Facebook - GooglePlus - Twitter
	private String origin;
	
	public String getCommentid() {
		return commentid;
	}
	public void setCommentid(String commentid) {
		this.commentid = commentid;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getPostid() {
		return postid;
	}
	public void setPostid(String postid) {
		this.postid = postid;
	}
	public String getSubcommentid() {
		return subcommentid;
	}
	public void setSubcommentid(String subcommentid) {
		this.subcommentid = subcommentid;
	}
	public String getSubcommenttext() {
		return subcommenttext;
	}
	public void setSubcommenttext(String subcommenttext) {
		this.subcommenttext = subcommenttext;
	}
}
