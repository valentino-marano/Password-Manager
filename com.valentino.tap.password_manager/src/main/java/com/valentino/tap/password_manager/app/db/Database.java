package com.valentino.tap.password_manager.app.db;

import java.util.List;

import com.valentino.tap.password_manager.app.Password;

public interface Database {

	public List<Password> getAllPasswords();

	public List<Password> getPasswordsByWebSite(String website);
	
	public void save(Password password);

	public boolean existsPassword(String website, String username);

	public void delete(Password password);

	public void update(Password password);

	public List<Password> getSearchedPasswords(String text);
	
	public List<String> getUsers();

	public Database login(String username, String database, String password);

	public Database register(String username, String database, String password);

	public Password getPassword(String website, String username);
}
