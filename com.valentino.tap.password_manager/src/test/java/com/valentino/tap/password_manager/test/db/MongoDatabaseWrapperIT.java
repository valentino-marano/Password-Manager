package com.valentino.tap.password_manager.test.db;

import java.net.UnknownHostException;

import com.mongodb.MongoClient;

public class MongoDatabaseWrapperIT extends AbstractMongoDatabaseWrapperTest {

	@Override
	public MongoClient createMongoClient() throws UnknownHostException {
		MongoClient mongoClient = new MongoClient();
		return mongoClient;
	}
}
