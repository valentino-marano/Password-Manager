package com.valentino.tap.password_manager.test.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
		Password password = new Password("site1", "user1", "password1", calendar.getTime());
		((MongoTester) sut).save(password);
		assertTrue(sut.existsPassword("site1", "user1"));
		assertEquals("password1", sut.getPassword("site1", "user1").getPassw());
		assertEquals(calendar.getTime(), sut.getPassword("site1", "user1").getDateExpiration());
	}
	
	@Test
	public void testPasswordIsDeleted() {
		Password password1 = new Password("site1", "user1", "password1", calendar.getTime());
		Password password2 = new Password("site2", "user2", "password2", calendar.getTime());
		mongoTestHelper.addPassword(password1);
		mongoTestHelper.addPassword(password2);
		((MongoTester) sut).delete(password1);
		assertFalse(sut.existsPassword("site1", "user1"));
		assertTrue(sut.existsPassword("site2", "user2"));
	}
}