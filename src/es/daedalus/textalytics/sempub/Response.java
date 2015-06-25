/**
 * Response.java
 * 
 * @contact    http://www.textalytics.com (http://www.daedalus.es)
 * @copyright  Copyright (c) 2014, DAEDALUS S.A. All rights reserved.
 */

package es.daedalus.textalytics.sempub;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import es.daedalus.textalytics.sempub.domain.Category;
import es.daedalus.textalytics.sempub.domain.Concept;
import es.daedalus.textalytics.sempub.domain.Dictionary;
import es.daedalus.textalytics.sempub.domain.Entity;
import es.daedalus.textalytics.sempub.domain.Model;

/**
 * Response class models the API response, with all its objects and fields. It models the objects
 * response of the three different services of the API: Semantic Tagging, Text Proofreading and 
 * User Resources Management.
 * 
 * @author Textalytics
 * @version 1.0 02/2014
 */
public class Response {

	// Fields of the response for Semantic Tagging and Text Proofreading services
	private Status status;
	private Result result;
	
	// Fields of the response for the User Resources Management service
	private String id;
	private Dictionary dictionary;
	@JsonProperty("dictionary_list")
	private List<Dictionary> dictionaries;
	private Model model;
	@JsonProperty("model_list")
	private List<Model> models;
	private Entity entity;
	@JsonProperty("entity_list")
	private List<Entity> entities;
	private Concept concept;
	@JsonProperty("concept_list")
	private List<Concept> concepts;
	private Category category;
	@JsonProperty("category_list")
	private List<Category> categories;
	
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Result getResult() {
		return result;
	}
	public void setResult(Result result) {
		this.result = result;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Dictionary getDictionary() {
		return dictionary;
	}
	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}
	public List<Dictionary> getDictionaries() {
		return dictionaries;
	}
	public void setDictionaries(List<Dictionary> dictionaries) {
		this.dictionaries = dictionaries;
	}
	public Model getModel() {
		return model;
	}
	public void setModel(Model model) {
		this.model = model;
	}
	public List<Model> getModels() {
		return models;
	}
	public void setModels(List<Model> models) {
		this.models = models;
	}
	public Entity getEntity() {
		return entity;
	}
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	public List<Entity> getEntities() {
		return entities;
	}
	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}
	public Concept getConcept() {
		return concept;
	}
	public void setConcept(Concept concept) {
		this.concept = concept;
	}
	public List<Concept> getConcepts() {
		return concepts;
	}
	public void setConcepts(List<Concept> concepts) {
		this.concepts = concepts;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public List<Category> getCategories() {
		return categories;
	}
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	
}
