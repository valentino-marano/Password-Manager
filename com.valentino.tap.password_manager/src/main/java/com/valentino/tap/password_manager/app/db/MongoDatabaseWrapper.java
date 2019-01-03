package com.valentino.tap.password_manager.app.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.valentino.tap.password_manager.app.Password;

public class MongoDatabaseWrapper implements Database {

	private MongoCollection passwords;
	private MongoCollection users;
	private DB db;
	public static final int PASSWORD = 0;
	public static final int ADMIN = 1;

	public MongoDatabaseWrapper(MongoClient mongoClient, String database, int type) {
		db = mongoClient.getDB(database);
		Jongo jongo = new Jongo(db);
		if (type == PASSWORD)
			passwords = jongo.getCollection("password");
		if (type == ADMIN)
			users = jongo.getCollection("system.users");
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

	@Override
	public List<Password> getSearchedPasswords(String pattern) {
		Iterable<Password> iterable = passwords.find("{$or:[{website: #}, {username: #}]}", Pattern.compile(pattern), Pattern.compile(pattern)).as(Password.class);
		return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
	}

	@Override
	public List<String> getUsers() {
		Iterable<DBUser> usersIterable = users.find().projection("{user: 1, _id: 0}").as(DBUser.class);
	    ArrayList<String> userArray = new ArrayList<>();
		for (DBUser user : usersIterable) {
			userArray.add(user.getUser());
		}
		return userArray;
	}

	@Override
	public Database login(String username, String database, String password) {
		MongoCredential credential = MongoCredential.createCredential(username, database, password.toCharArray());
		MongoClient passwordMongoClient = new MongoClient(db.getMongo().getAddress(), Arrays.asList(credential));
		MongoDatabaseWrapper mongoDatabaseWrapper = new MongoDatabaseWrapper(passwordMongoClient, database, PASSWORD);
		mongoDatabaseWrapper.db.getStats();
		return mongoDatabaseWrapper;
	}

	@Override
	public Database register(String username, String database, String password) {
		String createUser = "db.createUser({" +
				"user: \"" + username + "\", " +
				"pwd: \"" + password + "\", " + 
				"roles: [{role: \"readWrite\", db: \"" + database + "\"}], " + 
				"passwordDigestor:\"server\"})";
				
		MongoClient passwordMongoClient = new MongoClient(db.getMongo().getAddress());
		DB passwordDb = passwordMongoClient.getDB(database);
		passwordDb.eval(createUser);
		return new MongoDatabaseWrapper(passwordMongoClient, database, PASSWORD);
	}
}