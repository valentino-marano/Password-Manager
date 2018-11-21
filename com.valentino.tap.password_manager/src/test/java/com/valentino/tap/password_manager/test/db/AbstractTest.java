package com.valentino.tap.password_manager.test.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.net.UnknownHostException;
import java.util.List;

import org.junit.Test;

import com.valentino.tap.password_manager.app.Password;

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
		mongoTestHelper.addPassword(new Password("sito1", "user1", "password1", Password.createDate(1999, 9, 9)));
		assertEquals(1, sut.getAllPasswords().size());
	}

	@Test
	public void testGetAllPasswordsWhenThereAreThreePasswords() {
		mongoTestHelper.addPassword(new Password("sito1", "user1", "password1", Password.createDate(1999, 9, 9)));
		mongoTestHelper.addPassword(new Password("sito2", "user2", "password2", Password.createDate(1999, 9, 9)));
		mongoTestHelper.addPassword(new Password("sito3", "user3", "password3", Password.createDate(1999, 9, 9)));
		assertEquals(3, sut.getAllPasswords().size());
	}

	@Test
	public void testGetPasswordsByWebSiteWhenThereAreNoPasswords() {
		assertTrue(sut.getPasswordsByWebSite("sito").isEmpty());
	}

	@Test
	public void testGetPasswordsByWebSiteWhenThereAreNoMatchingPasswords() {
		mongoTestHelper.addPassword(new Password("sito1", "user1", "password1", Password.createDate(1999, 9, 9)));
		mongoTestHelper.addPassword(new Password("sito2", "user2", "password2", Password.createDate(1999, 9, 9)));
		mongoTestHelper.addPassword(new Password("sito3", "user3", "password3", Password.createDate(1999, 9, 9)));
		assertTrue(sut.getPasswordsByWebSite("website").isEmpty());
	}

	@Test
	public void testGetPasswordsByWebSiteWhenThereIsOneWholeMatchingPasswords() {
		mongoTestHelper.addPassword(new Password("sito1", "user1", "password1", Password.createDate(1999, 9, 9)));
		mongoTestHelper.addPassword(new Password("sito2", "user2", "password2", Password.createDate(1999, 9, 9)));
		mongoTestHelper.addPassword(new Password("sito3", "user3", "password3", Password.createDate(1999, 9, 9)));
		mongoTestHelper.addPassword(new Password("sito4", "user4", "password4", Password.createDate(1999, 9, 9)));
		mongoTestHelper.addPassword(new Password("sito5", "user5", "password5", Password.createDate(1999, 9, 9)));
		assertEquals(1, sut.getPasswordsByWebSite("sito1").size());
	}

	@Test
	public void testGetPasswordsByWebSiteWhenThereIsOneLikeMatchingPasswords() {
		mongoTestHelper.addPassword(new Password("sito1", "user1", "password1", Password.createDate(1999, 9, 9)));
		mongoTestHelper.addPassword(new Password("sito2", "user2", "password2", Password.createDate(1999, 9, 9)));
		mongoTestHelper.addPassword(new Password("sitoweb3", "user3", "password3", Password.createDate(1999, 9, 9)));
		mongoTestHelper.addPassword(new Password("sito4", "user4", "password4", Password.createDate(1999, 9, 9)));
		mongoTestHelper.addPassword(new Password("sito5", "user5", "password5", Password.createDate(1999, 9, 9)));
		assertEquals(1, sut.getPasswordsByWebSite("itow").size());
	}

	@Test
	public void testGetPasswordsByWebSiteWhenThereAreThreeMatchingPasswords() {
		mongoTestHelper.addPassword(new Password("site1", "user1", "password1", Password.createDate(1999, 9, 9)));
		mongoTestHelper.addPassword(new Password("site2", "user2", "password2", Password.createDate(1999, 9, 9)));
		mongoTestHelper.addPassword(new Password("site3", "user3", "password3", Password.createDate(1999, 9, 9)));
		mongoTestHelper.addPassword(new Password("website1", "user1", "password1", Password.createDate(1999, 9, 9)));
		mongoTestHelper.addPassword(new Password("website2", "user2", "password2", Password.createDate(1999, 9, 9)));
		mongoTestHelper.addPassword(new Password("site1web", "user1", "password1", Password.createDate(1999, 9, 9)));
		assertEquals(3, sut.getPasswordsByWebSite("site1").size());
	}
	
	@Test
	public void testUpdatePassword() {
		Password password = new Password("site1", "user1", "password1", Password.createDate(1999, 9, 9));
		mongoTestHelper.addPassword(password);
		password.setWebsite("site2");
		password.setUsername("user2");
		password.setPassw("password2");
		password.setExpiration(Password.createDate(2012, 12, 12));
		sut.updatePassword(password);
		List<Password> allPasswords = sut.getAllPasswords();
		assertEquals(1, allPasswords.size());
		assertEquals("site2", allPasswords.get(0).getWebsite());
		assertEquals("user2", allPasswords.get(0).getUsername());
		assertEquals("password2", allPasswords.get(0).getPassw());
		assertEquals("2012/12/12", allPasswords.get(0).getExpiration());
	}

	@Test
	public void testSearchPasswordsWhenThereAreNoMatchingPasswords() {
		mongoTestHelper.addPassword(new Password("site1", "user1", "password1", Password.createDate(2012, 12, 12)));
		mongoTestHelper.addPassword(new Password("site2", "user2", "password2", Password.createDate(2012, 12, 12)));
		assertEquals(0, sut.searchPasswords("zzz").size());
	}

	@Test
	public void testSearchPasswordsWebsiteMatching() {
		mongoTestHelper.addPassword(new Password("site1", "user1", "password1", Password.createDate(2012, 12, 12)));
		mongoTestHelper.addPassword(new Password("site2", "user2", "password2", Password.createDate(2012, 12, 12)));
		mongoTestHelper.addPassword(new Password("website1", "user1", "password1", Password.createDate(2012, 12, 12)));
		mongoTestHelper.addPassword(new Password("website2", "user2", "password2", Password.createDate(2012, 12, 12)));
		assertEquals(2, sut.searchPasswords("bs").size());
	}

	@Test
	public void testSearchPasswordsUsernameMatching() {
		mongoTestHelper.addPassword(new Password("site1", "utente1", "password1", Password.createDate(2012, 12, 12)));
		mongoTestHelper.addPassword(new Password("site2", "user2", "password2", Password.createDate(2012, 12, 12)));
		mongoTestHelper.addPassword(new Password("website1", "user1", "password1", Password.createDate(2012, 12, 12)));
		mongoTestHelper.addPassword(new Password("website2", "utente2", "password2", Password.createDate(2012, 12, 12)));
		assertEquals(2, sut.searchPasswords("ten").size());
	}

	@Test
	public void testSearchPasswordsMixMatching() {
		mongoTestHelper.addPassword(new Password("site1", "user1", "password1", Password.createDate(2012, 12, 12)));
		mongoTestHelper.addPassword(new Password("sito2", "site1", "password2", Password.createDate(2012, 12, 12)));
		mongoTestHelper.addPassword(new Password("sito3", "user2", "password2", Password.createDate(2012, 12, 12)));
		assertEquals(2, sut.searchPasswords("ite").size());
	}
}