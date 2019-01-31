package com.valentino.tap.password_manager.test.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.valentino.tap.password_manager.app.Password;

public abstract class AbstractTest {

	protected SUT sut;

	public abstract void setUp() throws UnknownHostException, Exception;

	protected MongoTestHelper mongoTestHelper;
	protected static Calendar calendar;

	public AbstractTest() {
		super();
	}
	
	@Before
	public void createCalendar() {
		calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"), Locale.ITALY);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}
	
	@Test
	public void testGetAllPasswordsWhenThereAreNoPasswords() {
		assertTrue(sut.getAllPasswords().isEmpty());
	}

	@Test
	public void testGetAllPasswordsWhenThereIsOnePassword() {
		mongoTestHelper.addPassword(new Password("sito1", "user1", "password1", calendar.getTime()));
		assertEquals(1, sut.getAllPasswords().size());
	}

	@Test
	public void testGetAllPasswordsWhenThereAreThreePasswords() {
		mongoTestHelper.addPassword(new Password("sito1", "user1", "password1", calendar.getTime()));
		mongoTestHelper.addPassword(new Password("sito2", "user2", "password2", calendar.getTime()));
		mongoTestHelper.addPassword(new Password("sito3", "user3", "password3", calendar.getTime()));
		assertEquals(3, sut.getAllPasswords().size());
	}

	@Test
	public void testGetPasswordsByWebSiteWhenThereAreNoPasswords() {
		assertTrue(sut.getPasswordsByWebSite("sito").isEmpty());
	}

	@Test
	public void testGetPasswordsByWebSiteWhenThereAreNoMatchingPasswords() {
		mongoTestHelper.addPassword(new Password("sito1", "user1", "password1", calendar.getTime()));
		mongoTestHelper.addPassword(new Password("sito2", "user2", "password2", calendar.getTime()));
		mongoTestHelper.addPassword(new Password("sito3", "user3", "password3", calendar.getTime()));
		assertTrue(sut.getPasswordsByWebSite("website").isEmpty());
	}

	@Test
	public void testGetPasswordsByWebSiteWhenThereIsOneWholeMatchingPasswords() {
		mongoTestHelper.addPassword(new Password("sito1", "user1", "password1", calendar.getTime()));
		mongoTestHelper.addPassword(new Password("sito2", "user2", "password2", calendar.getTime()));
		mongoTestHelper.addPassword(new Password("sito3", "user3", "password3", calendar.getTime()));
		mongoTestHelper.addPassword(new Password("sito4", "user4", "password4", calendar.getTime()));
		mongoTestHelper.addPassword(new Password("sito5", "user5", "password5", calendar.getTime()));
		assertEquals(1, sut.getPasswordsByWebSite("sito1").size());
	}

	@Test
	public void testGetPasswordsByWebSiteWhenThereIsOneLikeMatchingPasswords() {
		mongoTestHelper.addPassword(new Password("sito1", "user1", "password1", calendar.getTime()));
		mongoTestHelper.addPassword(new Password("sito2", "user2", "password2", calendar.getTime()));
		mongoTestHelper.addPassword(new Password("sitoweb3", "user3", "password3", calendar.getTime()));
		mongoTestHelper.addPassword(new Password("sito4", "user4", "password4", calendar.getTime()));
		mongoTestHelper.addPassword(new Password("sito5", "user5", "password5", calendar.getTime()));
		assertEquals(1, sut.getPasswordsByWebSite("itow").size());
	}

	@Test
	public void testGetPasswordsByWebSiteWhenThereAreThreeMatchingPasswords() {
		mongoTestHelper.addPassword(new Password("site1", "user1", "password1", calendar.getTime()));
		mongoTestHelper.addPassword(new Password("site2", "user2", "password2", calendar.getTime()));
		mongoTestHelper.addPassword(new Password("site3", "user3", "password3", calendar.getTime()));
		mongoTestHelper.addPassword(new Password("website1", "user1", "password1", calendar.getTime()));
		mongoTestHelper.addPassword(new Password("website2", "user2", "password2", calendar.getTime()));
		mongoTestHelper.addPassword(new Password("site1web", "user1", "password1", calendar.getTime()));
		assertEquals(3, sut.getPasswordsByWebSite("site1").size());
	}
	
	@Test
	public void testUpdatePassword() {
		Password password = new Password("site1", "user1", "password1", calendar.getTime());
		mongoTestHelper.addPassword(password);
		password.setWebsite("site2");
		password.setUsername("user2");
		password.setPassw("password2");
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		Date date1 = calendar.getTime();
		password.setExpiration(date1);
		sut.updatePassword(password);
		List<Password> allPasswords = sut.getAllPasswords();
		assertEquals(1, allPasswords.size());
		assertEquals("site2", allPasswords.get(0).getWebsite());
		assertEquals("user2", allPasswords.get(0).getUsername());
		assertEquals("password2", allPasswords.get(0).getPassw());
		assertEquals(date1, allPasswords.get(0).getDateExpiration());
	}

	@Test
	public void testSearchPasswordsWhenThereAreNoMatchingPasswords() {
		mongoTestHelper.addPassword(new Password("site1", "user1", "password1", calendar.getTime()));
		mongoTestHelper.addPassword(new Password("site2", "user2", "password2", calendar.getTime()));
		assertEquals(0, sut.searchPasswords("zzz").size());
	}

	@Test
	public void testSearchPasswordsWebsiteMatching() {
		mongoTestHelper.addPassword(new Password("site1", "user1", "password1", calendar.getTime()));
		mongoTestHelper.addPassword(new Password("site2", "user2", "password2", calendar.getTime()));
		mongoTestHelper.addPassword(new Password("website1", "user1", "password1", calendar.getTime()));
		mongoTestHelper.addPassword(new Password("website2", "user2", "password2", calendar.getTime()));
		assertEquals(2, sut.searchPasswords("bs").size());
	}

	@Test
	public void testSearchPasswordsUsernameMatching() {
		mongoTestHelper.addPassword(new Password("site1", "utente1", "password1", calendar.getTime()));
		mongoTestHelper.addPassword(new Password("site2", "user2", "password2", calendar.getTime()));
		mongoTestHelper.addPassword(new Password("website1", "user1", "password1", calendar.getTime()));
		mongoTestHelper.addPassword(new Password("website2", "utente2", "password2", calendar.getTime()));
		assertEquals(2, sut.searchPasswords("ten").size());
	}

	@Test
	public void testSearchPasswordsMixMatching() {
		mongoTestHelper.addPassword(new Password("site1", "user1", "password1", calendar.getTime()));
		mongoTestHelper.addPassword(new Password("sito2", "site1", "password2", calendar.getTime()));
		mongoTestHelper.addPassword(new Password("sito3", "user2", "password2", calendar.getTime()));
		assertEquals(2, sut.searchPasswords("ite").size());
	}
}