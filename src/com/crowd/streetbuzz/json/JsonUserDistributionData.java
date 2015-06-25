/**
 * 
 */
package com.crowd.streetbuzz.json;

/**
 * @author Atrijit
 * 
 */
public class JsonUserDistributionData {
	private Long id;

	private String avatar;

	private String name;

	private String distance;
	
	private String location;

	private String usersocialnetwork;

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsersocialnetwork() {
		return usersocialnetwork;
	}

	public void setUsersocialnetwork(String usersocialnetwork) {
		this.usersocialnetwork = usersocialnetwork;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
