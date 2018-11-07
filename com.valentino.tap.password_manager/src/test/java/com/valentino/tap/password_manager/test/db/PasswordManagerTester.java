package com.valentino.tap.password_manager.test.db;

import java.util.List;

import com.valentino.tap.password_manager.app.Password;
import com.valentino.tap.password_manager.app.PasswordManager;
import com.valentino.tap.password_manager.app.db.Database;

public class PasswordManagerTester implements SUT {
	
	private PasswordManager passwordManager;

	public PasswordManagerTester(Database database) {
		passwordManager = new PasswordManager(database);
	}

	@Override
	public List<Password> getAllPasswords() {
		return passwordManager.getAllPasswords();
	}

	@Override
	public List<Password> getPasswordsByWebSite(String website) {
		return passwordManager.getPasswordsByWebSite(website);
	}

	public boolean addPassword(Password password) {
		return passwordManager.addPassword(password);
	}

	@Override
	public boolean existsPassword(Password password) {
		return passwordManager.existsPassword(password);
	}

	@Override
	public void updatePassword(Password password) {
		passwordManager.updatePassword(password);		
	}
	
	public boolean deletePassword(Password password) {
		return passwordManager.deletePassword(password);
	}
}
