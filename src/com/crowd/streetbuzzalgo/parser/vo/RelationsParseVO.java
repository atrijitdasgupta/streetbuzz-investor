/**
 * 
 */
package com.crowd.streetbuzzalgo.parser.vo;

import java.util.List;

/**
 * @author Atrijit
 *
 */
public class RelationsParseVO {
	private List subject;
	private List subjectactiontext;
	private List subjectverbtext;
	
	private List subjectentities;
	private List locationentities;
	private List objecttext;
	private List objectentities;
	
	public List getLocationentities() {
		return locationentities;
	}
	public void setLocationentities(List locationentities) {
		this.locationentities = locationentities;
	}
	public List getObjectentities() {
		return objectentities;
	}
	public void setObjectentities(List objectentities) {
		this.objectentities = objectentities;
	}
	public List getObjecttext() {
		return objecttext;
	}
	public void setObjecttext(List objecttext) {
		this.objecttext = objecttext;
	}
	public List getSubject() {
		return subject;
	}
	public void setSubject(List subject) {
		this.subject = subject;
	}
	public List getSubjectactiontext() {
		return subjectactiontext;
	}
	public void setSubjectactiontext(List subjectactiontext) {
		this.subjectactiontext = subjectactiontext;
	}
	public List getSubjectentities() {
		return subjectentities;
	}
	public void setSubjectentities(List subjectentities) {
		this.subjectentities = subjectentities;
	}
	public List getSubjectverbtext() {
		return subjectverbtext;
	}
	public void setSubjectverbtext(List subjectverbtext) {
		this.subjectverbtext = subjectverbtext;
	}
	
	
}
