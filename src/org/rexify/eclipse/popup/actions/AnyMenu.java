package org.rexify.eclipse.popup.actions;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.rexify.eclipse.helper.CommandLine;
import org.rexify.eclipse.helper.EclipseHelper;
import org.rexify.eclipse.helper.InternetHelper;
import org.rexify.eclipse.model.TemplateModel;

public class AnyMenu {

	static public SelectionAdapter getCreateRexfileAction() {

		return new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// System.out.println(AnyMenu.getProjectPath());
				IProject selected_project = EclipseHelper.getProject();
				if(selected_project.exists() && !selected_project.isOpen()) {
					try {
						selected_project.open(null);
					} catch(CoreException ex) {
						// Show Error Message
						return;
					}
				}
				
				IFile rexfile = selected_project.getFile("Rexfile");
				if(rexfile.exists()) {
					// Messagebox that rexfile already exists.
				}
				else {
					//HashMap<String, String> template_list = new HashMap<String, String>();
					//template_list.put("Mac", "bar");
					//template_list.put("Linux", "bar");
					HashMap<String, TemplateModel> template_list = InternetHelper.getTemplates();
					
					String[] foo = template_list.keySet().toArray(new String[0]);
					
					ElementListSelectionDialog dialog = new ElementListSelectionDialog(null, new LabelProvider());
					dialog.setElements(foo);
					dialog.setTitle("Choose a Template");
					if(dialog.open() != Window.OK) {
						return;
					}
					
					System.out.println("Selected: " + dialog.getResult()[0]);

					String rexfile_template = template_list.get(dialog.getResult()[0].toString()).getTemplateCode();
					InputStream is = new ByteArrayInputStream(rexfile_template.getBytes());
					
					try {
						rexfile.create(is, true, null);
						EclipseHelper.openEditor(rexfile);
					} catch(CoreException ex) {
						// Display error message
					}
					
				}
			}
		};
	}
	
	
	static public SelectionAdapter getRunTaskAction(final String task_name) {
		
		return new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Running Task: " + task_name);
				List<String> cmd = new ArrayList<String>();

				if(System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
					cmd.add("cmd");
					cmd.add("/C");
					cmd.add("rex.bat -m " + task_name + " 2>&1");
				}
				else {
					cmd.add("/bin/sh");
					cmd.add("-c");
					cmd.add("rex -m " + task_name + " 2>&1");
				}
				
				File path = EclipseHelper.getProjectPath().toFile();

				MessageConsole console = EclipseHelper.findConsole("Rex Console");
				MessageConsoleStream out = console.newMessageStream();
				
				PrintStream default_out = System.out;
				PrintStream default_err = System.err;
				
				System.setOut(new PrintStream(out));
				System.setErr(new PrintStream(out));

				CommandLine.run(cmd, path);
				
				System.setOut(default_out);
				System.setErr(default_err);

			}
		};
	}
	

}
