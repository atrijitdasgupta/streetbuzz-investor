/**
 * 
 */
package com.crowd.streetbuzzalgo.vo;

import java.io.Serializable;
import java.util.List;

import com.google.api.client.util.DateTime;

/**
 * @author Atrijit
 * 
 */
public class YoutubeVO implements Serializable{
	private String videoId;

	private String thumbnailurl;

	private String title;

	private String youtubeurl;

	private String description;

	private int sentimentscore;

	private String sentimentrating;

	private String positivephrase;

	private String negativephrase;

	private String neutralphrase;

	private DateTime publishdatetime;
	
	private List crawlerList;

	public List getCrawlerList() {
		return crawlerList;
	}

	public void setCrawlerList(List crawlerList) {
		this.crawlerList = crawlerList;
	}

	public DateTime getPublishdatetime() {
		return publishdatetime;
	}

	public void setPublishdatetime(DateTime publishdatetime) {
		this.publishdatetime = publishdatetime;
	}

	public String getNegativephrase() {
		return negativephrase;
	}

	public void setNegativephrase(String negativephrase) {
		this.negativephrase = negativephrase;
	}

	public String getNeutralphrase() {
		return neutralphrase;
	}

	public void setNeutralphrase(String neutralphrase) {
		this.neutralphrase = neutralphrase;
	}

	public String getPositivephrase() {
		return positivephrase;
	}

	public void setPositivephrase(String positivephrase) {
		this.positivephrase = positivephrase;
	}

	public String getSentimentrating() {
		return sentimentrating;
	}

	public void setSentimentrating(String sentimentrating) {
		this.sentimentrating = sentimentrating;
	}

	public int getSentimentscore() {
		return sentimentscore;
	}

	public void setSentimentscore(int sentimentscore) {
		this.sentimentscore = sentimentscore;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getThumbnailurl() {
		return thumbnailurl;
	}

	public void setThumbnailurl(String thumbnailurl) {
		this.thumbnailurl = thumbnailurl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getYoutubeurl() {
		return youtubeurl;
	}

	public void setYoutubeurl(String youtubeurl) {
		this.youtubeurl = youtubeurl;
	}

}
