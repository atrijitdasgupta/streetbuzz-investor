/**
 * TimeExpression.java
 * 
 * @contact    http://www.textalytics.com (http://www.daedalus.es)
 * @copyright  Copyright (c) 2014, DAEDALUS S.A. All rights reserved.
 */

package es.daedalus.textalytics.sempub.domain;

import java.lang.reflect.Field;

/**
 * TimeExpression class represents a time expression within a text in the response of the 
 * Semantic Tagging service.
 * 
 * @author Textalytics
 * @version 1.0 02/2014
 */
public class TimeExpression {

	private String form;
	private String date;
	private String time;
	private int inip;
	private int endp;
	
	public String getForm() {
		return form;
	}
	public void setForm(String form) {
		this.form = form;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
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
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("TimeExpression: [");
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
