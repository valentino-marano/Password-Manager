package com.valentino.tap.password_manager.test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.github.fakemongo.Fongo;
import com.mongodb.MongoClient;
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
		// close the shell
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				shell.close();
			}
		});
	}
	
	@Test
	public void testViewGUI() {
		bot.shell(Labels.frameTitle);
		bot.button(Labels.addLabel);
		bot.buttonWithTooltip(Labels.addHint);
		bot.button(Labels.editLabel);
		bot.buttonWithTooltip(Labels.editHint);
		bot.button(Labels.deleteLabel);
		bot.buttonWithTooltip(Labels.deleteHint);
		bot.table();
	}
}
