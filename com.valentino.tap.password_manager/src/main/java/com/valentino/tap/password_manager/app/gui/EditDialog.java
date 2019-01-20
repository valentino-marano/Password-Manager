package com.valentino.tap.password_manager.app.gui;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.valentino.tap.password_manager.app.Password;
import com.valentino.tap.password_manager.app.PasswordManager;

public class EditDialog extends Dialog {

	private PasswordManager passwordManager;
	private Shell shell;

	private Text websiteField;
	private Text userField;
	private Text passwordField;
	private DateTime expirationField;
	private Button okButton;
	private Date expiration;


	public EditDialog (PasswordManager passwordManager, Password password, Shell parent) {
		super(parent);
		this.passwordManager = passwordManager;
		createGUI();
		if (password == null)
			setAddMode();
		else
			setEditMode(password);
		shell.open();
		shell.pack();
		shell.layout();
		shell.forceActive();
	}
	
	private boolean checkExpiration() {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"), Locale.ITALY);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		expiration = Password.createDate(expirationField.getYear(), expirationField.getMonth(), expirationField.getDay());
		if (expiration.before(calendar.getTime())) {
			/* MessageBox attualmente non testabile con SWTBot
			 * Vedi bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=164192
			 */
			MessageDialog messageDialog = new MessageDialog(shell, Labels.ERROR_TITLE, Labels.EXPIRED_ERROR_MSG);
			messageDialog.eventLoop(Display.getDefault());
			return false;
		}
		return true;
	}

	private void setAddMode() {
		shell.setText(Labels.ADD_TITLE);
		okButton.setEnabled(false);
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (checkExpiration()) {
					Password newPassword = new Password(websiteField.getText(), userField.getText(), passwordField.getText(), expiration);
					if(passwordManager.addPassword(newPassword))
						shell.dispose();
					else {
						/* MessageBox attualmente non testabile con SWTBot
						 * Vedi bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=164192
						MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK)
						messageBox.setText(Labels.ERROR_TITLE)
						messageBox.setMessage(Labels.EXISTS_MSG)
						messageBox.open()
						*/
						MessageDialog messageDialog = new MessageDialog(shell, Labels.ERROR_TITLE, Labels.EXISTS_MSG);
						messageDialog.eventLoop(Display.getDefault());
					}
				}
			}
		});
	}

	private void setEditMode(Password password) {
		shell.setText(Labels.EDIT_TITLE);
		String oldWebsite = password.getWebsite();
		websiteField.setText(oldWebsite);
		String oldUser = password.getUsername();
		userField.setText(oldUser);
		passwordField.setText(password.getPassw());
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"), Locale.ITALY);
		calendar.setTime(password.getDateExpiration());
		expirationField.setDay(calendar.get(Calendar.DAY_OF_MONTH));
		expirationField.setMonth(calendar.get(Calendar.MONTH));
		expirationField.setYear(calendar.get(Calendar.YEAR));
		
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (checkExpiration()) {
					password.setWebsite(websiteField.getText());
					password.setUsername(userField.getText());
					password.setPassw(passwordField.getText());
					password.setExpiration(expiration);

					if(passwordManager.existsPassword(password) && !(password.getWebsite().equals(oldWebsite) && password.getUsername().equals(oldUser))) {
						/* MessageBox attualmente non testabile con SWTBot
						 * Vedi bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=164192
						MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK)
						messageBox.setText(Labels.ERROR_TITLE)
						messageBox.setMessage(Labels.EXISTS_MSG)
						messageBox.open()
						 */
						MessageDialog messageDialog = new MessageDialog(shell, Labels.ERROR_TITLE, Labels.EXISTS_MSG);
						messageDialog.eventLoop(Display.getDefault());
					} else {
						passwordManager.updatePassword(password);
						shell.dispose();
					}
				}
			}
		});
	}

	private void createGUI() {
		shell = new Shell(getParent(), SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL);
		shell.setBounds(300, 300, 600, 400);
		shell.setMinimumSize(600, 400);
		shell.setLayout(new GridLayout(2, true));

		// Labels & Input Fields
		Label websiteLabel = new Label(shell, SWT.NONE);
		websiteLabel.setText(Labels.COLUMN_HEADERS[0]);
		websiteLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		websiteField = new Text(shell, SWT.SINGLE | SWT.BORDER);
		websiteField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
		Label userLabel = new Label(shell, SWT.NONE);
		userLabel.setText(Labels.COLUMN_HEADERS[1]);
		userLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		userField = new Text(shell, SWT.SINGLE | SWT.BORDER);
		userField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
		Label passwordLabel = new Label(shell, SWT.NONE);
		passwordLabel.setText(Labels.COLUMN_HEADERS[2]);
		passwordLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		passwordField = new Text(shell, SWT.SINGLE | SWT.BORDER);
		passwordField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
		Label expirationLabel = new Label(shell, SWT.NONE);
		expirationLabel.setText(Labels.COLUMN_HEADERS[3]);
		expirationLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		expirationField = new DateTime(shell, SWT.CALENDAR);
		expirationField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));		

		// Cancel & Ok Buttons
		Button cancelButton = new Button(shell, SWT.PUSH);
		cancelButton.setText(Labels.CANCEL_LABEL);
		cancelButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();					
			}
		});

		okButton = new Button(shell, SWT.PUSH);
		okButton.setText(Labels.OK_LABEL);

		ModifyListener fieldListener = ((ModifyEvent arg0) -> {
			if (websiteField.getText().isEmpty() || 
					userField.getText().isEmpty() || 
					passwordField.getText().isEmpty())
				okButton.setEnabled(false);
			else
				okButton.setEnabled(true);
		});

		websiteField.addModifyListener(fieldListener);
		userField.addModifyListener(fieldListener);
		passwordField.addModifyListener(fieldListener);
	}

	public void eventLoop(Display display) {
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
