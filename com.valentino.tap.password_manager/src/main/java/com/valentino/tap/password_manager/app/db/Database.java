package com.valentino.tap.password_manager.app.db;

import java.util.List;

import com.valentino.tap.password_manager.app.Password;

public interface Database {

	public List<Password> getAllPasswords();

	public List<Password> getPasswordsByWebSite(String website);
	
	public void save(Password password);

	public boolean existsPassword(Password password);

	public void delete(Password password);

	public void update(Password password);
}
