/**
 * LinkedData.java
 * 
 * @contact    http://www.textalytics.com (http://www.daedalus.es)
 * @copyright  Copyright (c) 2014, DAEDALUS S.A. All rights reserved.
 */

package es.daedalus.textalytics.sempub.domain.auxiliary;

import java.lang.reflect.Field;

/**
 * LinkedData class represents a linked data resource within the response of the 
 * Semantic Tagging service.
 * 
 * @author Textalytics
 * @version 1.0 02/2014
 */
public class LinkedData {

	private String href;
	private String source;
	
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
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
