/**
 * 
 */
package com.crowd.streetbuzzalgo.parser.vo;

import java.util.List;

/**
 * @author Atrijit
 * 
 */
public class BasicDependencyVO {

	private String nsubj="";

	private String vmod="";

	private List nn;

	private String prep_in="";

	private String conj_and="";
	
	private String conj="";

	private String amod="";

	private String dobj="";

	private String rcmod="";
	
	private String root="";
	
	private String mark="";
	
	private String csubjpass="";
	
	private String advcl="";
	
	

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getRoot() {
		return root;
	}

	public String getAmod() {
		return amod;
	}

	public void setAmod(String amod) {
		this.amod = amod;
	}

	public String getConj_and() {
		return conj_and;
	}

	public void setConj_and(String conj_and) {
		this.conj_and = conj_and;
	}

	public String getDobj() {
		return dobj;
	}

	public void setDobj(String dobj) {
		this.dobj = dobj;
	}

	public List getNn() {
		return nn;
	}

	public void setNn(List nn) {
		this.nn = nn;
	}

	public String getNsubj() {
		return nsubj;
	}

	public void setNsubj(String nsubj) {
		this.nsubj = nsubj;
	}

	public String getPrep_in() {
		return prep_in;
	}

	public void setPrep_in(String prep_in) {
		this.prep_in = prep_in;
	}

	public String getRcmod() {
		return rcmod;
	}

	public void setRcmod(String rcmod) {
		this.rcmod = rcmod;
	}

	public String getVmod() {
		return vmod;
	}

	public void setVmod(String vmod) {
		this.vmod = vmod;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public String getCsubjpass() {
		return csubjpass;
	}

	public void setCsubjpass(String csubjpass) {
		this.csubjpass = csubjpass;
	}

	public String getAdvcl() {
		return advcl;
	}

	public void setAdvcl(String advcl) {
		this.advcl = advcl;
	}

	public String getConj() {
		return conj;
	}

	public void setConj(String conj) {
		this.conj = conj;
	}

	
}
