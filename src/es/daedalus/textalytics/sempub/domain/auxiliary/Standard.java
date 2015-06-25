/**
 * Standard.java
 *
 * @contact    http://www.textalytics.com (http://www.daedalus.es)
 * @copyright  Copyright (c) 2014, DAEDALUS S.A. All rights reserved.
 */
package es.daedalus.textalytics.sempub.domain.auxiliary;

import java.lang.reflect.Field;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Standard class provides information about relevant international standards related to an entity 
 * or concept in the Semantic Tagging service response.
 * 
 * @author Textalytics
 * @version 1.0 02/2014
 */
public class Standard {

	@JsonProperty("BCBA")
	private String bcba;
	@JsonProperty("BCS")
	private String bcs;
	@JsonProperty("BVL")
	private String bvl;
	@JsonProperty("BMAD")
	private String BMAD;
	@JsonProperty("Euronext")
	private String euronext;
	@JsonProperty("ISO3166-1-a2")
	private String iso3166_1_a2;
	@JsonProperty("ISO3166-1-a3")
	private String iso3166_1_a3;
	@JsonProperty("ISO4217")
	private String iso4217;
	@JsonProperty("ISO639-1")
	private String iso639_1;
	@JsonProperty("ISO639-2")
	private String iso639_2;
	@JsonProperty("ISO639-3")
	private String iso639_3;
	@JsonProperty("ISO639-5")
	private String iso639_5;
	@JsonProperty("ISO8601")
	private String iso8601;
	@JsonProperty("LSE")
	private String lse;
	@JsonProperty("LuxSE")
	private String luxSe;
	@JsonProperty("NASDAQ")
	private String nasdaq;
	@JsonProperty("NYSE")
	private String nyse;
	
	public String getBcba() {
		return bcba;
	}
	public void setBcba(String bcba) {
		this.bcba = bcba;
	}
	public String getBcs() {
		return bcs;
	}
	public void setBcs(String bcs) {
		this.bcs = bcs;
	}
	public String getBvl() {
		return bvl;
	}
	public void setBvl(String bvl) {
		this.bvl = bvl;
	}
	public String getBMAD() {
		return BMAD;
	}
	public void setBMAD(String bMAD) {
		BMAD = bMAD;
	}
	public String getEuronext() {
		return euronext;
	}
	public void setEuronext(String euronext) {
		this.euronext = euronext;
	}
	public String getIso3166_1_a2() {
		return iso3166_1_a2;
	}
	public void setIso3166_1_a2(String iso3166_1_a2) {
		this.iso3166_1_a2 = iso3166_1_a2;
	}
	public String getIso3166_1_a3() {
		return iso3166_1_a3;
	}
	public void setIso3166_1_a3(String iso3166_1_a3) {
		this.iso3166_1_a3 = iso3166_1_a3;
	}
	public String getIso4217() {
		return iso4217;
	}
	public void setIso4217(String iso4217) {
		this.iso4217 = iso4217;
	}
	public String getIso639_1() {
		return iso639_1;
	}
	public void setIso639_1(String iso639_1) {
		this.iso639_1 = iso639_1;
	}
	public String getIso639_2() {
		return iso639_2;
	}
	public void setIso639_2(String iso639_2) {
		this.iso639_2 = iso639_2;
	}
	public String getIso639_3() {
		return iso639_3;
	}
	public void setIso639_3(String iso639_3) {
		this.iso639_3 = iso639_3;
	}
	public String getIso639_5() {
		return iso639_5;
	}
	public void setIso639_5(String iso639_5) {
		this.iso639_5 = iso639_5;
	}
	public String getIso8601() {
		return iso8601;
	}
	public void setIso8601(String iso8601) {
		this.iso8601 = iso8601;
	}
	public String getLse() {
		return lse;
	}
	public void setLse(String lse) {
		this.lse = lse;
	}
	public String getLuxSe() {
		return luxSe;
	}
	public void setLuxSe(String luxSe) {
		this.luxSe = luxSe;
	}
	public String getNasdaq() {
		return nasdaq;
	}
	public void setNasdaq(String nasdaq) {
		this.nasdaq = nasdaq;
	}
	public String getNyse() {
		return nyse;
	}
	public void setNyse(String nyse) {
		this.nyse = nyse;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		for(Field field : getClass().getDeclaredFields()) {
			try {
				if(field.get(this) != null)
					buffer.append(field.getName() + "=" + field.get(this) + ", ");
			} catch (Exception e) {}
		}
		return buffer.toString();
	}
}
