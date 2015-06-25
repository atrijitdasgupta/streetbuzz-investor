/**
 * Suggestion.java
 * 
 * @contact    http://www.textalytics.com (http://www.daedalus.es)
 * @copyright  Copyright (c) 2014, DAEDALUS S.A. All rights reserved.
 */

package es.daedalus.textalytics.sempub.domain.auxiliary;

import java.lang.reflect.Field;

/**
 * Suggestion class represents a suggestion object within an issue in the response of the 
 * Text Proofreading service.
 * 
 * @author Textalytics
 * @version 1.0 02/2014
 */
public class Suggestion {

	private String form;
	private int confidence;
	
	public String getForm() {
		return form;
	}
	public void setForm(String form) {
		this.form = form;
	}
	public int getConfidence() {
		return confidence;
	}
	public void setConfidence(int confidence) {
		this.confidence = confidence;
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
