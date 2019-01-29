package com.valentino.tap.password_manager.app.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

	/********** MongoDatabaseWrapper() **********/
	/**
		@brief It's the constructor method for the class MongoDatabaseWrapper
		@param mongoClient to connect
		@param database database name to connect
		@param type of connection to be opened: PASSWORD for standard client connection to manage passwords, ADMIN for login connection
	 */
	public MongoDatabaseWrapper(MongoClient mongoClient, String database, int type) {
		db = mongoClient.getDB(database);
		Jongo jongo = new Jongo(db);
		if (type == PASSWORD)
			passwords = jongo.getCollection("password");
		if (type == ADMIN)
			users = jongo.getCollection("system.users");
	}

	/* (non-Javadoc)
	 * @see com.valentino.tap.password_manager.app.db.Database#getAllPasswords()
	 */
	@Override
	public List<Password> getAllPasswords() {
		Iterable<Password> iterable = passwords.find().as(Password.class);
		return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
	}
	
	/* (non-Javadoc)
	 * @see com.valentino.tap.password_manager.app.db.Database#getPasswordsByWebSite(java.lang.String)
	 */
	@Override
	public List<Password> getPasswordsByWebSite(String website) {
		Iterable<Password> iterable = passwords.find("{website: {$regex: #}}", website).as(Password.class);
		return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
	}

	/* (non-Javadoc)
	 * @see com.valentino.tap.password_manager.app.db.Database#save(com.valentino.tap.password_manager.app.Password)
	 */
	@Override
	public void save(Password password) {
		passwords.save(password);
	}

	/* (non-Javadoc)
	 * @see com.valentino.tap.password_manager.app.db.Database#existsPassword(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean existsPassword(String website, String username) {
		return (getPassword(website, username) != null);
	}
	
	/* (non-Javadoc)
	 * @see com.valentino.tap.password_manager.app.db.Database#getPassword(java.lang.String, java.lang.String)
	 */
	@Override
	public Password getPassword(String website, String username) {
		Iterable<Password> iterable = passwords.find("{website: #, username: #}", website, username).as(Password.class);
		List<Password> list = StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
		if (list.isEmpty())
			return null;
		return list.get(0);
	}	

	/* (non-Javadoc)
	 * @see com.valentino.tap.password_manager.app.db.Database#delete(com.valentino.tap.password_manager.app.Password)
	 */
	@Override
	public void delete(Password password) {
		passwords.remove(password.getKey());		
	}

	/* (non-Javadoc)
	 * @see com.valentino.tap.password_manager.app.db.Database#update(com.valentino.tap.password_manager.app.Password)
	 */
	@Override
	public void update(Password password) {
		passwords.update(password.getKey()).with(password);		
	}

	/* (non-Javadoc)
	 * @see com.valentino.tap.password_manager.app.db.Database#getSearchedPasswords(java.lang.String)
	 */
	@Override
	public List<Password> getSearchedPasswords(String pattern) {
		Iterable<Password> iterable = passwords.find("{$or:[{website: {$regex: #}}, {username: {$regex: #}}]}", pattern, pattern).as(Password.class);
		return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
	}

	/* (non-Javadoc)
	 * @see com.valentino.tap.password_manager.app.db.Database#getUsers()
	 */
	@Override
	public List<String> getUsers() {
		Iterable<DBUser> usersIterable = users.find().projection("{user: 1, _id: 0}").as(DBUser.class);
	    ArrayList<String> userArray = new ArrayList<>();
		for (DBUser user : usersIterable) {
			userArray.add(user.getUser());
		}
		return userArray;
	}

	/* (non-Javadoc)
	 * @see com.valentino.tap.password_manager.app.db.Database#login(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Database login(String username, String database, String password) {
		MongoCredential credential = MongoCredential.createCredential(username, database, password.toCharArray());
		MongoClient passwordMongoClient = new MongoClient(db.getMongo().getAddress(), Arrays.asList(credential));
		MongoDatabaseWrapper mongoDatabaseWrapper = new MongoDatabaseWrapper(passwordMongoClient, database, PASSWORD);
		mongoDatabaseWrapper.db.getStats();
		return mongoDatabaseWrapper;
	}

	/* (non-Javadoc)
	 * @see com.valentino.tap.password_manager.app.db.Database#register(java.lang.String, java.lang.String, java.lang.String)
	 */
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