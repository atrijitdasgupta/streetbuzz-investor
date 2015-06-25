/**
 * 
 */
package com.crowd.streetbuzz.json;

/**
 * @author Atrijit
 *
 */
public class JsonUserActivities {
private Long id;
private String activititytype;
private String activitytext;
private String activityarea;
private String points;
private String datetime;

public String getActivititytype() {
	return activititytype;
}
public void setActivititytype(String activititytype) {
	this.activititytype = activititytype;
}
public String getActivityarea() {
	return activityarea;
}
public void setActivityarea(String activityarea) {
	this.activityarea = activityarea;
}
public String getActivitytext() {
	return activitytext;
}
public void setActivitytext(String activitytext) {
	this.activitytext = activitytext;
}
public String getDatetime() {
	return datetime;
}
public void setDatetime(String datetime) {
	this.datetime = datetime;
}
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public String getPoints() {
	return points;
}
public void setPoints(String points) {
	this.points = points;
}

}
