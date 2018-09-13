package com.valentino.tap.password_manager.app;

import java.net.UnknownHostException;
import java.util.List;

import org.apache.log4j.Logger;

import com.mongodb.MongoClient;
import com.valentino.tap.password_manager.app.db.Database;
import com.valentino.tap.password_manager.app.db.MongoDatabaseWrapper;

public class Application {
	static final Logger LOGGER = Logger.getLogger(Application.class);
	
	public static void main(String[] args) throws UnknownHostException {
		String mongoHost = "localhost";
		if (args.length > 0)
			mongoHost = args[0];
		Database database = new MongoDatabaseWrapper(new MongoClient(mongoHost));
		PasswordManager passwordManager = new PasswordManager(database);
		LOGGER.info("Adding password...");
		passwordManager.addPassword(new Password("sito1", "user1", "password1"));
		LOGGER.info("Adding password...");
		passwordManager.addPassword(new Password("sito2", "user2", "password2"));
		
		LOGGER.info("List of all passwords:");
		List<Password> passwords = passwordManager.getAllPasswords();
		passwords.stream().forEach(password -> 
			LOGGER.info("Password: " + password.getWebsite() + " - " + 
				password.getUsername() + " - " + password.getPassword()));
		LOGGER.info("Terminates.");
	}		
}
