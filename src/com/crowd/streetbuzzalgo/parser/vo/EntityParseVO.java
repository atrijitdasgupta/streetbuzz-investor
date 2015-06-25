/**
 * 
 */
package com.crowd.streetbuzzalgo.parser.vo;

import java.util.List;

/**
 * @author Atrijit
 * 
 */
public class EntityParseVO {
	private String sentence;

	private List LOCATION;

	private List TIME;

	private List PERSON;

	private List ORGANIZATION;

	private List MONEY;

	private List PERCENT;

	private List DATE;

	public List getDATE() {
		return DATE;
	}

	public void setDATE(List date) {
		DATE = date;
	}

	public List getLOCATION() {
		return LOCATION;
	}

	public void setLOCATION(List location) {
		LOCATION = location;
	}

	public List getMONEY() {
		return MONEY;
	}

	public void setMONEY(List money) {
		MONEY = money;
	}

	public List getORGANIZATION() {
		return ORGANIZATION;
	}

	public void setORGANIZATION(List organization) {
		ORGANIZATION = organization;
	}

	public List getPERCENT() {
		return PERCENT;
	}

	public void setPERCENT(List percent) {
		PERCENT = percent;
	}

	public List getPERSON() {
		return PERSON;
	}

	public void setPERSON(List person) {
		PERSON = person;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public List getTIME() {
		return TIME;
	}

	public void setTIME(List time) {
		TIME = time;
	}

}
