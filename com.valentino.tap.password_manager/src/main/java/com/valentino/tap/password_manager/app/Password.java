package com.valentino.tap.password_manager.app;

import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

public class Password {
	
	private String website;
	private String username;
	private String password;
	
	@MongoId
	@MongoObjectId
    private String key;
	
	public Password(String website, String username, String password) {
		this.website = website;
		this.username = username;
		this.password = password;
	}
	
	public Password() {};

	public String getWebsite() {
		return this.website;
	}
/*
	public void setWebsite(String website) {
		this.website = website;
	}
*/
	public String getUsername() {
		return this.username;
	}
/*
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
*/
}
