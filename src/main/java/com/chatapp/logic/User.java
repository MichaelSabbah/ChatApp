package com.chatapp.logic;

public class User {
	
	private String username;
	private String password;
	private int locationId;
	
	public User(String username, String password,int locationId) {
		this.username = username;
		this.password = password;
		this.locationId = locationId;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
}
