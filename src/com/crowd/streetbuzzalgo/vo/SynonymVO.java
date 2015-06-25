/**
 * 
 */
package com.crowd.streetbuzzalgo.vo;

import java.io.Serializable;

/**
 * @author Atrijit
 *
 */
public class SynonymVO implements Serializable{
private String term;
private String canonical;
private String oskill;
public String getCanonical() {
	return canonical;
}
public void setCanonical(String canonical) {
	this.canonical = canonical;
}
public String getOskill() {
	return oskill;
}
public void setOskill(String oskill) {
	this.oskill = oskill;
}
public String getTerm() {
	return term;
}
public void setTerm(String term) {
	this.term = term;
}




}
