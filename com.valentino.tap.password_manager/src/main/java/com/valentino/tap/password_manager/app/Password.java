package com.valentino.tap.password_manager.app;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.bson.types.ObjectId;
import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"simpleDateFormat"})
public class Password {
	
	private String website;
	private String username;
	private String passw;
	private Date expiration;
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
	
	@MongoId
	@MongoObjectId
    private String key;
	
	public Password(String website, String username, String passw, Date expiration) {
		this.website = website;
		this.username = username;
		this.passw = passw;
		this.expiration = expiration;
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
	
	public String getExpiration() {
		return simpleDateFormat.format(this.expiration);
	}
	
	public Date getDateExpiration() {
		return this.expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	public ObjectId getKey() {
		return new ObjectId(key);
	}
	
	public SimpleDateFormat getSimpleDateFormat() {
		return this.simpleDateFormat;
	}
}
