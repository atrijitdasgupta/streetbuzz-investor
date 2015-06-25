/**
 * 
 */
package com.crowd.streetbuzzalgo.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Atrijit
 *
 */
public class SearchDemoVO implements Serializable{
private String url;
private String title;
private Date date;
private String searchsource;
private String channel;

public String getChannel() {
	return channel;
}
public void setChannel(String channel) {
	this.channel = channel;
}
public String getSearchsource() {
	return searchsource;
}
public void setSearchsource(String searchsource) {
	this.searchsource = searchsource;
}
public Date getDate() {
	return date;
}
public void setDate(Date date) {
	this.date = date;
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
