package com.valentino.tap.password_manager.app;

import org.bson.types.ObjectId;
import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

public class Password {
	
	private String website;
	private String username;
	private String passw;
	
	@MongoId
	@MongoObjectId
    private String key;
	
	public Password(String website, String username, String passw) {
		this.website = website;
		this.username = username;
		this.passw = passw;
	}
	
	public Password() {}

	public String getWebsite() {
		return this.website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassw() {
		return this.passw;
	}

	public void setPassw(String passw) {
		this.passw = passw;
	}

	public ObjectId getKey() {
		return new ObjectId(key);
	}
}
