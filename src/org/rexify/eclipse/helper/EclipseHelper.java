/**
 * Eclipse Plugin to run Rex Tasks from within Eclipse.
 *
 * Copyright (C) 2013  Jan Gehring <jan.gehring@gmail.com>
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
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
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.ide.IDE;

public class EclipseHelper {

	static public MessageConsole findConsole(String name) {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager console_manager = plugin.getConsoleManager();
		IConsole[] existing_consoles = console_manager.getConsoles();
		for (int i = 0; i < existing_consoles.length; i++)
			if(name.equals(existing_consoles[i].getName()))
				return (MessageConsole) existing_consoles[i];
		
		MessageConsole console = new MessageConsole(name, null);
		console_manager.addConsoles(new IConsole[] { console });
		console_manager.showConsoleView(console);
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
