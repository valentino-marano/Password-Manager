package com.valentino.tap.password_manager.test.db;

import static org.junit.Assert.assertTrue;

import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.valentino.tap.password_manager.app.Password;

public abstract class AbstractMongoDatabaseWrapperTest extends AbstractTest {

	public abstract MongoClient createMongoClient() throws UnknownHostException;

	@Override
	@Before
	public void setUp() throws UnknownHostException {
		MongoClient mongoClient = createMongoClient();
		mongoTestHelper = new MongoTestHelper(mongoClient);
		sut = new MongoTester(mongoClient);
	}
	
	@Test
	public void testPasswordIsSaved() {
		Password password = new Password("sito1", "user1", "password1");
		((MongoTester) sut).save(password);
		assertTrue(sut.existsPassword(password));
	}

}