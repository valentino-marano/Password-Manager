package com.valentino.tap.password_manager.app.gui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.valentino.tap.password_manager.app.Password;
import com.valentino.tap.password_manager.app.PasswordManager;

public class PasswordManagerGUI {

	private Table table;
	private PasswordManager passwordManager;
	private Shell shell;
	private Text searchField;
	private String searchedString;
	private Password selected;
	private TableColumn selectedColumn;
	private boolean reversedOrder = false;
	private boolean expiredPasswords;
	private List<Password> passwords;
	private SimpleDateFormat simpleDateFormat = new Labels().getSimpledateformat();
	static final Logger LOGGER = Logger.getLogger(PasswordManagerGUI.class);

	public PasswordManagerGUI(PasswordManager passwordManager) {
		this.passwordManager = passwordManager;
		shell = new Shell();
	}

	public void open() {
		createGUI();
		shell.open();
		shell.pack();
		shell.layout();
		shell.forceActive();
		checkExpirations();
	}

	private void getPasswordsFromDB() {
		if (searchField.getText().isEmpty())
			passwords = passwordManager.getAllPasswords();
		else
			passwords = passwordManager.getSearchedPasswords(searchField.getText());			
	}

	private void refresh () {
		table.removeAll();
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"), Locale.ITALY);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date today = calendar.getTime();
		expiredPasswords = false;
				
		for (Password password : passwords) {
			TableItem item = new TableItem(table, SWT.NULL);
			item.setData(password);
			item.setText(0, password.getWebsite());
			item.setText(1, password.getUsername());
			item.setText(2, password.getPassw());
			item.setText(3, simpleDateFormat.format(password.getDateExpiration()));
			if (password.getDateExpiration().before(today)) {
				LOGGER.info(password.getDateExpiration().toString());
				expiredPasswords = true;
				item.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
			}
		}
		
		for (int i = 0; i < Labels.getColumnHeadersLength(); i++)
			table.getColumn(i).pack();
	}
	
	private void checkExpirations() {
		if (expiredPasswords) {
			/* MessageBox attualmente non testabile con SWTBot
			 * Vedi bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=164192
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK)
			messageBox.setText(Labels.EXPIRED_TITLE)
			messageBox.setMessage(Labels.EXPIRED_MSG)
			messageBox.open()
			*/
			MessageDialog messageDialog = new MessageDialog(shell, Labels.EXPIRED_TITLE, Labels.EXPIRED_MSG);
			messageDialog.eventLoop(Display.getDefault());
		}
	}

	private void createGUI() {
		shell.setText(Labels.FRAME_TITLE);
		shell.setBounds(300, 300, 750, 500);
		shell.setMinimumSize(750, 500);
		shell.setLayout(new GridLayout(5, false));

		Button addButton = new Button(shell, SWT.PUSH);
		addButton.setText(Labels.ADD_LABEL);
		addButton.setToolTipText(Labels.ADD_HINT);
		addButton.addSelectionListener(addButtonAction);

		Button editButton = new Button(shell, SWT.PUSH);
		editButton.setText(Labels.EDIT_LABEL);
		editButton.setToolTipText(Labels.EDIT_HINT);
		editButton.addSelectionListener(editButtonAction);

		Button deleteButton = new Button(shell, SWT.PUSH);
		deleteButton.setText(Labels.DELETE_LABEL);
		deleteButton.setToolTipText(Labels.DELETE_HINT);
		deleteButton.addSelectionListener(deleteButtonAction);

		Button refreshButton = new Button(shell, SWT.PUSH);
		refreshButton.setText(Labels.REFRESH_LABEL);
		refreshButton.setToolTipText(Labels.REFRESH_HINT);
		refreshButton.addSelectionListener(refreshButtonAction);

		GridData gridDataSearch = new GridData();
		gridDataSearch.horizontalAlignment = GridData.FILL;
		gridDataSearch.grabExcessHorizontalSpace = true;
		searchField = new Text(shell, SWT.SEARCH | SWT.BORDER);
		searchField.setLayoutData(gridDataSearch);
		searchField.setToolTipText(Labels.SEARCH_HINT);
		searchField.addModifyListener((ModifyEvent arg0) -> {
			getPasswordsFromDB();
			refresh();			
		});
		
		searchField.addFocusListener(searchFieldAction);
		
		GridData gridDataTable = new GridData();
		gridDataTable.horizontalAlignment = GridData.FILL;
		gridDataTable.verticalAlignment = GridData.FILL;
		gridDataTable.horizontalSpan = 5;
		gridDataTable.grabExcessHorizontalSpace = true;
		gridDataTable.grabExcessVerticalSpace = true;

		table = new Table(shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		for (int i = 0; i < Labels.getColumnHeadersLength(); i++) {
			TableColumn column = new TableColumn(table, SWT.NULL);
			column.setText(Labels.getColumnHeaders(i));
		}

		getPasswordsFromDB();
		refresh();

		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				selected = ((Password) event.item.getData());
				LOGGER.info("Selected Password " +  selected.getWebsite() + " " + selected.getUsername());
			}
		});
		
		for (TableColumn column : table.getColumns()) {
			column.addListener(SWT.Selection, (Event e) -> {
				TableColumn clickedColumn = (TableColumn) e.widget;

				if (clickedColumn == selectedColumn)
					reversedOrder = !reversedOrder;
				else {
					selectedColumn = clickedColumn;
					reversedOrder = false;
				}

				Comparator<Password> comparator = null;

				if (selectedColumn == table.getColumn(0))
					comparator = Comparator.comparing(Password::getWebsite);
				else if (selectedColumn == table.getColumn(1)) 
					comparator = Comparator.comparing(Password::getUsername);
				else if (selectedColumn == table.getColumn(2)) 
					comparator = Comparator.comparing(Password::getPassw);
				else if (selectedColumn == table.getColumn(3)) 
					comparator = Comparator.comparing(Password::getDateExpiration);

				if (reversedOrder)
					comparator = comparator.reversed();

				passwords.sort(comparator);
				table.setSortColumn(selectedColumn);
				refresh();
			});
		}
		table.setLayoutData(gridDataTable);			
	}

	private SelectionAdapter editButtonAction = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			if (selected != null) {
				EditDialog edit = new EditDialog(passwordManager, selected, shell);
				edit.eventLoop(Display.getDefault());
				getPasswordsFromDB();
				refresh();
				checkExpirations();
			}
		}
	};
	
	private SelectionAdapter addButtonAction = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			EditDialog edit = new EditDialog(passwordManager, null, shell);
			edit.eventLoop(Display.getDefault());
			getPasswordsFromDB();
			refresh();
			checkExpirations();
		}
	};

	private SelectionAdapter deleteButtonAction = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			if (selected != null) {
				passwordManager.deletePassword(selected);
				getPasswordsFromDB();
				refresh();
			}
		}
	};
	
	private SelectionAdapter refreshButtonAction = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			getPasswordsFromDB();
			refresh();
			checkExpirations();
			selected = null;
		}
	};
	
	private FocusListener searchFieldAction = new FocusListener() {
		@Override
		public void focusLost(FocusEvent arg0) {
			if (!searchedString.equals(searchField.getText()))
				checkExpirations();				
		}

		@Override
		public void focusGained(FocusEvent arg0) {
			searchedString = searchField.getText();
		}
	};
	
	public void eventLoop(Display display) {
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public Shell getShell() {
		return shell;
	}
}