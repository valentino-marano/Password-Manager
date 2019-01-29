package com.valentino.tap.password_manager.app;

import java.net.UnknownHostException;
import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.valentino.tap.password_manager.app.db.Database;
import com.valentino.tap.password_manager.app.db.MongoDatabaseWrapper;
import com.valentino.tap.password_manager.app.gui.LoginGUI;
import com.valentino.tap.password_manager.app.gui.PasswordManagerGUI;

public class Application {
	static final Logger LOGGER = Logger.getLogger(Application.class);

	/********** main() **********/
	/**
		@brief Main method, it shows the login GUI and after correct login/registration shows main GUI
		@throws UnknownHostException if mongo host cannot be reached
	 */
	public static void main(String[] args) throws UnknownHostException {
		ServerAddress serverAddress = new ServerAddress("localhost", 27017);
		MongoClient mongoAdmin = new MongoClient(serverAddress);
		Database adminDB = new MongoDatabaseWrapper(mongoAdmin, "admin", MongoDatabaseWrapper.ADMIN);

		LoginGUI login = new LoginGUI(adminDB);
		login.open();
		Database database = login.eventLoop(Display.getDefault());
		if (database != null) {
			PasswordManager passwordManager = new PasswordManager(database);
			PasswordManagerGUI app = new PasswordManagerGUI(passwordManager);
			app.open();
			app.eventLoop(Display.getDefault());
		}
	}		
}
