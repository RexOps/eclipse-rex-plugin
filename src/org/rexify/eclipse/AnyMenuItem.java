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
package org.rexify.eclipse;


import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.rexify.eclipse.helper.CommandLine;
import org.rexify.eclipse.helper.EclipseHelper;
import org.rexify.eclipse.popup.actions.AnyMenu;

public class AnyMenuItem extends ContributionItem {
	public AnyMenuItem() {
	}

	public AnyMenuItem(String id) {
		super(id);
	}

	@Override
	public boolean isDynamic() {
		return true;
	}
	
	@Override
	public void fill(Menu menu, int index) {
		if(EclipseHelper.isProjectSelected()) {
			if(! EclipseHelper.getProject().getFile("Rexfile").exists()) {
				MenuItem create_rexfile = new MenuItem(menu, SWT.NORMAL, index);
				create_rexfile.setText("Create Rexfile");
				create_rexfile.addSelectionListener(AnyMenu.getCreateRexfileAction());
			}
			else {
				ArrayList<String> cmd = new ArrayList<String>();
				
				if(System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
//					cmd.add("cmd");
//					cmd.add("/C");
//					cmd.add("rex.bat -mTq");
					cmd.add("rex.bat");
					cmd.add("-mTq");
				}
				else {
					cmd.add("/bin/sh");
					cmd.add("-c");
					cmd.add("rex -mTq");
				}
				
				System.out.println("Want to run: " + cmd.get(0));
				
				ArrayList<String> output = (ArrayList<String>)CommandLine.run(cmd, EclipseHelper.getProjectPath().toFile());
				
				if(output != null) {
					Iterator<String> iter = output.iterator();
					while(iter.hasNext()) {
						String line = iter.next();
								
						String[] splitted_line = line.split(" = ");
						String task_name = splitted_line[0].substring(1, splitted_line[0].length()-1);
						
						MenuItem task_item = new MenuItem(menu, SWT.NORMAL, index);
						task_item.setText("Run Task: " + task_name);
						task_item.addSelectionListener(AnyMenu.getRunTaskAction(task_name));
					}
				}
				else {
					// display error message
				}
			}
		}
	}
}
