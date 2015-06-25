/**
 * 
 */
package com.crowd.streetbuzz.json;

import java.io.Serializable;
import java.util.List;

/**
 * @author Atrijit
 *
 */
public class JsonComments implements Serializable{
	static final long serialVersionUID = 1000000006L;
	private String postid;
	private String commentid;
	private String commenttext;
	//Facebook - GooglePlus - Twitter
	private String origin;
	private List <JsonSubComments> subcomments;
	
	public String getCommentid() {
		return commentid;
	}
	public void setCommentid(String commentid) {
		this.commentid = commentid;
	}
	public String getCommenttext() {
		return commenttext;
	}
	public void setCommenttext(String commenttext) {
		this.commenttext = commenttext;
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
	public List<JsonSubComments> getSubcomments() {
		return subcomments;
	}
	public void setSubcomments(List<JsonSubComments> subcomments) {
		this.subcomments = subcomments;
	}
	
}
