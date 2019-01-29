package com.valentino.tap.password_manager.app.db;

import java.util.List;

import com.valentino.tap.password_manager.app.Password;

/**
	@brief Interface for database interaction
	@author Valentino Marano 
 */
public interface Database {

	/********** getAllPasswords() **********/
	/**
		@brief Retrieves all password from the database
		@return List of retrieved passwords
	 */
	public List<Password> getAllPasswords();

	/********** getPasswordsByWebSite() **********/
	/**
		@brief Retrieves all password for a specific website
		@param website used to search
		@return List of retrieved passwords
	 */
	public List<Password> getPasswordsByWebSite(String website);
	
	/********** save() **********/
	/**
		@brief Saves a password on database
		@param password to be saved
	 */
	public void save(Password password);

	/********** existsPassword() **********/
	/**
		@brief Checks if exists a password on database for specific website and username
		@param website used to search
		@param username used to search
		@return true if password is found
		@return false if password is not found
	 */
	public boolean existsPassword(String website, String username);

	/********** delete() **********/
	/**
		@brief Deletes a password from database
		@param password to be deleted
	 */
	public void delete(Password password);

	/********** update() **********/
	/**
		@brief Updates a password in database
		@param password to be updated
	 */
	public void update(Password password);

	/********** getSearchedPasswords() **********/
	/**
		@brief Retrieves all passwords from the database having website or username matching param text
		@param text used to compare with website or username
		@return The list of passwords retrieved
	 */
	public List<Password> getSearchedPasswords(String text);
	
	/********** getUsers() **********/
	/**
		@brief Retrieves all users registered on database
		@return the list of users found
	 */
	public List<String> getUsers();

	/********** login() **********/
	/**
		@brief Tries to login
		@param username used to login
		@param database to connect
		@param password used to login
		@return database instance after successfull connection
	 */
	public Database login(String username, String database, String password);

	/********** register() **********/
	/**
		@brief Tries to register a new user
		@param username to be registered
		@param database to be created for new user
		@param password of the new user
		@return database instance after successfull registration
	 */
	public Database register(String username, String database, String password);

	/********** getPassword() **********/
	/**
		@brief Finds password for specific website and username
		@param website used to search
		@param username used to search
		@return Password found for website and username
		@return null if not password is found for website and username
	 */
	public Password getPassword(String website, String username);
}
