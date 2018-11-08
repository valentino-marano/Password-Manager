package com.valentino.tap.password_manager.test.db;

import java.util.List;

import com.mongodb.MongoClient;
import com.valentino.tap.password_manager.app.Password;
import com.valentino.tap.password_manager.app.db.MongoDatabaseWrapper;

public class MongoTester implements SUT {
	
	private MongoDatabaseWrapper mongoDatabase;

	public MongoTester(MongoClient mongoClient) {
		mongoDatabase = new MongoDatabaseWrapper(mongoClient);
	}

	@Override
	public List<Password> getAllPasswords() {
		return mongoDatabase.getAllPasswords();
	}

	@Override
	public List<Password> getPasswordsByWebSite(String website) {
		return mongoDatabase.getPasswordsByWebSite(website);
	}

	public void save(Password password) {
		mongoDatabase.save(password);
	}

	@Override
	public boolean existsPassword(Password password) {
		return mongoDatabase.existsPassword(password);
	}

	@Override
	public void updatePassword(Password password) {
		mongoDatabase.update(password);		
	}

	public void delete(Password password) {
		mongoDatabase.delete(password);		
	}

	@Override
	public List<Password> searchPasswords(String pattern) {
		return mongoDatabase.getSearchedPasswords(pattern);
	}
}
