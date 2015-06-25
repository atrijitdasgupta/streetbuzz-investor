/**
 * Stats.java
 * 
 * @contact    http://www.textalytics.com (http://www.daedalus.es)
 * @copyright  Copyright (c) 2014, DAEDALUS S.A. All rights reserved.
 */

package es.daedalus.textalytics.sempub.domain.auxiliary;

import java.lang.reflect.Field;

/**
 * Stats class represents the stats object field in a dictionary/model when reading the 
 * User Resources Management service response.
 * 
 * @author Textalytics
 * @version 1.0 02/2014
 */
public class Stats {
	
	private int entries;

	public int getEntries() {
		return entries;
	}

	public void setEntries(int entries) {
		this.entries = entries;
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
