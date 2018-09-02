package com.valentino.tap.password_manager.test.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.github.fakemongo.Fongo;
import com.mongodb.MongoClient;
import com.valentino.tap.password_manager.app.PasswordManager;
import com.valentino.tap.password_manager.app.db.Database;
import com.valentino.tap.password_manager.app.db.MongoDatabaseWrapper;

public class PasswordManagerIT {

	// SUT
	private PasswordManager passwordManager;
	
	private MongoTestHelper mongoTestHelper;

	@Before
	public void setUp() throws Exception {
		Fongo fongo = new Fongo("mongo server 1");
		MongoClient mongoClient = fongo.getMongo();
		mongoTestHelper = new MongoTestHelper(mongoClient);
		Database database = new MongoDatabaseWrapper(mongoClient);
		passwordManager = new PasswordManager(database);
	}
	
	@Test
	public void testGetAllPasswordsWhenThereAreNoPasswords() {
		assertTrue(passwordManager.getAllPasswords().isEmpty());
	}
	
	@Test
	public void testGetAllPasswordsWhenThereIsOnePassword() {
		mongoTestHelper.addPassword("sito1", "user1", "password1");
		assertEquals(1, passwordManager.getAllPasswords().size());
	}
	
	@Test
	public void testGetAllPasswordsWhenThereAreThreePasswords() {
		mongoTestHelper.addPassword("sito1", "user1", "password1");
		mongoTestHelper.addPassword("sito2", "user2", "password2");
		mongoTestHelper.addPassword("sito3", "user3", "password3");
		assertEquals(3, passwordManager.getAllPasswords().size());
	}
	
	@Test
	public void testGetPasswordsByWebSiteWhenThereAreNoPasswords() {
		assertTrue(passwordManager.getPasswordsByWebSite("sito").isEmpty());
	}
	
	@Test
	public void testGetPasswordsByWebSiteWhenThereAreNoMatchingPasswords() {
		mongoTestHelper.addPassword("sito1", "user1", "password1");
		mongoTestHelper.addPassword("sito2", "user2", "password2");
		mongoTestHelper.addPassword("sito3", "user3", "password3");
		assertTrue(passwordManager.getPasswordsByWebSite("website").isEmpty());
	}
	
	@Test
	public void testGetPasswordsByWebSiteWhenThereIsOneWholeMatchingPasswords() {
		mongoTestHelper.addPassword("sito1", "user1", "password1");
		mongoTestHelper.addPassword("sito2", "user2", "password2");
		mongoTestHelper.addPassword("sito3", "user3", "password3");
		mongoTestHelper.addPassword("sito4", "user4", "password4");
		mongoTestHelper.addPassword("sito5", "user5", "password5");
		assertEquals(1, passwordManager.getPasswordsByWebSite("sito1").size());
	}

	@Test
	public void testGetPasswordsByWebSiteWhenThereIsOneLikeMatchingPasswords() {
		mongoTestHelper.addPassword("sito1", "user1", "password1");
		mongoTestHelper.addPassword("sito2", "user2", "password2");
		mongoTestHelper.addPassword("sitoweb3", "user3", "password3");
		mongoTestHelper.addPassword("sito4", "user4", "password4");
		mongoTestHelper.addPassword("sito5", "user5", "password5");
		assertEquals(1, passwordManager.getPasswordsByWebSite("itow").size());
	}

	@Test
	public void testGetPasswordsByWebSiteWhenThereAreThreeMatchingPasswords() {
		mongoTestHelper.addPassword("site1", "user1", "password1");
		mongoTestHelper.addPassword("site2", "user2", "password2");
		mongoTestHelper.addPassword("site3", "user3", "password3");
		mongoTestHelper.addPassword("website1", "user1", "password1");
		mongoTestHelper.addPassword("website2", "user2", "password2");
		mongoTestHelper.addPassword("site1web", "user1", "password1");
		assertEquals(3, passwordManager.getPasswordsByWebSite("site1").size());
	}
}