/**
 * Uri.java
 * 
 * @contact    http://www.textalytics.com (http://www.daedalus.es)
 * @copyright  Copyright (c) 2014, DAEDALUS S.A. All rights reserved.
 */
package es.daedalus.textalytics.sempub.domain;

import java.lang.reflect.Field;

/**
 * Uri class represents an uri within a text in the response of the 
 * Semantic Tagging service.
 * 
 * @author Textalytics
 * @version 1.0 02/2014
 */
public class Uri {

	/**
	 * Possible values for the type field within an uri object in the 
	 * Semantic Tagging service response.
	 */
	public enum UriType {
		url,email
	}
	
	private String form;
	private UriType type;
	private int inip;
	private int endp;

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public UriType getType() {
		return type;
	}

	public void setType(UriType type) {
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

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		for(Field field : getClass().getDeclaredFields()) {
			try {
				if(field.get(this) != null)
					buffer.append(field.getName() + "=" + field.get(this) + ", ");
			} catch (Exception e) {}
		}
		return buffer.toString();
	}	
}
