/**
 * 
 */
package com.crowd.streetbuzzalgo.parser.vo;

/**
 * @author Atrijit
 *
 */

/*
 * <location>
                <text>in Delhi</text>
                <entities>
                    <entity>
                        <type>City</type>
                        <text>Delhi</text>
            <knowledgeGraph>
                <typeHierarchy>/places/cities/delhi</typeHierarchy>
            </knowledgeGraph>
                    </entity>
                </entities>
            </location>
 */
public class LocationEntityVO {
	private String etext;
	private String etype;
	private String text;
	private String place;
	public String getEtext() {
		return etext;
	}
	public void setEtext(String etext) {
		this.etext = etext;
	}
	public String getEtype() {
		return etype;
	}
	public void setEtype(String etype) {
		this.etype = etype;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
}
