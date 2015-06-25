/**
 * Variant.java
 * 
 * @contact    http://www.textalytics.com (http://www.daedalus.es)
 * @copyright  Copyright (c) 2014, DAEDALUS S.A. All rights reserved.
 */

package es.daedalus.textalytics.sempub.domain.auxiliary;

import java.lang.reflect.Field;


/**
 * Variant class represents a variant object in the response of the Semantic Tagging service. 
 * It models a mention of a term within a text. 
 * 
 * @author Textalytics
 * @version 1.0 02/2014
 */
public class Variant {

	private String form;
	private int inip;
	private int endp;
	
	public String getForm() {
		return form;
	}
	public void setForm(String form) {
		this.form = form;
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
