/**
 * Status.java
 * 
 * @contact    http://www.textalytics.com (http://www.daedalus.es)
 * @copyright  Copyright (c) 2014, DAEDALUS S.A. All rights reserved.
 */

package es.daedalus.textalytics.sempub;

import java.lang.reflect.Field;

/**
 * Status class represents the status object within the response for the 
 * three different services.
 * 
 * @author Textalytics
 * @version 1.0 02/2014
 */
public class Status {
		private int code;
		private String message;
		private String moreInfo;
		
		public int getCode() {
			return code;
		}
		public void setCode(int code) {
			this.code = code;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public String getMoreInfo() {
			return moreInfo;
		}
		public void setMoreInfo(String moreInfo) {
			this.moreInfo = moreInfo;
		}
		
		@Override
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("Status: [");
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
