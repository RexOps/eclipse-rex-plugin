package org.rexify.eclipse.helper;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.ide.IDE;

public class EclipseHelper {

	static public MessageConsole findConsole(String name) {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager console_manager = plugin.getConsoleManager();
		IConsole[] existing_consoles = console_manager.getConsoles();
		for(int i = 0; i < existing_consoles.length; i++)
			if(name.equals(existing_consoles[i].getName()))
				return (MessageConsole)existing_consoles[i];
		
		// no console found, create new one
		MessageConsole console = new MessageConsole(name, null);
		console_manager.addConsoles(new IConsole[]{ console });
		
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		String id = IConsoleConstants.ID_CONSOLE_VIEW;
		
		try {
			IConsoleView view = (IConsoleView)page.showView(id);
			view.display(console);
		} catch(PartInitException pex) {
			// Display error message
		}
		
		return console;
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

		IProject project = getProject();
		if(project != null) {
            IPath path = project.getLocation();
	        return path;
	    }
	
	    return null;
	}
	
	static public boolean isProjectSelected() {
		IProject project = getProject();
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
