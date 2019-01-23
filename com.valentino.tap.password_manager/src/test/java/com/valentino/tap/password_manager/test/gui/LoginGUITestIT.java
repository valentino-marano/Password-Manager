package com.valentino.tap.password_manager.test.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.mongodb.MongoClient;
import com.valentino.tap.password_manager.app.PasswordManager;
import com.valentino.tap.password_manager.app.db.Database;
import com.valentino.tap.password_manager.app.db.MongoDatabaseWrapper;
import com.valentino.tap.password_manager.app.gui.Labels;
import com.valentino.tap.password_manager.app.gui.LoginGUI;
import com.valentino.tap.password_manager.app.gui.PasswordManagerGUI;

public class LoginGUITestIT {
	static Thread uiThread;

	private static SWTBot bot;
	private static Database database;
	private static Shell shell;

	private final static CyclicBarrier swtBarrier = new CyclicBarrier(2);
	
	@BeforeClass
	public static synchronized void setupApp() {
		if (uiThread == null) {
			uiThread = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						boolean registered = false;
						while (true) {
							MongoClient mongoClient = new MongoClient("localhost", 27017);
							database = new MongoDatabaseWrapper(mongoClient, "admin", MongoDatabaseWrapper.ADMIN);
							
							if (!registered) { 
								database.register("Test", "TestDB", "TestPWD");
								registered = true;
							}
							
							LoginGUI window = new LoginGUI(database);
							window.open();
							shell = window.getShell();
							
							swtBarrier.await();

							Database database = window.eventLoop(Display.getDefault());
							if (database != null) {
								PasswordManager passwordManager = new PasswordManager(database);
								PasswordManagerGUI app = new PasswordManagerGUI(passwordManager);
								app.open();
								shell = app.getShell();
								app.eventLoop(Display.getDefault());
							}
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
		swtBarrier.await();
		bot = new SWTBot(shell);
	}

	@After
	public void closeShell() throws InterruptedException {
		while (!bot.activeShell().getText().equals(Labels.FRAME_TITLE) && !bot.activeShell().getText().equals(Labels.LOGIN_TITLE))
			bot.activeShell().close();
		
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				shell.close();
			}
		});
	}
	
	@Test
	public void testViewLogin() {
		assertEquals(Labels.LOGIN_TITLE, bot.activeShell().getText());
		bot.tabItem(Labels.LOGIN_LABEL);
		bot.label(Labels.getColumnHeaders(1));
		bot.comboBox();
		bot.label(Labels.getColumnHeaders(2));
		bot.text();
		SWTBotButton loginButton = bot.button(Labels.LOGIN_LABEL);
		assertFalse(loginButton.isEnabled());
	}
	
	@Test
	public void testViewRegister() {
		bot.tabItem(Labels.REGISTER_LABEL).activate();
		bot.label(Labels.getColumnHeaders(1));
		bot.text(0);
		bot.label(Labels.getColumnHeaders(2));
		bot.text(1);
		SWTBotButton registerButton = bot.button(Labels.REGISTER_LABEL);
		assertFalse(registerButton.isEnabled());
	}
	
	@Test
	public void testLoginSuccess() {
		SWTBotShell loginShell = bot.activeShell();
		bot.comboBox().setSelection("Test");
		bot.text().setText("TestPWD");
		SWTBotButton loginButton = bot.button(Labels.LOGIN_LABEL);
		assertTrue(loginButton.isEnabled());
		loginButton.click();
		bot.waitUntil(Conditions.shellCloses(loginShell));
		SWTBotShell passwordManagerShell = bot.activeShell();
		assertEquals(Labels.FRAME_TITLE, passwordManagerShell.getText());
	}
	
	@Test
	public void testLoginFail() {
		bot.comboBox().setSelection("Test");
		bot.text().setText("123");
		bot.button(Labels.LOGIN_LABEL).click();
		SWTBotShell errorShell = bot.activeShell();
		SWTBot errorBot = new SWTBot(errorShell.widget);
		assertEquals(Labels.ERROR_TITLE, errorShell.getText());
		errorBot.label(Labels.LOGIN_ERROR_MSG);
		assertTrue(errorBot.text().isReadOnly());
		errorBot.button(Labels.OK_LABEL).click();
		errorBot.waitUntil(Conditions.shellCloses(errorShell));
		assertEquals(Labels.LOGIN_TITLE, bot.activeShell().getText());
	}
	
	@Test
	public void testLoginButtonEnableDisable() {
		bot.comboBox().setSelection("Test");
		assertFalse(bot.button(Labels.LOGIN_LABEL).isEnabled());
		bot.text().setText("123");
		assertTrue(bot.button(Labels.LOGIN_LABEL).isEnabled());
		bot.text().setText("");
		assertFalse(bot.button(Labels.LOGIN_LABEL).isEnabled());
	}
	
	@Test
	public void testRegisterSuccess() {
		SWTBotShell loginShell = bot.activeShell();
		bot.tabItem(Labels.REGISTER_LABEL).activate();
		bot.text("").setText("Test2");
		bot.text("").setText("Test2PWD");
		SWTBotButton registerButton = bot.button(Labels.REGISTER_LABEL);
		assertTrue(registerButton.isEnabled());
		registerButton.click();
		bot.waitUntil(Conditions.shellCloses(loginShell));
		SWTBotShell passwordManagerShell = bot.activeShell();
		assertEquals(Labels.FRAME_TITLE, passwordManagerShell.getText());
	}
	
	@Test
	public void testRegisterFail() {
		bot.tabItem(Labels.REGISTER_LABEL).activate();
		bot.text("").setText("Test");
		bot.text("").setText("aaa");
		bot.button(Labels.REGISTER_LABEL).click();
		SWTBotShell errorShell = bot.activeShell();
		SWTBot errorBot = new SWTBot(errorShell.widget);
		assertEquals(Labels.ERROR_TITLE, errorShell.getText());
		errorBot.label(Labels.REGISTER_ERROR_MSG);
		assertTrue(errorBot.text().isReadOnly());
		errorBot.button(Labels.OK_LABEL).click();
		errorBot.waitUntil(Conditions.shellCloses(errorShell));
		assertEquals(Labels.LOGIN_TITLE, bot.activeShell().getText());
	}
}
