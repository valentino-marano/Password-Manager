package com.valentino.tap.password_manager.app.db;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.valentino.tap.password_manager.app.Password;

public class MongoDatabaseWrapper implements Database {

	private MongoCollection passwords;

	public MongoDatabaseWrapper(MongoClient mc) {
		DB db = mc.getDB("PasswordManager");
		Jongo jongo = new Jongo(db);
		passwords = jongo.getCollection("password");
	}

	public List<Password> getAllPasswords() {
		Iterable<Password> iterable = passwords.find().as(Password.class);
		return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
	}
	
	public List<Password> getPasswordsByWebSite(String website) {
		Iterable<Password> iterable = passwords.find("{website: #}", Pattern.compile(website)).as(Password.class);
		return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
	}

	@Override
	public void save(Password password) {
		passwords.save(password);
	}

	public boolean existsPassword(Password password) {
		return passwords.find("{website: #, username: #}", password.getWebsite(), password.getUsername()).as(Password.class).hasNext();
	}

	@Override
	public void delete(Password password) {
		passwords.remove(password.getKey());		
	}

	@Override
	public void update(Password password) {
		passwords.update(password.getKey()).with(password);		
	}
}