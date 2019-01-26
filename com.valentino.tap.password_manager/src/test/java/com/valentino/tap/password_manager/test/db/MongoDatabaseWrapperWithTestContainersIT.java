package com.valentino.tap.password_manager.test.db;

import static org.junit.Assert.assertEquals;

import java.net.UnknownHostException;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.testcontainers.containers.GenericContainer;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.valentino.tap.password_manager.app.db.Database;
import com.valentino.tap.password_manager.app.db.MongoDatabaseWrapper;

public class MongoDatabaseWrapperWithTestContainersIT extends AbstractMongoDatabaseWrapperTest {

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	private Database database;
	
	@SuppressWarnings("rawtypes")
	@ClassRule
	public static GenericContainer mongo =	new GenericContainer("mongo:latest").withExposedPorts(27017);

	@Override
	public MongoClient createMongoClient() throws UnknownHostException {
		MongoClient mongoClient = new MongoClient(mongo.getContainerIpAddress(), mongo.getMappedPort(27017));
		database = new MongoDatabaseWrapper(mongoClient, "admin", MongoDatabaseWrapper.ADMIN);
		return mongoClient;
	}
	
	@Test
	public void testLoginSuccess() {
		database.register("TestMongo", "TestMongoDB", "TestMongoPWD");
		database.login("TestMongo", "TestMongoDB", "TestMongoPWD");
	}
	
	@Test
	public void testLoginPasswordFail() {
		database.register("TestMongo2", "TestMongo2DB", "TestMongo2PWD");
		expectedEx.expect(MongoException.class);
		database.login("TestMongo2", "TestMongo2DB", "WrongPassword");
	}
	
	@Test
	public void testLoginUserFail() {
		database.register("TestMongo3", "TestMongo3DB", "TestMongo3PWD");
		expectedEx.expect(MongoException.class);
		database.login("WrongUser", "TestMongo3DB", "TestMongo3PWD");
	}
	
	@Test
	public void testRegisterSuccess() {
		int users = database.getUsers().size();
		database.register("TestMongo4", "TestMongo4DB", "TestMongo4PWD");
		assertEquals(users + 1, database.getUsers().size());
	}
	
	@Test
	public void testRegisterFail() {
		database.register("TestMongo5", "TestMongo5DB", "TestPWD5");
		expectedEx.expect(MongoException.class);
		database.register("TestMongo5", "TestMongo5DB", "TestPWD5");
	}
}
