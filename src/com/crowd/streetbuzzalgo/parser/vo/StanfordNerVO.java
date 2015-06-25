/**
 * 
 */
package com.crowd.streetbuzzalgo.parser.vo;

import java.util.List;

/**
 * @author Atrijit
 *
 */
public class StanfordNerVO {
private String entry;
private String parsexml;
private List location;
private List time;
private List person;
private List organization;
private List money;
private List percent;
private List date;

public List getDate() {
	return date;
}
public void setDate(List date) {
	this.date = date;
}
public List getLocation() {
	return location;
}
public void setLocation(List location) {
	this.location = location;
}
public List getMoney() {
	return money;
}
public void setMoney(List money) {
	this.money = money;
}
public List getOrganization() {
	return organization;
}
public void setOrganization(List organization) {
	this.organization = organization;
}
public List getPercent() {
	return percent;
}
public void setPercent(List percent) {
	this.percent = percent;
}
public List getPerson() {
	return person;
}
public void setPerson(List person) {
	this.person = person;
}
public List getTime() {
	return time;
}
public void setTime(List time) {
	this.time = time;
}
public String getEntry() {
	return entry;
}
public void setEntry(String entry) {
	this.entry = entry;
}
public String getParsexml() {
	return parsexml;
}
public void setParsexml(String parsexml) {
	this.parsexml = parsexml;
}
}
