package com.crowd.streetbuzzalgo.faroosearch.vo;

import java.util.List;

public class FarooResultSet {
	private List<FarooResult> results;
	private String query;
	private int count;
	private int start;
	private String time;
	private int length;
	private List<String> suggestions;
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public List<FarooResult> getResults() {
		return results;
	}
	public void setResults(List<FarooResult> results) {
		this.results = results;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public List<String> getSuggestions() {
		return suggestions;
	}
	public void setSuggestions(List<String> suggestions) {
		this.suggestions = suggestions;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
