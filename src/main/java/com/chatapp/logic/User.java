package com.chatapp.logic;

import com.amazonaws.regions.Regions;

public class User {
	
	private String username;
	private String password;
	private Regions region;
	
	public User(String username, String password,Regions region) {
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

	public Regions getRegion() {
		return region;
	}

	public void setRegion(Regions region) {
		this.region = region;
	}
}
