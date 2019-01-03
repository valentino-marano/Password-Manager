package com.valentino.tap.password_manager.app.db;

public class DBUser {
	private String user;
	
	public DBUser(String user) {
		this.user = user;
	}
	
	public DBUser() {}
	
	public String getUser() {
		return this.user;
	}
}