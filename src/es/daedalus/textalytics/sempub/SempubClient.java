/**
 * APIClient.java
 *
 * @contact    http://www.textalytics.com (http://www.daedalus.es)
 * @copyright  Copyright (c) 2014, DAEDALUS S.A. All rights reserved.
 */
package es.daedalus.textalytics.sempub;

import java.io.IOException;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import es.daedalus.textalytics.sempub.domain.Category;
import es.daedalus.textalytics.sempub.domain.Concept;
import es.daedalus.textalytics.sempub.domain.Dictionary;
import es.daedalus.textalytics.sempub.domain.Document;
import es.daedalus.textalytics.sempub.domain.Entity;
import es.daedalus.textalytics.sempub.domain.Model;
import es.daedalus.textalytics.sempub.domain.Document.Language;
import es.daedalus.textalytics.sempub.manage.CategoryManager;
import es.daedalus.textalytics.sempub.manage.ConceptManager;
import es.daedalus.textalytics.sempub.manage.DictionaryManager;
import es.daedalus.textalytics.sempub.manage.EntityManager;
import es.daedalus.textalytics.sempub.manage.ModelManager;
import es.daedalus.textalytics.sempub.utils.Conf;

public class SempubClient {
	
	/**
	 * Parameters' names for all possible operations of the API
	 */
	public enum Params{
		key, 
		input, 
		output, 
		fields,
		model,
		filter_data,
		doc,
		dictionary,
		mode,
		doc_offset,
		group_errors,
		check_spacing,
		query,
		lang
	}
	
	/**
	 * Allowed formats for input and output (this client currently works with json)
	 */
	public enum Format {
		xml, json
	}

	/**
	 * Types of analysis provided by the Semantic Tagging service 
	 */
	public enum AnalysisField {
		categories, 
		entities,
		concepts,
		timeExpressions,
		moneyExpressions,
		uris,
		phoneExpressions,
		quotations
	}
	
	/**
	 * Possible values for filter_data param
	 */
	public enum FilterData {
		y, n
	}
	
	/**
	 * Reviewing mode
	 */
	public enum Mode {
		all, next
	}
	
	/**
	 * What to do with text issues
	 */
	public enum GroupErrors {
		ALL, GROUP_AND_IGNORE, GROUP_AND_WARN
	}
	
	/**
	 * Warn about spacing errors in a text
	 */
	public enum CheckSpacing {
		y, n
	}
	
	/**
	 * Resource types
	 */
	public enum Type {
		dictionary,
		model,
		entity,
		concept,
		category
	}
	
	private HttpClient httpclient;
	private ResponseHandler<Response> responseHandler;
	
	private String protocol;
	private String host;
	private String path;
	private String key;
	
	private String fields = null; // API default - produce all results 
    private String models = null; // API default - no personal classification model
    private FilterData filterData = FilterData.y; // API default - no linked data
    
    private String dictionary = null; // API default - no personal dictionary
    
    private Mode mode = Mode.all; // API default - check for all errors
	private int docOffset = 0; // API default - check from the beginning of the text
	private int groupErrors = GroupErrors.GROUP_AND_WARN.ordinal(); // API default - group errors and warn about them
	private CheckSpacing checkSpacing = CheckSpacing.n; // API default - does not check for spacing errors

	// Managers
    private DictionaryManager dictionaryManager;
    private EntityManager entityManager;
    private ConceptManager conceptManager;
    private ModelManager modelManager;
    private CategoryManager	categoryManager;
    
    final private Format input = Format.json; // This client currently supports JSON
    final private Format output = Format.json; // This client currently supports JSON 
    
    public SempubClient() {
    	Properties prop = new Properties();
    	
    	HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		
		try {
			prop.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
			this.protocol = prop.getProperty("textalytics.protocol");
			this.host = prop.getProperty("textalytics.host");
			this.path = prop.getProperty("textalytics.service.endpoint.manage");
			this.key = prop.getProperty("textalytics.sempub.key");
			
			KeyStore truststore = KeyStore.getInstance(KeyStore.getDefaultType());
			truststore.load(null, null);
			
			SSLSocketFactory socketFactory = new SempubSSLSocketFactory(truststore);
			
			Scheme sch = new Scheme(protocol, socketFactory, 443);
			this.httpclient = new DefaultHttpClient(params);

			this.httpclient.getConnectionManager().getSchemeRegistry().register(sch);
			
		} catch (Exception e) {
			this.httpclient = new DefaultHttpClient(params);
		}
		
		this.responseHandler = new SempubResponseHandler();
		
		String url = host + path;
		
		dictionaryManager = new DictionaryManager(key, input, output, url, httpclient, responseHandler, protocol);
    	entityManager = new EntityManager(key, input, output, url, httpclient, responseHandler, protocol);
    	conceptManager = new ConceptManager(key, input, output, url, httpclient, responseHandler, protocol);
    	modelManager = new ModelManager(key, input, output, url, httpclient, responseHandler, protocol);
    	categoryManager = new CategoryManager(key, input, output, url, httpclient, responseHandler, protocol);
    }
    
    /**
	 * Analyzes a document with the Semantic Tagging service
	 *  
	 * @param document 
	 * @return a result object if the process succeed and config is OK. 
	 * @throws TextalyticsClientException
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public Result analyze(Document document) throws TextalyticsClientException, ClientProtocolException, IOException {
		Properties prop = new Properties();
		prop.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
		this.path = prop.getProperty("textalytics.service.endpoint.semtag");
		
		Response response = null;
		
		HttpPost post = new HttpPost(protocol + "://" + host + path);
		Conf.setPostParams(post, document, key, input, output, fields, dictionary, models, filterData);
		// if status not OK throws exception
		response = httpclient.execute(post, responseHandler);
		
		return response.getResult();
	}
	
	/**
	 * Analyzes a string of text with the Semantic Tagging service
	 * 	  
	 * @param text
	 * @param lang
	 * @return a result object if the process succeed and config is OK.
	 * @throws TextalyticsClientException 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public Result analyze(String text, Language lang) throws TextalyticsClientException, ClientProtocolException, IOException {
		Document document;
		if(lang == null) document = new Document("0", text);
		else document = new Document("0", text, lang);
		return analyze(document);
	}
	
	/**
	 * Analyzes a document with the Proofreading Service
	 * 
	 * @param document
	 * @return a result object if the process succeed and config is OK.
	 * @throws TextalyticsClientException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public Result check(Document document) throws TextalyticsClientException, ClientProtocolException, IOException {
		Properties prop = new Properties();
		prop.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
		this.path = prop.getProperty("textalytics.service.endpoint.check");
		
		Response response = null;
		
		HttpPost post = new HttpPost(protocol + "://" + host + path);
		Conf.setPostParams(post, document, key, input, output, docOffset, groupErrors, mode, checkSpacing, dictionary);
		// if status not OK throws exception
		response = httpclient.execute(post, responseHandler);
		
		return response.getResult();
	}
	
	/**
	 * Analyzes a string of text with the Proofreading Service
	 * 
	 * @param text
	 * @param lang
	 * @return a result object if the process succeed and config is OK.
	 * @throws TextalyticsClientException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public Result check(String text, Language lang) throws TextalyticsClientException, ClientProtocolException, IOException {
		Document document;
		if(lang == null) document = new Document("0", text);
		else document = new Document("0", text, lang);
		return check(document);
	}
	
	// DICTIONARY OPERATIONS
    public List<Dictionary> getDictionaryList(String query, Language lang) throws TextalyticsClientException, ClientProtocolException, IOException {
    	return dictionaryManager.getList(query, lang);
    }
    
    public String createDictionary(Dictionary dict) throws TextalyticsClientException, ClientProtocolException, IOException {
    	return dictionaryManager.create(dict);
    }

    public Dictionary readDictionary(String dictName) throws TextalyticsClientException, ClientProtocolException, IOException {
    	return dictionaryManager.read(dictName);
    }
    
    public String updateDictionary(Dictionary dict) throws TextalyticsClientException, ClientProtocolException, IOException {
    	return dictionaryManager.update(dict);
    }
    
    public String deleteDictionary(String dictName) throws TextalyticsClientException, ClientProtocolException, IOException {
    	return dictionaryManager.delete(dictName);
    }
    
    public String deleteDictionary(Dictionary dict) throws TextalyticsClientException, ClientProtocolException, IOException {
    	return dictionaryManager.delete(dict.getName());
    }
    
    // ENTITY OPERATIONS
    public List<Entity> getEntityList(Dictionary dict, String query) throws TextalyticsClientException, ClientProtocolException, IOException {
    	return entityManager.getList(dict.getName(), query);
    }
    
    public String createEntity(Entity entity, Dictionary dict) throws TextalyticsClientException, ClientProtocolException, IOException {
    	return entityManager.create(entity, dict.getName());
    }

    public Entity readEntity(String id, Dictionary dict) throws TextalyticsClientException, ClientProtocolException, IOException {
    	return entityManager.read(id, dict.getName());
    }
    
    public String updateEntity(Entity entity, Dictionary dict) throws TextalyticsClientException, ClientProtocolException, IOException {
    	return entityManager.update(entity, dict.getName());
    }
    
    public String deleteEntity(String id, Dictionary dict) throws TextalyticsClientException, ClientProtocolException, IOException {
    	return entityManager.delete(id, dict.getName());
    }
    
    public String deleteEntity(Entity entity, Dictionary dict) throws TextalyticsClientException, ClientProtocolException, IOException {
    	return entityManager.delete(entity.getObjectIdentifier(), dict.getName());
    }
    
    // CONCEPT OPERATIONS
    public List<Concept> getConceptList(Dictionary dict, String query) throws TextalyticsClientException, ClientProtocolException, IOException {
    	return conceptManager.getList(dict.getName(), query);
    }
    
    public String createConcept(Concept concept, Dictionary dict) throws TextalyticsClientException, ClientProtocolException, IOException {
    	return conceptManager.create(concept, dict.getName());
    }

    public Concept readConcept(String id, Dictionary dict) throws TextalyticsClientException, ClientProtocolException, IOException {
    	return conceptManager.read(id, dict.getName());
    }
    
    public String updateConcept(Concept concept, Dictionary dict) throws TextalyticsClientException, ClientProtocolException, IOException {
    	return conceptManager.update(concept, dict.getName());
    }
    
    public String deleteConcept(String id, Dictionary dict) throws TextalyticsClientException, ClientProtocolException, IOException {
    	return conceptManager.delete(id, dict.getName());
    }
    
    public String deleteConcept(Concept concept, Dictionary dict) throws TextalyticsClientException, ClientProtocolException, IOException {
    	return conceptManager.delete(concept.getObjectIdentifier(), dict.getName());
    }
    
    // MODEL OPERATIONS
    public List<Model> getModelList(String query, Language lang) throws TextalyticsClientException, ClientProtocolException, IOException {
    	return modelManager.getList(query, lang);
    }
    
    public String createModel(Model model) throws TextalyticsClientException, ClientProtocolException, IOException {
    	return modelManager.create(model);
    }

    public Model readModel(String modelName) throws TextalyticsClientException, ClientProtocolException, IOException {
    	return modelManager.read(modelName);
    }
    
    public String updateModel(Model model) throws TextalyticsClientException, ClientProtocolException, IOException {
    	return modelManager.update(model);
    }
    
    public String deleteModel(String modelName) throws TextalyticsClientException, ClientProtocolException, IOException{
    	return modelManager.delete(modelName);
    }
    
    public String deleteModel(Model model) throws TextalyticsClientException, ClientProtocolException, IOException{
    	return modelManager.delete(model.getName());
    }
    
    // CATEGORY OPERATIONS
    public List<Category> getCategoryList(Model model, String query) throws TextalyticsClientException, ClientProtocolException, IOException {
    	return categoryManager.getList(model.getName(), query);
    }
    
    public String createCategory(Category category, Model model) throws TextalyticsClientException, ClientProtocolException, IOException {
    	return categoryManager.create(category, model.getName());
    }

    public Category readCategory(String id, Model model) throws TextalyticsClientException, ClientProtocolException, IOException {
    	return categoryManager.read(id, model.getName());
    }
    
    public String updateCategory(Category category, Model model) throws TextalyticsClientException, ClientProtocolException, IOException {
    	return categoryManager.update(category, model.getName());
    }
    
    public String deleteCategory(String id, Model model) throws TextalyticsClientException, ClientProtocolException, IOException {
    	return categoryManager.delete(id, model.getName());
    }
    
    public String deleteCategory(Category category, Model model) throws TextalyticsClientException, ClientProtocolException, IOException {
    	return categoryManager.delete(category.getObjectIdentifier(), model.getName());
    }
    
    /**
	 * @return a pipe separated lists of the fields that are included in the analsysis, Default is to use all. 
	 */
	public String getFields() {
		if (fields == null) return fieldsValue(new TreeSet<AnalysisField>(Arrays.asList(AnalysisField.values())));
 		return fields;
	}
	
	/**
	 * Select the analysis to perform 
	 * 
	 * @param fields a pipe separated list of fields 
	 */
	public void setFields(String fields) {
		this.fields = fields;
	}
	
	/**
	 * Select the analysis to perform
	 * 
	 * @param fields a set of fields 
	 */
	public void setFields(Set<AnalysisField> fields) {
		String s = fieldsValue(fields);
		setFields(s);
	}
	
	/**
	 * @return a dictionary that is included in the analsysis, Default is none. 
	 */
	public String getDictionary() {
		return dictionary;
	}
	
	/**
	 * Select the dictionary to use
	 * 
	 * @param dict the dictionary name 
	 */
	public void setDictionary(Dictionary dict) {
		this.dictionary = dict.getName();
	}
	
	/**
	 * @return a pipe separated lists of the models that are included in the analysis, Default is none. 
	 */
	public String getModel() {
		return models;
	}
	
	/**
	 * Select the classification to perform 
	 * 
	 * @param models a pipe separated list of fields 
	 */
	public void setModels(String models) {
		this.models = models;
	}
	
	/**
	 * Select the classification to perform
	 * 
	 * @param models a set of strings
	 */
	public void setModels(Set<String> models) {
		String s = modelsValue(models);
		setModels(s);
	}
	
	/**
	 * @return value of filter_data param
	 */
	public FilterData getFilterData() {
		return filterData;
	}
	
	/**
	 * Select to filter or not linked data
	 * 
	 * @param filterData value of filter_data param
	 */
	public void setFilterData(FilterData filterData) {
		this.filterData = filterData;
	}
	
	// Transform a set of fields into a value for the parameter fields separated by the pipe character
	private String fieldsValue(Set<AnalysisField> fields) {
		if (fields.isEmpty()) return null; 
		
		StringBuffer buffer = new StringBuffer();
		Iterator<AnalysisField> it = fields.iterator();
		buffer.append(it.next().toString());
		while (it.hasNext()) {
			buffer.append('|').append(it.next());
		}
		
		return buffer.toString();
	}
	
	// Transform a set of models into a value for the parameter model separated by the pipe character
	private String modelsValue(Set<String> models) {
		if (models.isEmpty()) return null; 
		
		StringBuffer buffer = new StringBuffer();
		Iterator<String> it = models.iterator();
		buffer.append(it.next().toString());
		while (it.hasNext()) {
			buffer.append('|').append(it.next());
		}
		
		return buffer.toString();
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public int getDocOffset() {
		return docOffset;
	}

	public void setDocOffset(int docOffset) {
		this.docOffset = docOffset;
	}

	public int getGroupErrors() {
		return groupErrors;
	}

	public void setGroupErrors(int groupErrors) {
		this.groupErrors = groupErrors;
	}

	public CheckSpacing getCheckSpacing() {
		return checkSpacing;
	}

	public void setCheckSpacing(CheckSpacing checkSpacing) {
		this.checkSpacing = checkSpacing;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if (httpclient != null)
			httpclient.getConnectionManager().shutdown();
	}
	
	
}
