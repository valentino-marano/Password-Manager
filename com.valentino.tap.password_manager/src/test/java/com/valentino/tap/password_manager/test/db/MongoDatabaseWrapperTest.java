package com.valentino.tap.password_manager.test.db;

import com.github.fakemongo.Fongo;
import com.mongodb.MongoClient;

public class MongoDatabaseWrapperTest extends AbstractMongoDatabaseWrapperTest {
	@Override
	public MongoClient createMongoClient() {
		Fongo fongo = new Fongo("mongo server 1");
		MongoClient mongoClient = fongo.getMongo();
		return mongoClient;
	}
}
