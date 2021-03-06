package com.valentino.tap.password_manager.app.gui;

import java.text.SimpleDateFormat;

public class Labels {
	
	// Titles & Headers
	public static final String FRAME_TITLE = "Password Manager";
	public static final String ERROR_TITLE = "Error Occurred";
	public static final String ADD_TITLE = "Add a new password";
	public static final String EDIT_TITLE = "Edit an existing password";
	private static final String[] COLUMN_HEADERS = {"Website", "Username", "Password", "Expiration"};
	public static final String EXPIRED_TITLE = "Expired Passwords";
	public static final String LOGIN_TITLE = "Login or Register";
	
	// Labels
	public static final String DELETE_LABEL = "Delete Password";
	public static final String ADD_LABEL = "Add Password";
	public static final String EDIT_LABEL = "Edit Password";
	public static final String REFRESH_LABEL = "Refresh";
	public static final String OK_LABEL = "Ok";
	public static final String CANCEL_LABEL = "Cancel";
	public static final String LOGIN_LABEL = "Login";
	public static final String REGISTER_LABEL = "Register";
	
	// Hints	
	public static final String DELETE_HINT = "Deletes a password";
	public static final String ADD_HINT = "Opens pop-up to add a new password";
	public static final String EDIT_HINT = "Opens pop-up to edit a password";
	public static final String REFRESH_HINT = "Reloads Passwords from Database";
	public static final String SEARCH_HINT = "Search by website or by username";
	
	// Messages
	public static final String EXISTS_MSG = "Cannot save this password, it already exists in Database";
	public static final String EXPIRED_MSG = "Some passwords have expired, check the highlighted ones.";
	public static final String EXPIRED_ERROR_MSG = "Password has expired date";
	public static final String REGISTER_ERROR_MSG = "User Registration Failed";
	public static final String LOGIN_ERROR_MSG = "Login User Failed";
	
	private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
	
	public static String getColumnHeaders(int index) {
		return COLUMN_HEADERS[index];
	}
	
	public static int getColumnHeadersLength() {
		return COLUMN_HEADERS.length;
	}

	public SimpleDateFormat getSimpledateformat() {
		return simpleDateFormat;
	}
}
