/**
 * Result.java
 * 
 * @contact    http://www.textalytics.com (http://www.daedalus.es)
 * @copyright  Copyright (c) 2014, DAEDALUS S.A. All rights reserved.
 */

package es.daedalus.textalytics.sempub;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import es.daedalus.textalytics.sempub.domain.Category;
import es.daedalus.textalytics.sempub.domain.Concept;
import es.daedalus.textalytics.sempub.domain.Entity;
import es.daedalus.textalytics.sempub.domain.Issue;
import es.daedalus.textalytics.sempub.domain.MoneyExpression;
import es.daedalus.textalytics.sempub.domain.PhoneExpression;
import es.daedalus.textalytics.sempub.domain.Quotation;
import es.daedalus.textalytics.sempub.domain.TimeExpression;
import es.daedalus.textalytics.sempub.domain.Uri;

/**
 * Result class represents the result object within the response for the 
 * Semantic Tagging and Text Proofreading services.
 * 
 * @author Textalytics
 * @version 1.0 02/2014
 */
public class Result {

	private String id;
	
	@JsonProperty("category_list")
	private List<Category> categories;
	@JsonProperty("entity_list")
	private List<Entity> entities;
	@JsonProperty("concept_list")
	private List<Concept> concepts;
	@JsonProperty("time_expression_list")
	private List<TimeExpression> timeExpressions;
	@JsonProperty("money_expression_list")
	private List<MoneyExpression> moneyExpressions;
	@JsonProperty("uri_list")
	private List<Uri> uris;
	@JsonProperty("phone_expression_list")
	private List<PhoneExpression> phoneExpressions;
	@JsonProperty("quotation_list")
	private List<Quotation> quotations;
	private Map<String,Object> userAttributes = new HashMap<String, Object>();
	
	@JsonProperty("issue_list")
	private List<Issue> issues;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<Category> getCategories() {
		return categories;
	}
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	public List<Entity> getEntities() {
		return entities;
	}
	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}
	public List<Concept> getConcepts() {
		return concepts;
	}
	public void setConcepts(List<Concept> concepts) {
		this.concepts = concepts;
	}
	public List<TimeExpression> getTimeExpressions() {
		return timeExpressions;
	}
	public void setTimeExpressions(List<TimeExpression> timeExpressions) {
		this.timeExpressions = timeExpressions;
	}
	public List<MoneyExpression> getMoneyExpressions() {
		return moneyExpressions;
	}
	public void setMoneyExpressions(List<MoneyExpression> moneyExpressions) {
		this.moneyExpressions = moneyExpressions;
	}
	public List<Uri> getUris() {
		return uris;
	}
	public void setUris(List<Uri> uris) {
		this.uris = uris;
	}
	public List<PhoneExpression> getPhoneExpressions() {
		return phoneExpressions;
	}
	public void setPhoneExpressions(List<PhoneExpression> phoneExpressions) {
		this.phoneExpressions = phoneExpressions;
	}
	public List<Quotation> getQuotations() {
		return quotations;
	}
	public void setQuotations(List<Quotation> quotations) {
		this.quotations = quotations;
	}
	
	@JsonAnyGetter
	public Map<String, Object> any() {
		return userAttributes;
	}
	
	@JsonAnySetter
	public void set(String name, Object categories) {
		userAttributes.put(name, categories);
	}
	
	public List<Issue> getIssues() {
		return issues;
	}
	public void setIssues(List<Issue> issues) {
		this.issues = issues;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Result: \n\n");
		for(Field field : getClass().getDeclaredFields()) {
			try {
				if(field.get(this) != null)
					buffer.append("[ " + field.getName() + ": " + field.get(this) + "]\n");
			} catch (Exception e) {}
		}
		buffer.append("\n");
		return buffer.toString();
	}
	
	
	
}
