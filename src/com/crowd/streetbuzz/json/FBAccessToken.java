/**
 * 
 */
package com.crowd.streetbuzz.json;

/**
 * @author Atrijit
 * 
 */
public class FBAccessToken {
	private String access_token;

	private String social_user_id;

	private String network;
	
	private String email;
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getSocial_user_id() {
		return social_user_id;
	}

	public void setSocial_user_id(String social_user_id) {
		this.social_user_id = social_user_id;
	}

}
