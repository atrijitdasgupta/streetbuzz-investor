/**
 * 
 */
package com.crowd.streetbuzz.json;

/**
 * @author Atrijit
 *
 */
public class JsonSentiment {
private Long positivevoices;
private Long negativevoices;
private Long neutralvoices;

public Long getNegativevoices() {
	return negativevoices;
}
public void setNegativevoices(Long negativevoices) {
	this.negativevoices = negativevoices;
}
public Long getNeutralvoices() {
	return neutralvoices;
}
public void setNeutralvoices(Long neutralvoices) {
	this.neutralvoices = neutralvoices;
}
public Long getPositivevoices() {
	return positivevoices;
}
public void setPositivevoices(Long positivevoices) {
	this.positivevoices = positivevoices;
}

}
