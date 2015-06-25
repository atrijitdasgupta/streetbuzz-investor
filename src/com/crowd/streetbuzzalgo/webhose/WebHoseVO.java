/**
 * 
 */
package com.crowd.streetbuzzalgo.webhose;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Atrijit
 * 
 */
public class WebHoseVO implements Serializable {
	private String author;

	private String text;

	private String title;
	
	private Date date;

	List crawlerList;

	private String url;
	
	private String sentiment;

	public String getSentiment() {
		return sentiment;
	}

	public void setSentiment(String sentiment) {
		this.sentiment = sentiment;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List getCrawlerList() {
		return crawlerList;
	}

	public void setCrawlerList(List crawlerList) {
		this.crawlerList = crawlerList;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
