/**
 * 
 */
package com.crowd.streetbuzzalgo.yandexsearch;

import java.util.Date;

/**
 * @author Atrijit
 *
 */
public class YandexVO {
public String url;
public String title;
public Date moddate;
public Date getModdate() {
	return moddate;
}
public void setModdate(Date moddate) {
	this.moddate = moddate;
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
