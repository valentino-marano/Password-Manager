package com.valentino.tap.password_manager.test.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.net.UnknownHostException;

import org.junit.Test;

public abstract class AbstractTest {

	protected SUT sut;

	public abstract void setUp() throws UnknownHostException, Exception;

	protected MongoTestHelper mongoTestHelper;

	public AbstractTest() {
		super();
	}

	@Test
	public void testGetAllPasswordsWhenThereAreNoPasswords() {
		assertTrue(sut.getAllPasswords().isEmpty());
	}

	@Test
	public void testGetAllPasswordsWhenThereIsOnePassword() {
		mongoTestHelper.addPassword("sito1", "user1", "password1");
		assertEquals(1, sut.getAllPasswords().size());
	}

	@Test
	public void testGetAllPasswordsWhenThereAreThreePasswords() {
		mongoTestHelper.addPassword("sito1", "user1", "password1");
		mongoTestHelper.addPassword("sito2", "user2", "password2");
		mongoTestHelper.addPassword("sito3", "user3", "password3");
		assertEquals(3, sut.getAllPasswords().size());
	}

	@Test
	public void testGetPasswordsByWebSiteWhenThereAreNoPasswords() {
		assertTrue(sut.getPasswordsByWebSite("sito").isEmpty());
	}

	@Test
	public void testGetPasswordsByWebSiteWhenThereAreNoMatchingPasswords() {
		mongoTestHelper.addPassword("sito1", "user1", "password1");
		mongoTestHelper.addPassword("sito2", "user2", "password2");
		mongoTestHelper.addPassword("sito3", "user3", "password3");
		assertTrue(sut.getPasswordsByWebSite("website").isEmpty());
	}

	@Test
	public void testGetPasswordsByWebSiteWhenThereIsOneWholeMatchingPasswords() {
		mongoTestHelper.addPassword("sito1", "user1", "password1");
		mongoTestHelper.addPassword("sito2", "user2", "password2");
		mongoTestHelper.addPassword("sito3", "user3", "password3");
		mongoTestHelper.addPassword("sito4", "user4", "password4");
		mongoTestHelper.addPassword("sito5", "user5", "password5");
		assertEquals(1, sut.getPasswordsByWebSite("sito1").size());
	}

	@Test
	public void testGetPasswordsByWebSiteWhenThereIsOneLikeMatchingPasswords() {
		mongoTestHelper.addPassword("sito1", "user1", "password1");
		mongoTestHelper.addPassword("sito2", "user2", "password2");
		mongoTestHelper.addPassword("sitoweb3", "user3", "password3");
		mongoTestHelper.addPassword("sito4", "user4", "password4");
		mongoTestHelper.addPassword("sito5", "user5", "password5");
		assertEquals(1, sut.getPasswordsByWebSite("itow").size());
	}

	@Test
	public void testGetPasswordsByWebSiteWhenThereAreThreeMatchingPasswords() {
		mongoTestHelper.addPassword("site1", "user1", "password1");
		mongoTestHelper.addPassword("site2", "user2", "password2");
		mongoTestHelper.addPassword("site3", "user3", "password3");
		mongoTestHelper.addPassword("website1", "user1", "password1");
		mongoTestHelper.addPassword("website2", "user2", "password2");
		mongoTestHelper.addPassword("site1web", "user1", "password1");
		assertEquals(3, sut.getPasswordsByWebSite("site1").size());
	}
}