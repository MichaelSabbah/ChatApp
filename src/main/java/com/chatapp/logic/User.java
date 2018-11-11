package com.chatapp.logic;

import com.amazonaws.services.s3.model.Region;

public class User {
	
	private String username;
	private String password;
	private Region region;
	
	public User(String username, String password,Region region) {
		this.username = username;
		this.password = password;
		this.region = region;
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

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}
}
