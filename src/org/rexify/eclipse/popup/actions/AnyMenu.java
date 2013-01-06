package org.rexify.eclipse.popup.actions;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public class AnyMenu {

	static public SelectionAdapter getCreateRexfileAction() {
		
		return new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// System.out.println(AnyMenu.getProjectPath());
				IProject selected_project = AnyMenu.getProject();
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
					String rexfile_template = "use Rex -base;\n\ntask 'build', sub {\n   build;\n};\n";
					InputStream is = new ByteArrayInputStream(rexfile_template.getBytes());
					
					try {
						rexfile.create(is, true, null);
						AnyMenu.openEditor(rexfile);
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

				Runtime run = Runtime.getRuntime();
				try {
					// if rex is installed via cpan this is not needed.
					String[] s = {"PERL5LIB=/Users/jan/Projekte/rex/lib", "PATH=/Users/jan/Projekte/rex/bin:/Users/jan/perl5/perlbrew/perls/perl-5.14.2/bin:/usr/bin:/usr/local/bin:/bin:/opt/local"};
					//String[] s = {};
					
					Process p = run.exec("rex -m " + task_name, s, AnyMenu.getProjectPath().toFile());
					p.waitFor();
					BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
					String line;
					while( (line = buf.readLine()) != null) {
						System.out.println(line);
					}
				} catch(IOException ex) {
					// Display error message
					System.out.println(ex.toString());
				} catch(InterruptedException int_ex) {
					// Display error message
					System.out.println(int_ex.toString());
				}
				
			}
		};
	}
	
	static public IProject getProject() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	    if (window != null) {
	        IStructuredSelection selection = (IStructuredSelection) window.getSelectionService().getSelection();
	        Object firstElement = selection.getFirstElement();
	        if (firstElement instanceof IProject) {
	            IProject project = (IProject)((IAdaptable)firstElement).getAdapter(IProject.class);
	            return project;
	        }
	    }
	    
	    return null;
	}
	
	static public IPath getProjectPath() {

		IProject project = AnyMenu.getProject();
		if(project != null) {
            IPath path = project.getLocation();
	        return path;
	    }
	
	    return null;
	}
	
	static public boolean isProjectSelected() {
		IProject project = AnyMenu.getProject();
		if(project != null) {
			return true;
	    }
	    
	    return false;
	}
	
	static public void openEditor(IFile file) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			IDE.openEditor(page, file);
		} catch(PartInitException ex) {
			// Display error message
			System.out.println(ex.toString());
		}
	}
}
