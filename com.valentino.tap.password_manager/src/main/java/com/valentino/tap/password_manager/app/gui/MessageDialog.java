package com.valentino.tap.password_manager.app.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class MessageDialog extends Dialog {
	
	private Shell shell;
	private String title;
	private String message;
	private String text;

	public MessageDialog(Shell parent, String title, String message) {
		super(parent);
		this.title = title;
		this.message = message;
		createDialog();
	}
	
	public MessageDialog(Shell parent, String title, String message, String text) {
		super(parent);
		this.title = title;
		this.message = message;
		this.text = text;
		createDialog();
	}
	
	private void createDialog() {
		shell = new Shell(getParent(), SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL);
		shell.setBounds(300, 300, 400, 100);
		shell.setMinimumSize(shell.getSize());
		shell.setText(title);
		shell.setLayout(new GridLayout(1, true));
		Label messageLabel = new Label(shell, SWT.NONE);
		messageLabel.setText(message);
		
		if (this.text != null) {
			Text errorText = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY);
		    errorText.setText(text);
		    errorText.setLayoutData(new GridData(GridData.FILL_BOTH));
		    shell.setBounds(300, 300, 400, 200);
			shell.setMinimumSize(shell.getSize());
		}
		
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.CENTER;
		Button okButton = new Button(shell, SWT.PUSH);
		okButton.setText(Labels.OK_LABEL);
		okButton.setLayoutData(gridData);
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		
		shell.open();
		shell.pack();
		shell.layout();
		shell.forceActive();	
	}
	
	public void eventLoop(Display display) {
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}