package com.valentino.tap.password_manager.test.db;

import static org.junit.Assert.assertEquals;

import java.net.UnknownHostException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.valentino.tap.password_manager.app.db.Database;

public abstract class AbstractMongoDatabaseWrapperIT extends AbstractMongoDatabaseWrapperTest {
	
	protected static final String PASSWORD = "TestMongoPWD";
	protected static final String DATABASE = "TestMongoDB";
	protected static final String USER_NAME = "TestMongo";
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	protected static Database database;
	protected static MongoClient mongoClient;
	
	@Override
	public MongoClient createMongoClient() throws UnknownHostException {
		return mongoClient;
	}
	
	@Test
	public void testLoginSuccess() {
		database.login(USER_NAME, DATABASE, PASSWORD);
	}
	
	@Test
	public void testLoginPasswordFail() {
		expectedEx.expect(MongoException.class);
		database.login(USER_NAME, DATABASE, "WrongPassword");
	}
	
	@Test
	public void testLoginUserFail() {
		expectedEx.expect(MongoException.class);
		database.login("WrongUser", DATABASE, PASSWORD);
	}
	
	@Test
	public void testRegisterSuccess() {
		int users = database.getUsers().size();
		database.register(USER_NAME + "1", DATABASE + "1", PASSWORD + "1");
		assertEquals(users + 1, database.getUsers().size());
	}
	
	@Test
	public void testRegisterFail() {
		expectedEx.expect(MongoException.class);
		database.register(USER_NAME, DATABASE, PASSWORD);
	}
}
