package com.valentino.tap.password_manager.test.db;

import java.util.List;

import com.valentino.tap.password_manager.app.Password;

public interface SUT {

	List<Password> getAllPasswords();

	List<Password> getPasswordsByWebSite(String website);

	boolean existsPassword(String website, String username);
	
	void updatePassword(Password password);

	List<Password> searchPasswords(String pattern);
	
	Password getPassword(String website, String username);
}
