/**
 * Issue.java
 * 
 * @contact    http://www.textalytics.com (http://www.daedalus.es)
 * @copyright  Copyright (c) 2014, DAEDALUS S.A. All rights reserved.
 */
package es.daedalus.textalytics.sempub.domain;

import java.lang.reflect.Field;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import es.daedalus.textalytics.sempub.domain.auxiliary.Suggestion;

/**
 * The class Issue represents a warning or error in a text when using the
 * Text Proofreading service.
 * 
 * @author Textalytics
 * @version 1.0 02/2014
 */
public class Issue {

	/**
	 * Possible values for the type field of an issue
	 */
	public enum IssueType {
		spelling, grammar, style, typography, semantic
	}
	
	private String text;
	private IssueType type;
	private int inip;
	private int endp;
	private String msg;
	@JsonProperty("sug_list")
	private List<Suggestion> sugList;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public IssueType getType() {
		return type;
	}
	public void setType(IssueType type) {
		this.type = type;
	}
	public int getInip() {
		return inip;
	}
	public void setInip(int inip) {
		this.inip = inip;
	}
	public int getEndp() {
		return endp;
	}
	public void setEndp(int endp) {
		this.endp = endp;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public List<Suggestion> getSugList() {
		return sugList;
	}
	public void setSugList(List<Suggestion> sugList) {
		this.sugList = sugList;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Issue: [");
		for(Field field : getClass().getDeclaredFields()) {
			try {
				if(field.get(this) != null)
					buffer.append(field.getName() + "=" + field.get(this) + ", ");
			} catch (Exception e) {}
		}
		buffer.append("]\n");
		return buffer.toString();
	}
}
