package com.crowd.streetbuzz.json;

import java.util.Date;
import java.util.List;
/*
 * id; - database id of the message.
text - the message text.
authoravatar; - the avatar (FB profile pic url) of the user.
messageby; - Who send this message, his username.
messageat; - to whom was this message sent, date and time.
mediaurl; - url pointing to any media uploaded with the message (not being used currently, once the basic messaging is fixed would work on this with you)
isowner; - whether the person who is sending the message is the owner of the message.
link; - any http link that was part of the message.
replies; - list of messages sent as replies to this message.
 */
public class JsonPrivateMessageResponse {
private Long id;
private String text;
private String authoravatar;
private String messageatavatar;
private String messageby;
private Long messagebyid;
private String messageat;
private Long messageatid;
private String mediaurl;
private boolean isowner;
private String link;
private Date date;
private List replies;
private String mediavideourl;
private String previewtext;
private Long userrating;
private String linkpreviewurl;


public String getLinkpreviewurl() {
	return linkpreviewurl;
}
public void setLinkpreviewurl(String linkpreviewurl) {
	this.linkpreviewurl = linkpreviewurl;
}
public Long getUserrating() {
	return userrating;
}
public void setUserrating(Long userrating) {
	this.userrating = userrating;
}
public String getPreviewtext() {
	return previewtext;
}
public void setPreviewtext(String previewtext) {
	this.previewtext = previewtext;
}
public String getMediavideourl() {
	return mediavideourl;
}
public void setMediavideourl(String mediavideourl) {
	this.mediavideourl = mediavideourl;
}
public String getAuthoravatar() {
	return authoravatar;
}
public void setAuthoravatar(String authoravatar) {
	this.authoravatar = authoravatar;
}
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public boolean isIsowner() {
	return isowner;
}
public void setIsowner(boolean isowner) {
	this.isowner = isowner;
}
public String getLink() {
	return link;
}
public void setLink(String link) {
	this.link = link;
}
public String getMediaurl() {
	return mediaurl;
}
public void setMediaurl(String mediaurl) {
	this.mediaurl = mediaurl;
}

public List getReplies() {
	return replies;
}
public void setReplies(List replies) {
	this.replies = replies;
}
public String getText() {
	return text;
}
public void setText(String text) {
	this.text = text;
}
public String getMessageat() {
	return messageat;
}
public void setMessageat(String messageat) {
	this.messageat = messageat;
}
public Long getMessageatid() {
	return messageatid;
}
public void setMessageatid(Long messageatid) {
	this.messageatid = messageatid;
}
public String getMessageby() {
	return messageby;
}
public void setMessageby(String messageby) {
	this.messageby = messageby;
}
public Long getMessagebyid() {
	return messagebyid;
}
public void setMessagebyid(Long messagebyid) {
	this.messagebyid = messagebyid;
}
public Date getDate() {
	return date;
}
public void setDate(Date date) {
	this.date = date;
}
public String getMessageatavatar() {
	return messageatavatar;
}
public void setMessageatavatar(String messageatavatar) {
	this.messageatavatar = messageatavatar;
}

}
