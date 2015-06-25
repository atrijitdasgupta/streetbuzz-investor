/**
 * Dictionary.java
 *
 * @contact    -- http://www.textalytics.com (http://www.daedalus.es)
 * @copyright  -- Copyright (c) 2014, DAEDALUS S.A. All rights reserved.
 */

package es.daedalus.textalytics.sempub.domain;

import java.io.StringWriter;
import java.lang.reflect.Field;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.daedalus.textalytics.sempub.TextalyticsClientException;
import es.daedalus.textalytics.sempub.domain.Document.Language;
import es.daedalus.textalytics.sempub.domain.auxiliary.Stats;

/**
 * The class Dictionary represents a collection that contains entities and concepts; it models
 * the object used in the user resources management service (endpoints: 
 * /dictionary_list/ and /dictionary_list/{name}).
 *
 * @author Textalytics  
 * @version 1.0 02/2014
 */

public class Dictionary implements Resource{

	private Stats stats;
	private String name;
	private Language language;
	private String description;
	
	// Dummy constructor for Jackson
	public Dictionary() {}
	
	/**
	 * Class constructor
	 * 
	 * @param name field that identifies univocally a dictionary
	 * @param language language of the dictionary
	 * @param description field with a description of the dictionary contents
	 */
	public Dictionary(String name, Language language, String description) {
		this.name = name;
		this.language = language;
		this.description = description;
	}
	
	public Dictionary(String name, Language language) {
		this.name = name;
		this.language = language;
	}
	
	/**
	 * Returns the Dictionary object as a json string
	 *
	 * @return json string with the object
	 * @throws TextalyticsClientException
	 */
	public String toJsonString() throws TextalyticsClientException {
		ObjectMapper mapper = new ObjectMapper();
		StringWriter out = new StringWriter();

		// Object should be wrapped in document
		out.append("{\"dictionary\":");
		try {
			mapper.writeValue(out, this);
		} catch (Exception e) {
			throw new TextalyticsClientException(500, "Problem parsing 'dictionary' parameter.\n", e);
		}
		out.append("}");		
		
		return out.toString();
	}
	
	public Stats getStats() {
		return stats;
	}
	public void setStats(Stats stats) {
		this.stats = stats;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Language getLanguage() {
		return language;
	}
	public void setLanguage(Language language) {
		this.language = language;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Returns the value of the attribute that identifies univocally the dictionary
	 */
	public String getObjectIdentifier() {
		return name;
	}

	/**
	 * Concatenates the fields associated to a dictionary in user resources management 
	 * into a single string
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Dictionary: [");
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
