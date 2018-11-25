package com.valentino.tap.password_manager.app;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.bson.types.ObjectId;
import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

public class Password {
	
	private String website;
	private String username;
	private String passw;
	private Date expiration;
	public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
	
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
	
	public static Date createDate(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"), Locale.ITALY);
		calendar.set(year, month, day);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
}
