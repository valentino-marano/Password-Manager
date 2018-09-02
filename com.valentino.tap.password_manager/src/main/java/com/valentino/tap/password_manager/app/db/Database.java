package com.valentino.tap.password_manager.app.db;

import java.util.List;

import com.valentino.tap.password_manager.app.Password;

public interface Database {

	List<Password> getAllPasswords();

	List<Password> getPasswordsByWebSite(String website);

}
