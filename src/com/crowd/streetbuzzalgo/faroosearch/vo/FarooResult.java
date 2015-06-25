/**
 * 
 */
package com.crowd.streetbuzzalgo.faroosearch.vo;

/**
 * @author Atrijit
 * 
 */
public class FarooResult {
	private String title;

	private String kwic;

	private String url;

	private String iurl;

	private String domain;

	private String author;

	private boolean news;

	private String date;

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getIurl() {
		return iurl;
	}

	public void setIurl(String iurl) {
		this.iurl = iurl;
	}

	public String getKwic() {
		return kwic;
	}

	public void setKwic(String kwic) {
		this.kwic = kwic;
	}

	public boolean isNews() {
		return news;
	}

	public void setNews(boolean news) {
		this.news = news;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
