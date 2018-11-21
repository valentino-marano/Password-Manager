package com.valentino.tap.password_manager.test;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.github.fakemongo.Fongo;
import com.mongodb.MongoClient;
import com.valentino.tap.password_manager.app.Password;
import com.valentino.tap.password_manager.app.PasswordManager;
import com.valentino.tap.password_manager.app.db.Database;
import com.valentino.tap.password_manager.app.db.MongoDatabaseWrapper;
import com.valentino.tap.password_manager.app.gui.Labels;
import com.valentino.tap.password_manager.app.gui.PasswordManagerGUI;

public class PasswordManagerGUITest {
	static Thread uiThread;

	private static SWTBot bot;

	private static Shell shell;
	private static PasswordManager passwordManager;

	private final static CyclicBarrier swtBarrier = new CyclicBarrier(2);

	@BeforeClass
	public static synchronized void setupApp() {
		if (uiThread == null) {
			uiThread = new Thread(new Runnable() {

				@Override
				public void run() {
					try {	
						while (true) {
							// open and layout the shell
							Fongo fongo = new Fongo("mongo server 1");
							MongoClient mongoClient = fongo.getMongo();
							Database database = new MongoDatabaseWrapper(mongoClient);
							passwordManager = new PasswordManager(database);
							PasswordManagerGUI window = new PasswordManagerGUI(passwordManager);
							window.open();
							shell = window.getShell();

							// wait for the test setup
							swtBarrier.await();

							// run the event loop
							window.eventLoop(Display.getDefault());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			uiThread.setDaemon(true);
			uiThread.start();
		}
	}

	@Before
	public final void setupSWTBot() throws InterruptedException, BrokenBarrierException {
		// synchronize with the thread opening the shell
		swtBarrier.await();
		bot = new SWTBot(shell);
	}

	@After
	public void closeShell() throws InterruptedException {
		while (!bot.activeShell().getText().equals(Labels.FRAME_TITLE))
			bot.activeShell().close();
		// close the shell
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				shell.close();
			}
		});
	}
	
	@Test
	public void testViewGUI() {
		assertEquals(Labels.FRAME_TITLE, bot.activeShell().getText());
		bot.button(Labels.ADD_LABEL);
		bot.buttonWithTooltip(Labels.ADD_HINT);
		bot.button(Labels.EDIT_LABEL);
		bot.buttonWithTooltip(Labels.EDIT_HINT);
		bot.button(Labels.DELETE_LABEL);
		bot.buttonWithTooltip(Labels.DELETE_HINT);
		bot.button(Labels.REFRESH_LABEL);
		bot.buttonWithTooltip(Labels.REFRESH_HINT);
		bot.textWithTooltip(Labels.SEARCH_HINT);
		bot.table();
	}
	
	@Test
	public void testTableRefresh() {		
		passwordManager.addPassword(new Password("sito1", "user1", "password1", Password.createDate(1999, 9, 9)));
		passwordManager.addPassword(new Password("sito2", "user2", "password2", Password.createDate(2012, 12, 12)));
		SWTBotTable table = bot.table();
		assertEquals(0, table.rowCount());
		bot.button(Labels.REFRESH_LABEL).click();
		assertEquals(2, table.rowCount());
		assertEquals("sito1", table.cell(0, 0));
		assertEquals("user1", table.cell(0, 1));
		assertEquals("password1", table.cell(0, 2));
		assertEquals("1999/09/09", table.cell(0, 3));
		assertEquals("sito2", table.cell(1, 0));
		assertEquals("user2", table.cell(1, 1));
		assertEquals("password2", table.cell(1, 2));
		assertEquals("2012/12/12", table.cell(1, 3));
	}
	
	@Test
	public void testAddPasswordStructure() {
		bot.button(Labels.ADD_LABEL).click();
		SWTBotShell addShell = bot.activeShell();
		assertEquals(Labels.ADD_TITLE, addShell.getText());
		SWTBot addBot = new SWTBot(addShell.widget);
		addBot.label(Labels.COLUMN_HEADERS[0]);
		addBot.text("", 0);
		addBot.label(Labels.COLUMN_HEADERS[1]);
		addBot.text("", 1);
		addBot.label(Labels.COLUMN_HEADERS[2]);
		addBot.text("", 2);
		addBot.label(Labels.COLUMN_HEADERS[3]);
		addBot.dateTime();
		addBot.button(Labels.OK_LABEL);
		addBot.button(Labels.CANCEL_LABEL);
	}
	
	@Test
	public void testAddPasswordCancel() {
		bot.button(Labels.ADD_LABEL).click();
		SWTBotShell addShell = bot.activeShell();
		SWTBot addBot = new SWTBot(addShell.widget);
		addBot.button(Labels.CANCEL_LABEL).click();
		addBot.waitUntil(Conditions.shellCloses(addShell));
		assertEquals(Labels.FRAME_TITLE, bot.activeShell().getText());
		assertEquals(0, bot.table().rowCount());
	}
	
	@Test
	public void testAddPasswordNotExists() throws ParseException {
		bot.button(Labels.ADD_LABEL).click();
		SWTBotShell addShell = bot.activeShell();
		SWTBot addBot = new SWTBot(addShell.widget);
		addBot.text("", 0).setText("sito1");
		addBot.text("", 0).setText("user1");
		addBot.text("", 0).setText("password1");
		addBot.button(Labels.OK_LABEL).click();
		addBot.waitUntil(Conditions.shellCloses(addShell));
		assertEquals(Labels.FRAME_TITLE, bot.activeShell().getText());
		SWTBotTable table = bot.table();
		assertEquals(1, table.rowCount());
	}
	
	@Test
	public void testAddPasswordAlreadyExists() {
		passwordManager.addPassword(new Password("sito1", "user1", "password1", Password.createDate(1999, 9, 9)));
		bot.button(Labels.ADD_LABEL).click();
		SWTBot addBot = new SWTBot(bot.activeShell().widget);
		addBot.text("", 0).setText("sito1");
		addBot.text("", 0).setText("user1");
		addBot.text("", 0).setText("password2");
		addBot.button(Labels.OK_LABEL).click();
		SWTBotShell errorShell = addBot.activeShell();
		SWTBot errorBot = new SWTBot(errorShell.widget);
		assertEquals(Labels.ERROR_TITLE, errorShell.getText());
		errorBot.label(Labels.EXISTS_MSG);
		errorBot.button(Labels.OK_LABEL).click();
		errorBot.waitUntil(Conditions.shellCloses(errorShell));
		assertEquals(Labels.ADD_TITLE, addBot.activeShell().getText());
	}
	
	@Test
	public void testEditPasswordNotSelected() {
		passwordManager.addPassword(new Password("sito1", "user1", "password1", Password.createDate(1999, 9, 9)));
		bot.button(Labels.REFRESH_LABEL).click();
		bot.button(Labels.EDIT_LABEL);
		assertEquals(Labels.FRAME_TITLE, bot.activeShell().getText());
	}
	
	@Test
	public void testEditPasswordStructure() {		
		passwordManager.addPassword(new Password("sito1", "user1", "password1", Password.createDate(1999, 9, 9)));
		bot.button(Labels.REFRESH_LABEL).click();
		bot.table().getTableItem(0).click();
		bot.button(Labels.EDIT_LABEL).click();
		SWTBotShell editShell = bot.activeShell();
		SWTBot editBot = new SWTBot(editShell.widget);
		assertEquals(Labels.EDIT_TITLE, editShell.getText());
		editBot.label(Labels.COLUMN_HEADERS[0]);
		editBot.text("sito1");
		editBot.label(Labels.COLUMN_HEADERS[1]);
		editBot.text("user1");
		editBot.label(Labels.COLUMN_HEADERS[2]);
		editBot.text("password1");
		editBot.label(Labels.COLUMN_HEADERS[3]);
		editBot.dateTime();
		editBot.button(Labels.CANCEL_LABEL);
		editBot.button(Labels.OK_LABEL);
	}
	
	@Test
	public void testEditPasswordCancel() throws ParseException {
		passwordManager.addPassword(new Password("sito1", "user1", "password1", Password.createDate(1999, 9, 9)));
		bot.button(Labels.REFRESH_LABEL).click();
		SWTBotTable table = bot.table();
		table.getTableItem(0).click();
		bot.button(Labels.EDIT_LABEL).click();
		SWTBotShell editShell = bot.activeShell();
		SWTBot editBot = new SWTBot(editShell.widget);
		editBot.text(0).setText("sito2");
		editBot.text(1).setText("user2");
		editBot.text(2).setText("password2");
		editBot.button(Labels.CANCEL_LABEL).click();
		editBot.waitUntil(Conditions.shellCloses(editShell));
		assertEquals(Labels.FRAME_TITLE, bot.activeShell().getText());
		assertEquals(1, table.rowCount());
		assertEquals("sito1", table.cell(0, 0));
		assertEquals("user1", table.cell(0, 1));
		assertEquals("password1", table.cell(0, 2));
		assertEquals("1999/09/09", table.cell(0, 3));
	}
	
	@Test
	public void testEditPasswordNotExists() throws ParseException {
		passwordManager.addPassword(new Password("sito1", "user1", "password1", Password.createDate(1999, 9, 9)));
		bot.button(Labels.REFRESH_LABEL).click();
		SWTBotTable table = bot.table();
		table.getTableItem(0).click();
		bot.button(Labels.EDIT_LABEL).click();
		SWTBotShell editShell = bot.activeShell();
		SWTBot editBot = new SWTBot(editShell.widget);
		editBot.text(0).setText("sito2");
		editBot.text(1).setText("user2");
		editBot.text(2).setText("password2");
		editBot.button(Labels.OK_LABEL).click();
		editBot.waitUntil(Conditions.shellCloses(editShell));
		assertEquals(Labels.FRAME_TITLE, bot.activeShell().getText());
		assertEquals(1, table.rowCount());
		assertEquals("sito2", table.cell(0, 0));
		assertEquals("user2", table.cell(0, 1));
		assertEquals("password2", table.cell(0, 2));
	}
	
	@Test
	public void testEditPasswordAlreadyExists() {
		passwordManager.addPassword(new Password("sito1", "user1", "password1", Password.createDate(1999, 9, 9)));
		passwordManager.addPassword(new Password("sito2", "user2", "password2", Password.createDate(1999, 9, 9)));
		bot.button(Labels.REFRESH_LABEL).click();
		bot.table().getTableItem(0).click();
		bot.button(Labels.EDIT_LABEL).click();
		SWTBot editBot = new SWTBot(bot.activeShell().widget);
		editBot.text(0).setText("sito2");
		editBot.text(1).setText("user2");
		editBot.button(Labels.OK_LABEL).click();
		SWTBotShell errorShell = editBot.activeShell();
		SWTBot errorBot = new SWTBot(errorShell.widget);
		assertEquals(Labels.ERROR_TITLE, errorShell.getText());
		errorBot.label(Labels.EXISTS_MSG);
		errorBot.button(Labels.OK_LABEL).click();
		errorBot.waitUntil(Conditions.shellCloses(errorShell));
		assertEquals(Labels.EDIT_TITLE, editBot.activeShell().getText());
	}
	
	@Test
	public void testDeletePasswordNotSelected() {
		passwordManager.addPassword(new Password("sito1", "user1", "password1", Password.createDate(1999, 9, 9)));
		passwordManager.addPassword(new Password("sito2", "user2", "password2", Password.createDate(1999, 9, 9)));
		bot.button(Labels.REFRESH_LABEL).click();
		bot.button(Labels.DELETE_LABEL).click();
		assertEquals(2, bot.table().rowCount());		
	}
	
	@Test
	public void testDeletePasswordSelected() {
		passwordManager.addPassword(new Password("sito1", "user1", "password1", Password.createDate(1999, 9, 9)));
		passwordManager.addPassword(new Password("sito2", "user2", "password2", Password.createDate(1999, 9, 9)));
		passwordManager.addPassword(new Password("sito3", "user3", "password3", Password.createDate(1999, 9, 9)));
		bot.button(Labels.REFRESH_LABEL).click();
		SWTBotTable table = bot.table();
		table.getTableItem(1).click();
		bot.button(Labels.DELETE_LABEL).click();
		assertEquals(2, table.rowCount());
		assertEquals("sito1", table.cell(0, 0));
		assertEquals("sito3", table.cell(1, 0));
	}
	
	@Test
	public void testSearchNotFound() {
		passwordManager.addPassword(new Password("site1", "user1", "password1", Password.createDate(1999, 9, 9)));
		bot.textWithTooltip(Labels.SEARCH_HINT).setText("we");
		assertEquals(0, bot.table().rowCount());
	}
	
	@Test
	public void testSearchByWebsite() {
		passwordManager.addPassword(new Password("site1", "user1", "password1", Password.createDate(1999, 9, 9)));
		passwordManager.addPassword(new Password("site2", "user2", "password2", Password.createDate(1999, 9, 9)));
		passwordManager.addPassword(new Password("sito3", "utente3", "password3", Password.createDate(1999, 9, 9)));
		bot.textWithTooltip(Labels.SEARCH_HINT).setText("ite");
		SWTBotTable table = bot.table();
		assertEquals(2, table.rowCount());
		assertEquals("site1", table.cell(0, 0));
		assertEquals("site2", table.cell(1, 0));
	}
	
	@Test
	public void testSearchByUser() {
		passwordManager.addPassword(new Password("site1", "user1", "password1", Password.createDate(1999, 9, 9)));
		passwordManager.addPassword(new Password("site2", "user2", "password2", Password.createDate(1999, 9, 9)));
		passwordManager.addPassword(new Password("site3", "utente3", "password3", Password.createDate(1999, 9, 9)));
		bot.textWithTooltip(Labels.SEARCH_HINT).setText("ser");
		SWTBotTable table = bot.table();
		assertEquals(2, table.rowCount());
		assertEquals("site1", table.cell(0, 0));
		assertEquals("site2", table.cell(1, 0));
	}
	
	@Test
	public void testSearchByWebsiteAndUser() {
		passwordManager.addPassword(new Password("site1", "user1", "password1", Password.createDate(1999, 9, 9)));
		passwordManager.addPassword(new Password("sito2", "site1", "password2", Password.createDate(1999, 9, 9)));
		passwordManager.addPassword(new Password("sito3", "utente3", "password3", Password.createDate(1999, 9, 9)));
		bot.textWithTooltip(Labels.SEARCH_HINT).setText("ite");
		SWTBotTable table = bot.table();
		assertEquals(2, table.rowCount());
		assertEquals("site1", table.cell(0, 0));
		assertEquals("sito2", table.cell(1, 0));
	}
	
	@Test
	public void testTableOrder() {
		passwordManager.addPassword(new Password("site2", "user1", "password1", Password.createDate(2019, 9, 9)));
		passwordManager.addPassword(new Password("site1", "user2", "password3", Password.createDate(2019, 11, 9)));
		passwordManager.addPassword(new Password("site3", "user3", "password2", Password.createDate(2019, 12, 9)));
		bot.button(Labels.REFRESH_LABEL).click();
		SWTBotTable table = bot.table();
		assertEquals("site2", table.cell(0, 0));
		assertEquals("site1", table.cell(1, 0));
		assertEquals("site3", table.cell(2, 0));
		table.header(Labels.COLUMN_HEADERS[0]).click();
		assertEquals("site1", table.cell(0, 0));
		assertEquals("site2", table.cell(1, 0));
		assertEquals("site3", table.cell(2, 0));
		table.header(Labels.COLUMN_HEADERS[0]).click();
		assertEquals("site3", table.cell(0, 0));
		assertEquals("site2", table.cell(1, 0));
		assertEquals("site1", table.cell(2, 0));
		table.header(Labels.COLUMN_HEADERS[1]).click();
		assertEquals("user1", table.cell(0, 1));
		assertEquals("user2", table.cell(1, 1));
		assertEquals("user3", table.cell(2, 1));
		table.header(Labels.COLUMN_HEADERS[1]).click();
		assertEquals("user3", table.cell(0, 1));
		assertEquals("user2", table.cell(1, 1));
		assertEquals("user1", table.cell(2, 1));
		table.header(Labels.COLUMN_HEADERS[2]).click();
		assertEquals("password1", table.cell(0, 2));
		assertEquals("password2", table.cell(1, 2));
		assertEquals("password3", table.cell(2, 2));
		table.header(Labels.COLUMN_HEADERS[2]).click();
		assertEquals("password3", table.cell(0, 2));
		assertEquals("password2", table.cell(1, 2));
		assertEquals("password1", table.cell(2, 2));
		table.header(Labels.COLUMN_HEADERS[3]).click();
		assertEquals("2019/09/09", table.cell(0, 3));
		assertEquals("2019/11/09", table.cell(1, 3));
		assertEquals("2019/12/09", table.cell(2, 3));
		table.header(Labels.COLUMN_HEADERS[3]).click();
		assertEquals("2019/12/09", table.cell(0, 3));
		assertEquals("2019/11/09", table.cell(1, 3));
		assertEquals("2019/09/09", table.cell(2, 3));
	}
}
