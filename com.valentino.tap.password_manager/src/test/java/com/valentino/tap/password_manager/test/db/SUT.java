package com.valentino.tap.password_manager.test.db;

import java.util.List;

import com.valentino.tap.password_manager.app.Password;

public interface SUT {

	List<Password> getAllPasswords();

	List<Password> getPasswordsByWebSite(String website);

	boolean existsPassword(Password password);
	
	void updatePassword(Password password);
}
