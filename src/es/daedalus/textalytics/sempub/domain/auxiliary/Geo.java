/**
 * Geo.java
 *
 * @contact    http://www.textalytics.com (http://www.daedalus.es)
 * @copyright  Copyright (c) 2014, DAEDALUS S.A. All rights reserved.
 */
package es.daedalus.textalytics.sempub.domain.auxiliary;

import java.lang.reflect.Field;

/**
 * Geo class provides geographical information about an entity or concept in the 
 * Semantic Tagging service response.
 * 
 * @author Textalytics
 * @version 1.0 02/2014
 */
public class Geo {

	private String continent;
	private String country;
	private String adm1;
	private String adm2;
	private String adm3;
	private String city;
	private String district;
	
	public String getContinent() {
		return continent;
	}
	public void setContinent(String continent) {
		this.continent = continent;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getAdm1() {
		return adm1;
	}
	public void setAdm1(String adm1) {
		this.adm1 = adm1;
	}
	public String getAdm2() {
		return adm2;
	}
	public void setAdm2(String adm2) {
		this.adm2 = adm2;
	}
	public String getAdm3() {
		return adm3;
	}
	public void setAdm3(String adm3) {
		this.adm3 = adm3;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
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
