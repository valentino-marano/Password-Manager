package com.valentino.tap.password_manager.test.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.valentino.tap.password_manager.app.db.MongoDatabaseWrapper;

public abstract class AbstractMongoDatabaseWrapperTest {

	private MongoDatabaseWrapper mongoDatabase;

	public abstract MongoClient createMongoClient() throws UnknownHostException;

	private MongoTestHelper mongoTestHelper;

	@Before
	public void setUp() throws UnknownHostException {
		MongoClient mongoClient = createMongoClient();
		mongoTestHelper = new MongoTestHelper(mongoClient);
		mongoDatabase = new MongoDatabaseWrapper(mongoClient);
	}

	@Test
	public void testGetAllPasswordsWhenThereAreNoPasswords() {
		assertTrue(mongoDatabase.getAllPasswords().isEmpty());
	}

	@Test
	public void testGetAllPasswordsWhenThereIsOnePassword() {
		mongoTestHelper.addPassword("sito1", "user1", "password1");
		assertEquals(1, mongoDatabase.getAllPasswords().size());
	}

	@Test
	public void testGetAllPasswordsWhenThereAreThreePasswords() {
		mongoTestHelper.addPassword("sito1", "user1", "password1");
		mongoTestHelper.addPassword("sito2", "user2", "password2");
		mongoTestHelper.addPassword("sito3", "user3", "password3");
		assertEquals(3, mongoDatabase.getAllPasswords().size());
	}

	@Test
	public void testGetPasswordsByWebSiteWhenThereAreNoPasswords() {
		assertTrue(mongoDatabase.getPasswordsByWebSite("sito").isEmpty());
	}

	@Test
	public void testGetPasswordsByWebSiteWhenThereAreNoMatchingPasswords() {
		mongoTestHelper.addPassword("sito1", "user1", "password1");
		mongoTestHelper.addPassword("sito2", "user2", "password2");
		mongoTestHelper.addPassword("sito3", "user3", "password3");
		assertTrue(mongoDatabase.getPasswordsByWebSite("website").isEmpty());
	}

	@Test
	public void testGetPasswordsByWebSiteWhenThereIsOneWholeMatchingPasswords() {
		mongoTestHelper.addPassword("sito1", "user1", "password1");
		mongoTestHelper.addPassword("sito2", "user2", "password2");
		mongoTestHelper.addPassword("sito3", "user3", "password3");
		mongoTestHelper.addPassword("sito4", "user4", "password4");
		mongoTestHelper.addPassword("sito5", "user5", "password5");
		assertEquals(1, mongoDatabase.getPasswordsByWebSite("sito1").size());
	}

	@Test
	public void testGetPasswordsByWebSiteWhenThereIsOneLikeMatchingPasswords() {
		mongoTestHelper.addPassword("sito1", "user1", "password1");
		mongoTestHelper.addPassword("sito2", "user2", "password2");
		mongoTestHelper.addPassword("sitoweb3", "user3", "password3");
		mongoTestHelper.addPassword("sito4", "user4", "password4");
		mongoTestHelper.addPassword("sito5", "user5", "password5");
		assertEquals(1, mongoDatabase.getPasswordsByWebSite("itow").size());
	}

	@Test
	public void testGetPasswordsByWebSiteWhenThereAreThreeMatchingPasswords() {
		mongoTestHelper.addPassword("site1", "user1", "password1");
		mongoTestHelper.addPassword("site2", "user2", "password2");
		mongoTestHelper.addPassword("site3", "user3", "password3");
		mongoTestHelper.addPassword("website1", "user1", "password1");
		mongoTestHelper.addPassword("website2", "user2", "password2");
		mongoTestHelper.addPassword("site1web", "user1", "password1");
		assertEquals(3, mongoDatabase.getPasswordsByWebSite("site1").size());
	}

}