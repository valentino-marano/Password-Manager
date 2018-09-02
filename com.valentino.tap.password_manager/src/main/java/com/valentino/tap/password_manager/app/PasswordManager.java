package com.valentino.tap.password_manager.app;

import java.util.List;

import com.valentino.tap.password_manager.app.db.Database;

public class PasswordManager {
	private Database database;
	
	public PasswordManager(Database database) {
		this.database = database;
	}

	public List<Password> getAllPasswords() {
		return database.getAllPasswords();
	}
	
	public List<Password> getPasswordsByWebSite(String id) {
		return database.getPasswordsByWebSite(id);
	}
}