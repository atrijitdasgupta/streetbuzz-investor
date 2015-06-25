/**
 * Document.java
 * 
 * @contact    -- http://www.textalytics.com (http://www.daedalus.es)
 * @copyright  -- Copyright (c) 2014, DAEDALUS S.A. All rights reserved.
 */
package es.daedalus.textalytics.sempub.domain;

import java.io.StringWriter;
import java.lang.reflect.Field;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.daedalus.textalytics.sempub.TextalyticsClientException;

/**
 * The class Document represents the input document for the semantic tagging and 
 * text proofreading services.
 * 
 * @author Textalytics
 * @version 1.0 02/2014
 */

public class Document {

	/**
	 * Possible values for the source field
	 */
	public enum Source {
		TWITTER,
		FACEBOOK,
		BLOG,
		NEWS,
		UNKNOWN, 
	}
	
	/**
	 * Possible values for the language field
	 */
	public enum Language {
		es,en,all
	}

	/**
	 * Possible values for the itf field
	 */
	public enum InputTextFormat {
		plain, markup
	}
	
	String id;
	String txt;
	Language language;
	Source source;
	String timeref;
	InputTextFormat itf;
	
	/**
	 * Document constructor with only required fields
	 * 
	 * @param id 
	 * @param txt
	 */
	public Document(String id, String txt) {
		super();
		this.id = id;
		this.txt = txt;
	}
	
	/**
	 * Document constructor with required and language fields
	 * 
	 * @param id
	 * @param txt
	 * @param lang
	 */
	public Document(String id, String txt, Language lang) {
		this(id, txt);
		language = lang;
	}
	
	/**
	 * Document constructor with all fields
	 * 	
	 * @param id
	 * @param txt
	 * @param language
	 * @param source
	 * @param timeref
	 * @param itf
	 */
	public Document(String id, String txt, Language language, Source source,
			String timeref, InputTextFormat itf) {
		this(id, txt, language);
		this.source = source;
		this.timeref = timeref;
		this.itf = itf;
	}

	/**
	 * Returns the Document object as a json string
	 *
	 * @return json string with the object
	 * @throws TextalyticsClientException
	 */
	public String toJsonString() throws TextalyticsClientException {
		ObjectMapper mapper = new ObjectMapper();
		StringWriter out = new StringWriter();

		// Object should be wrapped in document
		out.append("{\"document\":");
		try {
			mapper.writeValue(out, this);
		} catch (Exception e) {
			throw new TextalyticsClientException(500, "Problem parsing 'doc' parameter.\n", e);
		} 
		out.append("}");		
		
		return out.toString();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTxt() {
		return txt;
	}
	public void setTxt(String txt) {
		this.txt = txt;
	}
	public Language getLanguage() {
		return language;
	}
	public void setLanguage(Language language) {
		this.language = language;
	}
	public Source getSource() {
		return source;
	}
	public void setSource(Source source) {
		this.source = source;
	}
	public String getTimeref() {
		return timeref;
	}
	public void setTimeref(String timeref) {
		this.timeref = timeref;
	}
	
	public InputTextFormat getItf() {
		return itf;
	}
	public void setItf(InputTextFormat itf) {
		this.itf = itf;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Document: [");
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
