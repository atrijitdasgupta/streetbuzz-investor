/**
 * Concept.java
 *
 * @contact    http://www.textalytics.com (http://www.daedalus.es)
 * @copyright  Copyright (c) 2014, DAEDALUS S.A. All rights reserved.
 */

package es.daedalus.textalytics.sempub.domain;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.daedalus.textalytics.sempub.TextalyticsClientException;
import es.daedalus.textalytics.sempub.domain.auxiliary.Geo;
import es.daedalus.textalytics.sempub.domain.auxiliary.LinkedData;
import es.daedalus.textalytics.sempub.domain.auxiliary.Standard;
import es.daedalus.textalytics.sempub.domain.auxiliary.Variant;
import es.daedalus.textalytics.sempub.utils.TermUtils;


/**
 * The class Concept represents the concepts included in a dictionary; it models
 * both the object returned in the semantic tagging service and the object used in
 * the user resources management service (endpoints: /dictionary_list/{name}/concept_list
 * and /dictionary_list/{name}/concept_list/{id}).
 *
 * @author Textalytics  
 * @version 1.0 02/2014
 */

public class Concept implements Resource {

  //attributes associated to the resource management and tagging
	private String id;
	private String form;
	@JsonProperty("alias_list")
	private List<String> alias;
	private String type;
	@JsonProperty("theme_list")
	private List<String> themes;

  //attributes associated to the tagging returned	
	private String dictionary;
	@JsonProperty("geo_list")
	private List<Geo> geoList;
	@JsonProperty("standard_list")
	private List<Standard> standards;
	@JsonProperty("variant_list")
	private List<Variant> variants;
	@JsonProperty("linked_data_list")
	private List<LinkedData> linkedData;
	private int relevance;

	
	// Dummy constructor for Jackson
	public Concept() {}

  /**
   * Class constructor
   *
   * @param id field that identifies univocally a concept in a dictionary
   * @param form textual representative
   * @param alias list of string with alias
   * @param type string with the type
   * @param themes list of possible themes
   */	
	public Concept(String id, String form, List<String> alias,
			String type, List<String> themes) {
		this.id = id;
		this.form = form;
		this.alias = alias;
		this.type = type;
		this.themes = themes;
	}
	
	public Concept(String id, String form) {
		this.id= id;
		this.form = form;
		alias = new ArrayList<String>();
		themes = new ArrayList<String>();
	}

  /**
   * Returns the Concept object as a json string
   *
   * @return json string with the object
   * @throws TextalyticsClientException
   */	
	public String toJsonString() throws TextalyticsClientException {
		ObjectMapper mapper = new ObjectMapper();
		StringWriter out = new StringWriter();

		// Object should be wrapped in document
		out.append("{\"concept\":");
		try {
			mapper.writeValue(out, this);
		} catch (Exception e) {
			throw new TextalyticsClientException(500, "Problem parsing 'concept' parameter.\n", e);
		}
		out.append("}");		
		
		return out.toString();
	}
	
	public void addAlias(String a) {
		alias.add(a);
	}
	
	public void removeAlias(String a) {
		TermUtils.searchAndRemove(a, alias);
	}
	
	public void addTheme(String t) {
		themes.add(t);
	}
	
	public void removeTheme(String t) {
		TermUtils.searchAndRemove(t, themes);
	}

  // Get and set methods for 'id' attribute
  /**
   * Returns the value of the attribute that identifies univocally the concept
   * in the dictionary
   */
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

  // Get and set methods for 'form' attribute
	public String getForm() {
		return form;
	}
	public void setForm(String form) {
		this.form = form;
	}

  // Get and set methods for 'alias' attribute
	public List<String> getAlias() {
		return alias;
	}
	public void setAlias(List<String> alias) {
		this.alias = alias;
	}

  // Get and set methods for 'type' attribute
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

  // Get and set methods for 'themes' attribute
	public List<String> getThemes() {
		return themes;
	}
	public void setThemes(List<String> themes) {
		this.themes = themes;
	}

  // Get and set methods for 'variants' attribute
	public List<Variant> getVariants() {
		return variants;
	}
	public void setVariants(List<Variant> variants) {
		this.variants = variants;
	}

  // Get and set methods for 'linkedData' attribute
	public List<LinkedData> getLinkedData() {
		return linkedData;
	}
	public void setLinkedData(List<LinkedData> linkedData) {
		this.linkedData = linkedData;
	}

  // Get and set methods for 'relevance' attribute
	public int getRelevance() {
		return relevance;
	}
	public void setRelevance(int relevance) {
		this.relevance = relevance;
	}

	public String getDictionary() {
		return dictionary;
	}

	public void setDictionary(String dictionary) {
		this.dictionary = dictionary;
	}

	public List<Geo> getGeoList() {
		return geoList;
	}

	public void setGeoList(List<Geo> geoList) {
		this.geoList = geoList;
	}

	public List<Standard> getStandards() {
		return standards;
	}

	public void setStandards(List<Standard> standards) {
		this.standards = standards;
	}

	
	public String getObjectIdentifier() {
		return id;
	}
	
/**
   * Concatenates the fields associated to a concept in user resources management 
   * into a single string
   */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Concept: [");
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
