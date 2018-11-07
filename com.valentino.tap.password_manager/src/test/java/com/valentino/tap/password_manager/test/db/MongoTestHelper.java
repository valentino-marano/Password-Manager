package com.valentino.tap.password_manager.test.db;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.valentino.tap.password_manager.app.Password;

public class MongoTestHelper {

	private MongoCollection passwords;
	
	public MongoTestHelper(MongoClient mongoClient) {
		DB db = mongoClient.getDB("PasswordManager");		
		Jongo jongo = new Jongo(db);
		jongo.getCollection("password").drop();
		passwords = jongo.getCollection("password");
	}

	public void addPassword(Password password) {
		passwords.insert(password);		
	}
}
