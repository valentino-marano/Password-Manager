package com.valentino.tap.password_manager.app.gui;

import java.util.ArrayList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import com.valentino.tap.password_manager.app.db.Database;

public class LoginGUI {
	
	private Database adminDB;
	private Database database;
	private Shell shell;

	public LoginGUI (Database adminDB) {
		this.adminDB = adminDB;
	}
	
	public void open() {
		createGUI();
		shell.open();
		shell.pack();
		shell.layout();
		shell.forceActive();
	}
	
	public Database eventLoop(Display display) {
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		
		return database;
	}
	
	private void createGUI() {
		shell = new Shell(SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL);
		shell.setText(Labels.LOGIN_TITLE);
		shell.setBounds(300, 300, 300, 180);
		shell.setMinimumSize(shell.getSize());
		
		// TabFolder & Tabs
		TabFolder tabFolder = new TabFolder(shell, SWT.BORDER);
		tabFolder.setSize(shell.getSize());
		
		TabItem tabLogin = new TabItem(tabFolder, SWT.NULL);
	    tabLogin.setText(Labels.LOGIN_LABEL);
	    
	    TabItem tabRegister = new TabItem(tabFolder, SWT.NULL);
	    tabRegister.setText(Labels.REGISTER_LABEL);		
		
	    ArrayList<String> userArray = (ArrayList<String>) adminDB.getUsers();
	    
		// Login Labels & Fields
		Composite compositeLogin = new Composite(tabFolder, SWT.NONE);
		compositeLogin.setLayout(new GridLayout(2, true));
	    
		Label userLabelLogin = new Label(compositeLogin, SWT.NONE);
		userLabelLogin.setText(Labels.COLUMN_HEADERS[1]);
		userLabelLogin.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		
		Combo userComboLogin = new Combo(compositeLogin, SWT.DROP_DOWN | SWT.READ_ONLY); 
		userComboLogin.setItems(userArray.toArray(new String[userArray.size()]));
		userComboLogin.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
		
		Label passwordLabelLogin = new Label(compositeLogin, SWT.NONE);
		passwordLabelLogin.setText(Labels.COLUMN_HEADERS[2]);
		passwordLabelLogin.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		
		Text passwordFieldLogin = new Text(compositeLogin, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);
		passwordFieldLogin.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
		
		// Login Button
		Button loginButton = new Button(compositeLogin, SWT.PUSH);
		loginButton.setText(Labels.LOGIN_LABEL);
		loginButton.setEnabled(false);
		loginButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					database = adminDB.login(userComboLogin.getText(), userComboLogin.getText() + "DB", passwordFieldLogin.getText());
					shell.dispose();
					} catch (RuntimeException exception) {
					/* MessageBox attualmente non testabile con SWTBot
					 * Vedi bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=164192
					 */
					MessageDialog messageDialog = new MessageDialog(shell, Labels.ERROR_TITLE, Labels.LOGIN_ERROR_MSG, exception.getMessage());
					messageDialog.eventLoop(Display.getDefault());
				}
			}
		});
		
		passwordFieldLogin.addModifyListener((ModifyEvent arg0) -> {
			if (passwordFieldLogin.getText().isEmpty())
				loginButton.setEnabled(false);
			else
				loginButton.setEnabled(true);
		});
					
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = false;
		loginButton.setLayoutData(gridData);
		
		tabLogin.setControl(compositeLogin);

	    // Register Labels & Fields
		Composite compositeRegister = new Composite(tabFolder, SWT.NONE);
		compositeRegister.setLayout(new GridLayout(2, true));
		
	    Label userLabelRegister = new Label(compositeRegister, SWT.NONE);
	    userLabelRegister.setText(Labels.COLUMN_HEADERS[1]);
	    userLabelRegister.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
	    
	    Text userFieldRegister = new Text(compositeRegister, SWT.SINGLE | SWT.BORDER);
	    userFieldRegister.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
	    
	    Label passwordLabelRegister = new Label(compositeRegister, SWT.NONE);
	    passwordLabelRegister.setText(Labels.COLUMN_HEADERS[2]);
	    passwordLabelRegister.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
	    
	    Text passwordFieldRegister = new Text(compositeRegister, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);
	    passwordFieldRegister.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));

	    // Register Button
	    Button registerButton = new Button(compositeRegister, SWT.PUSH);
		registerButton.setText(Labels.REGISTER_LABEL);
		registerButton.setEnabled(false);
		registerButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					database = adminDB.register(userFieldRegister.getText(), userFieldRegister.getText() + "DB", passwordFieldRegister.getText());
					shell.dispose();
				} catch (RuntimeException exception) {
					/* MessageBox attualmente non testabile con SWTBot
					 * Vedi bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=164192
					 */
					MessageDialog messageDialog = new MessageDialog(shell, Labels.ERROR_TITLE, Labels.REGISTER_ERROR_MSG, exception.getMessage());
					messageDialog.eventLoop(Display.getDefault());
				}
			}
		});	
		
		ModifyListener fieldListener = ((ModifyEvent arg0) -> {
			if (userFieldRegister.getText().isEmpty() ||  
					passwordFieldRegister.getText().isEmpty())
				registerButton.setEnabled(false);
			else
				registerButton.setEnabled(true);
		});
		
		userFieldRegister.addModifyListener(fieldListener);
		passwordFieldRegister.addModifyListener(fieldListener);
		
		registerButton.setLayoutData(gridData);
		tabRegister.setControl(compositeRegister);
	}
	
	public Shell getShell() {
		return shell;
	}
}
