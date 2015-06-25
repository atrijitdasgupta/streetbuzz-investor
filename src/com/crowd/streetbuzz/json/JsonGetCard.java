/**
 * 
 */
package com.crowd.streetbuzz.json;

/**
 * @author Atrijit
 *
 */
public class JsonGetCard {
private String cardid;
private String uniqueid;
/*
 * "S" - search card.
 * "C" - conversation card.
 * "H" - all types for home page.
 */
private String type;

/*
 * if "N" it means voices are not ready for the card.
 * By default it is set to "Y" because we would not send notification without voices being ready
 */
private String voices;

public String getVoices() {
	return voices;
}
public void setVoices(String voices) {
	this.voices = voices;
}
public String getCardid() {
	return cardid;
}
public void setCardid(String cardid) {
	this.cardid = cardid;
}
public String getUniqueid() {
	return uniqueid;
}
public void setUniqueid(String uniqueid) {
	this.uniqueid = uniqueid;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}


}
