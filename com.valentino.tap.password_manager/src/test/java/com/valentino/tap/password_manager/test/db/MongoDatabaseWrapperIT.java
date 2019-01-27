package com.valentino.tap.password_manager.test.db;

import java.net.UnknownHostException;

import org.junit.BeforeClass;
import com.mongodb.MongoClient;
import com.valentino.tap.password_manager.app.db.MongoDatabaseWrapper;

public class MongoDatabaseWrapperIT extends AbstractMongoDatabaseWrapperIT {
	
	@BeforeClass
	public static void startUp() throws UnknownHostException {
		mongoClient = new MongoClient("localhost", 27017);
		database = new MongoDatabaseWrapper(mongoClient, "admin", MongoDatabaseWrapper.ADMIN);
		database.register(USER_NAME, DATABASE, PASSWORD);
	}
}
