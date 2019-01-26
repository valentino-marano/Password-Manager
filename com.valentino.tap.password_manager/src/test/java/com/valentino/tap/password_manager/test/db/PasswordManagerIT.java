package com.valentino.tap.password_manager.test.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.github.fakemongo.Fongo;
import com.mongodb.MongoClient;
import com.valentino.tap.password_manager.app.Password;
import com.valentino.tap.password_manager.app.db.Database;
import com.valentino.tap.password_manager.app.db.MongoDatabaseWrapper;
import com.valentino.tap.password_manager.test.db.AbstractTest;

public class PasswordManagerIT extends AbstractTest {
	
	Database database;

	@Override
	@Before
	public void setUp() throws Exception {
		Fongo fongo = new Fongo("mongo server 1");
		MongoClient mongoClient = fongo.getMongo();
		mongoTestHelper = new MongoTestHelper(mongoClient);
		database = new MongoDatabaseWrapper(mongoClient, "test", MongoDatabaseWrapper.PASSWORD);
		sut = new PasswordManagerTester(database);
	}
	
	@Test
	public void testSavePasswordWhenDoesNotExist() {
		Password password = new Password("sito1", "user1", "password1", calendar.getTime());
		assertTrue(((PasswordManagerTester) sut).addPassword(password));
		assertTrue(sut.existsPassword("sito1", "user1"));
		assertEquals("password1", sut.getPassword("sito1", "user1").getPassw());
		assertEquals(calendar.getTime(), sut.getPassword("sito1", "user1").getDateExpiration());
	}
	
	@Test
	public void testSavePasswordWhenExists() {
		mongoTestHelper.addPassword(new Password("site1", "user1", "password1", calendar.getTime()));
		Password password = new Password("site1", "user1", "password2", calendar.getTime());
		assertFalse(((PasswordManagerTester) sut).addPassword(password));
	}
	
	@Test
	public void testDeletePasswordWhenExists() {
		Password password = new Password("site1", "user1", "password1", calendar.getTime());
		mongoTestHelper.addPassword(password);
		assertTrue(sut.existsPassword("site1", "user1"));
		assertTrue(((PasswordManagerTester) sut).deletePassword(password));
		assertFalse(sut.existsPassword("site1", "user1"));
	}
	
	@Test
	public void testDeletePasswordWhenNotExists() {
		Password password = new Password("site1", "user1", "password1", calendar.getTime());
		assertFalse(((PasswordManagerTester) sut).deletePassword(password));
	}
}