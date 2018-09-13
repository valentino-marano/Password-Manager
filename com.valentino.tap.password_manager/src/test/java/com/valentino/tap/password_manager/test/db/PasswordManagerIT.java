package com.valentino.tap.password_manager.test.db;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import com.github.fakemongo.Fongo;
import com.mongodb.MongoClient;
import com.valentino.tap.password_manager.app.Password;
import com.valentino.tap.password_manager.app.db.Database;
import com.valentino.tap.password_manager.app.db.MongoDatabaseWrapper;

public class PasswordManagerIT extends AbstractTest {
	
	Database database;

	@Override
	@Before
	public void setUp() throws Exception {
		Fongo fongo = new Fongo("mongo server 1");
		MongoClient mongoClient = fongo.getMongo();
		mongoTestHelper = new MongoTestHelper(mongoClient);
		database = new MongoDatabaseWrapper(mongoClient);
		sut = new PasswordManagerTester(database);
	}
	
	@Test
	public void testSavePasswordWhenDoesNotExist() {
		Password password = new Password("sito1", "user1", "password1");
		assertTrue(((PasswordManagerTester) sut).addPassword(password));
	}
	
	@Test
	public void testSavePasswordWhenExists() {
		mongoTestHelper.addPassword("site1", "user1", "password1");
		Password password = new Password("site1", "user1", "password1");
		assertFalse(((PasswordManagerTester) sut).addPassword(password));
	}
}