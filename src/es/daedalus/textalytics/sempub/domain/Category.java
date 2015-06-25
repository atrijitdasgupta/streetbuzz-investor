/**
 * Category.java
 *
 * @contact    http://www.textalytics.com (http://www.daedalus.es)
 * @copyright  Copyright (c) 2014, DAEDALUS S.A. All rights reserved.
 */

package es.daedalus.textalytics.sempub.domain;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.daedalus.textalytics.sempub.TextalyticsClientException;
import es.daedalus.textalytics.sempub.utils.TermUtils;

/**
 * The class Category represents the categories that define a classification models. It models
 * both the object returned in the semantic tagging service and the object used in
 * the user resources management service (endpoints: /model_list/{name}/category_list
 * and /model_list/{name}/category_list/{id}).
 *
 * @author Textalytics  
 * @version 1.0 02/2014
 */

public class Category implements Resource {

  //attributes associated to the resource management
	private String code;
	private String label;
	private String positive;
	private String negative;
	private String relevant;
	private String irrelevant;
	private String text;
	
  //attributes associated to the tagging returned
	@JsonProperty("label_list")
	private List<String> labels;
	private int relevance;
	

	// Dummy constructor for Jackson
	public Category() {}

	/**
	 * Class constructor
	 *
	 * @param code field that identifies univocally a category in a model
	 * @param label description of the category
	 * @param positive string with the list of positive terms separated by '|'
	 * @param negative string with the list of negative terms separated by '|'
	 * @param relevant string with the list of positive terms separated by '|'
	 * @param irrelevant string with the list of negative terms separated by '|'
	 * @param text training text
	 */	
	public Category(String code, String label, String positive,
			String negative, String relevant, String irrelevant, String text) {
		this.code = code;
		this.label = label;
		this.positive = positive;
		this.negative = negative;
		this.relevant = relevant;
		this.irrelevant = irrelevant;
		this.text = text;
	}
	
	public Category(String code, String label) {
		this.code = code;
		this.label = label;
	}

  /**
   * Returns the Category object as a json string
   *
   * @return json string with the object
   * @throws TextalyticsClientException
   */
	public String toJsonString() throws TextalyticsClientException {
		ObjectMapper mapper = new ObjectMapper();
		StringWriter out = new StringWriter();

		// Object should be wrapped in document
		out.append("{\"category\":");
		try {
			mapper.writeValue(out, this);
		} catch (Exception e) {
			throw new TextalyticsClientException(500, "Problem parsing 'category' parameter.\n", e);
		} 
		out.append("}");		
		
		return out.toString();
	}


  // Get	and set methods for 'code' attribute
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
 
  // Get and set methods for 'label' attribute
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}

  // Get and set methods for 'positive' attribute
	public String getPositive() {
		return positive;
	}
	public void setPositive(String positive) {
		this.positive = positive;
	}
	
  // Get and set methods for 'negative' attribute
	public String getNegative() {
		return negative;
	}
	public void setNegative(String negative) {
		this.negative = negative;
	}
	
  // Get and set methods for 'relevant' attribute
	public String getRelevant() {
		return relevant;
	}
	public void setRelevant(String relevant) {
		this.relevant = relevant;
	}
	
  // Get and set methods for 'irrelevant' attribute
	public String getIrrelevant() {
		return irrelevant;
	}
	public void setIrrelevant(String irrelevant) {
		this.irrelevant = irrelevant;
	}
	 
  // Get and set methods for 'text' attribute
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

  // Get and set methods for 'labels' attribute	
	public List<String> getLabels() {
		return labels;
	}
	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

  // Get and set methods for 'relevance' attribute
	public int getRelevance() {
		return relevance;
	}
	public void setRelevance(int relevance) {
		this.relevance = relevance;
	}
	
	public void addPositiveTerm(String p) {
		positive = TermUtils.add(p, positive);
	}
	
	public void removePositiveTerm(String p) {
		positive = TermUtils.remove(p, positive);
	}
	
	public void addNegativeTerm(String n) {
		negative = TermUtils.add(n, negative);
	}
	
	public void removeNegativeTerm(String n) {
		negative = TermUtils.remove(n, negative);
	}
	
	public void addRelevantTerm(String r) {
		relevant = TermUtils.add(r, relevant);
	}
	
	public void removeRelevantTerm(String r) {
		relevant = TermUtils.remove(r, relevant);
	}
	
	public void addIrrelevantTerm(String i) {
		irrelevant = TermUtils.add(i, irrelevant);
	}
	
	public void removeIrrelevantTerm(String i) {
		irrelevant = TermUtils.remove(i, irrelevant);
	}
	
	public void addTrainingText(String t) {
		text += "\n" + t;
	}

  // Operations associated to operators used in the lists of terms
	public String multi(String s) {
		return "+" + s;
	}
	
	public String and(String s) {
		return "_" + s;
	}
	
	public String or(String s) {
		return "~" + s;
	}

  /**
   * Returns the value of the attribute that identifies univocally the category
   * in the model
   */
	public String getObjectIdentifier() {
		return code;
	}

  /**
   * Concatenates the fields associated to a category in user resources management 
   * into a single string
   */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Category: [");
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
