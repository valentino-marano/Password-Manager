package com.valentino.tap.password_manager.app.gui;

public class Labels {
	
	private Labels() {}
	
	// Titles & Headers
	public static final String FRAME_TITLE = "Password Manager";
	public static final String ERROR_TITLE = "Error Occurred";
	public static final String ADD_TITLE = "Add a new password";
	public static final String EDIT_TITLE = "Edit an existing password";
	public static final String[] COLUMN_HEADERS = {"Website", "Username", "Password"};
	
	// Labels
	public static final String DELETE_LABEL = "Delete Password";
	public static final String ADD_LABEL = "Add Password";
	public static final String EDIT_LABEL = "Edit Password";
	public static final String REFRESH_LABEL = "Refresh";
	public static final String OK_LABEL = "Ok";
	public static final String CANCEL_LABEL = "Cancel";
	
	// Hints	
	public static final String DELETE_HINT = "Deletes a password";
	public static final String ADD_HINT = "Opens pop-up to add a new password";
	public static final String EDIT_HINT = "Opens pop-up to edit a password";
	public static final String REFRESH_HINT = "Reloads Passwords from Database";
	
	// Messages
	public static final String EXISTS_MSG = "Cannot save this password, it already exists in Database";
}
