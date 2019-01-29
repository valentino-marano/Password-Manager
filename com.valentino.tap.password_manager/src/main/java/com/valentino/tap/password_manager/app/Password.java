package com.valentino.tap.password_manager.app;

import java.util.Date;
import org.bson.types.ObjectId;
import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
	@brief Class that implements a password
	@author Valentino Marano 
 */
@JsonIgnoreProperties({"simpleDateFormat"})
public class Password {
	
	private String website;
	private String username;
	private String passw;
	private Date expiration;
	
	/**
	 * Field to manage Id in Mongo Database
	 */
	@MongoId
	@MongoObjectId
    private String key;
	
	/********** Password() **********/
	/**
		@brief It's the constructor method for the class Password
		@param website of the Password
		@param username of the Password
		@param passw password of the Password
		@param expiration date of the Password
	 */
	public Password(String website, String username, String passw, Date expiration) {
		this.website = website;
		this.username = username;
		this.passw = passw;
		this.expiration = expiration;
	}
	
	/********** Password() **********/
	/**
		@brief It's the constructor method for the class Password. This constructor is used for automatic unmarshall of the result of find method
	 */
	public Password() {}

	/********** getWebsite() **********/
	/**
		@brief Gives back the value of the field website
	 */
	public String getWebsite() {
		return this.website;
	}

	/********** setWebsite() **********/
	/**
		@brief Sets the value of the field website
		@param website to set
	 */
	public void setWebsite(String website) {
		this.website = website;
	}

	/********** getUsername() **********/
	/**
		@brief Gives back the value of the field username
	 */
	public String getUsername() {
		return this.username;
	}

	/********** setUsername() **********/
	/**
		@brief Sets the value of the field username
		@param username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/********** getPassw() **********/
	/**
		@brief Gives back the value of the field passw
	 */
	public String getPassw() {
		return this.passw;
	}

	/********** setPassw() **********/
	/**
		@brief Sets the value of the field passw
		@param passw to set
	 */
	public void setPassw(String passw) {
		this.passw = passw;
	}
	
	/********** getDateExpiration() **********/
	/**
		@brief Gives back the value of the field expiration
	 */
	public Date getDateExpiration() {
		return this.expiration;
	}

	/********** setExpiration() **********/
	/**
		@brief Sets the value of the field expiration
		@param expiration to set
	 */
	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	/********** getKey() **********/
	/**
		@brief Gives back the value of the field key
	 */
	public ObjectId getKey() {
		return new ObjectId(key);
	}
}
