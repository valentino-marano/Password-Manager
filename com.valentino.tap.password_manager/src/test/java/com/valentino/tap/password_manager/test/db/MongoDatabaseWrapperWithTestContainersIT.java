package com.valentino.tap.password_manager.test.db;

import java.net.UnknownHostException;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.testcontainers.containers.GenericContainer;

import com.mongodb.MongoClient;
import com.valentino.tap.password_manager.app.db.MongoDatabaseWrapper;

public class MongoDatabaseWrapperWithTestContainersIT extends AbstractMongoDatabaseWrapperIT {

	@SuppressWarnings("rawtypes")
	@ClassRule
	public static GenericContainer mongo =	new GenericContainer("mongo:latest").withExposedPorts(27017);

	@BeforeClass
	public static void startUp() throws UnknownHostException {
		mongoClient = new MongoClient(mongo.getContainerIpAddress(), mongo.getMappedPort(27017));
		database = new MongoDatabaseWrapper(mongoClient, "admin", MongoDatabaseWrapper.ADMIN);
		database.register(USER_NAME, DATABASE, PASSWORD);
	}
}
