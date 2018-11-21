package com.valentino.tap.password_manager.app.gui;

import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
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
	private Password selected;
	private TableColumn selectedColumn;
	private boolean reversedOrder = false;
	private List<Password> passwords;
	static final Logger LOGGER = Logger.getLogger(PasswordManagerGUI.class);

	public PasswordManagerGUI(PasswordManager passwordManager) {
		this.passwordManager = passwordManager;
	}

	public void open() {
		createGUI();
		shell.open();
		shell.pack();
		shell.layout();
		shell.forceActive();
	}

	private void getPasswordsFromDB() {
		if (searchField.getText().isEmpty())
			passwords = passwordManager.getAllPasswords();
		else
			passwords = passwordManager.getSearchedPasswords(searchField.getText());			
	}

	private void refresh () {
		table.removeAll();
		for (Password password : passwords) {
			TableItem item = new TableItem(table, SWT.NULL);
			item.setData(password);
			item.setText(0, password.getWebsite());
			item.setText(1, password.getUsername());
			item.setText(2, password.getPassw());
			item.setText(3, password.getExpiration());
		}
		for (int i = 0; i < Labels.COLUMN_HEADERS.length; i++)
			table.getColumn(i).pack();
	}

	private void createGUI() {
		shell = new Shell();
		shell.setText(Labels.FRAME_TITLE);
		shell.setBounds(300, 300, 750, 500);
		shell.setMinimumSize(750, 500);
		shell.setLayout(new GridLayout(5, false));

		Button addButton = new Button(shell, SWT.PUSH);
		addButton.setText(Labels.ADD_LABEL);
		addButton.setToolTipText(Labels.ADD_HINT);
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				EditDialog edit = new EditDialog(passwordManager, null, shell);
				edit.eventLoop(Display.getDefault());
				getPasswordsFromDB();
				refresh();
			}
		});

		Button editButton = new Button(shell, SWT.PUSH);
		editButton.setText(Labels.EDIT_LABEL);
		editButton.setToolTipText(Labels.EDIT_HINT);
		editButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (selected != null) {
					EditDialog edit = new EditDialog(passwordManager, selected, shell);
					edit.eventLoop(Display.getDefault());
					getPasswordsFromDB();
					refresh();
				}
			}
		});

		Button deleteButton = new Button(shell, SWT.PUSH);
		deleteButton.setText(Labels.DELETE_LABEL);
		deleteButton.setToolTipText(Labels.DELETE_HINT);
		deleteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (selected != null) {
					passwordManager.deletePassword(selected);
					getPasswordsFromDB();
					refresh();
				}
			}
		});

		Button refreshButton = new Button(shell, SWT.PUSH);
		refreshButton.setText(Labels.REFRESH_LABEL);
		refreshButton.setToolTipText(Labels.REFRESH_HINT);
		refreshButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getPasswordsFromDB();
				refresh();
				selected = null;
			}
		});

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

		GridData gridDataTable = new GridData();
		gridDataTable.horizontalAlignment = GridData.FILL;
		gridDataTable.verticalAlignment = GridData.FILL;
		gridDataTable.horizontalSpan = 5;
		gridDataTable.grabExcessHorizontalSpace = true;
		gridDataTable.grabExcessVerticalSpace = true;

		table = new Table(shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		for (int i = 0; i < Labels.COLUMN_HEADERS.length; i++) {
			TableColumn column = new TableColumn(table, SWT.NULL);
			column.setText(Labels.COLUMN_HEADERS[i]);
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
					comparator = Comparator.comparing(Password::getExpiration);

				if (reversedOrder)
					comparator = comparator.reversed();

				passwords.sort(comparator);
				table.setSortColumn(selectedColumn);
				refresh();
			});
		}
		table.setLayoutData(gridDataTable);
	}

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