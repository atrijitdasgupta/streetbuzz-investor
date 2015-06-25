/**
 * Quotation.java
 * 
 * @contact    http://www.textalytics.com (http://www.daedalus.es)
 * @copyright  Copyright (c) 2014, DAEDALUS S.A. All rights reserved.
 */

package es.daedalus.textalytics.sempub.domain;

import java.lang.reflect.Field;

/**
 * Quotation class represents a quotation within a text in the response of the 
 * Semantic Tagging service.
 * 
 * @author Textalytics
 * @version 1.0 02/2014
 */
public class Quotation {

	private String form;
	private String who;
	private String whoLemma;
	private String verb;
	private String verbLemma;
	private int inip;
	private int endp;
	public String getForm() {
		return form;
	}
	public void setForm(String form) {
		this.form = form;
	}
	public String getWho() {
		return who;
	}
	public void setWho(String who) {
		this.who = who;
	}
	public String getWhoLemma() {
		return whoLemma;
	}
	public void setWhoLemma(String whoLemma) {
		this.whoLemma = whoLemma;
	}
	public String getVerb() {
		return verb;
	}
	public void setVerb(String verb) {
		this.verb = verb;
	}
	public String getVerbLemma() {
		return verbLemma;
	}
	public void setVerbLemma(String verbLemma) {
		this.verbLemma = verbLemma;
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
		buffer.append("Quotation: [");
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
