package com.valentino.tap.password_manager.app;

import java.util.List;
import com.valentino.tap.password_manager.app.db.Database;

/**
	@brief Class to manage passwords saved in Database
	@author Valentino Marano 
 */
public class PasswordManager {

	// Database instance to interact with
	private Database database;

	/********** PasswordManager() **********/
	/**
	  @brief It's the constructor method for the class PasswordManager
	 */
	public PasswordManager(Database database) {
		this.database = database;
	}

	/********** getAllPasswords() **********/
	/**
		@brief Retrieves all passwords from database
		@return The list of password retrieved
	 */
	public List<Password> getAllPasswords() {
		return database.getAllPasswords();
	}

	/********** getPasswordsByWebSite() **********/
	/**
		@brief Retrieves all passwords for a specific website
		@param website used to search 
		@return The list of password retrieved
	 */
	public List<Password> getPasswordsByWebSite(String website) {
		return database.getPasswordsByWebSite(website);
	}

	/********** addPassword() **********/
	/**
		@brief Tries to add a password to the database. If a password with same website and user already exists, nothing happens.
		@param password to be added
		@return true if the password has been added
		@return false if another password with same website and user already exists
	 */
	public boolean addPassword(Password password) {
		if (existsPassword(password.getWebsite(), password.getUsername()))
			return false;

		database.save(password);
		return true;	
	}

	/********** existsPassword() **********/
	/**
		@brief Checks if there is a password for specific website and username 
		@param website used to search
		@param username used to search
		@return true if there is a password for website and username
		@return false if there isn't any password for website and username
	 */
	public boolean existsPassword(String website, String username) {
		return database.existsPassword(website, username);
	}

	/********** deletePassword() **********/
	/**
		@brief Tries to delete a password from the database. If does not exist any password for website and username, nothing happens.
		@param password to be deleted
		@return true if the password has been deleted
		@return false if no password for website and username has been found
	 */
	public boolean deletePassword(Password password) {
		if (!existsPassword(password.getWebsite(), password.getUsername()))
			return false;

		database.delete(password);
		return true;
	}

	/********** updatePassword() **********/
	/**
		@brief Updates a password in the database
		@param password to be updated
	 */
	public void updatePassword(Password password) {
		database.update(password);	
	}

	/********** getSearchedPasswords() **********/
	/**
		@brief Retrieves all passwords from the database having website or username matching param text
		@param text used to compare with website or username
		@return The list of passwords retrieved
	 */
	public List<Password> getSearchedPasswords(String text) {
		return database.getSearchedPasswords(text);
	}

	/********** getPassword() **********/
	/**
		@brief Retrieves exact password from the database with specific website and username
		@param website used to search
		@param username used to search
		@return null if there isn't any password in database for website and username
		@return Password found in database for website and username
	 */
	public Password getPassword(String website, String username) {
		return database.getPassword(website, username);
	}
}