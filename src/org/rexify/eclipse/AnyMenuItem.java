package org.rexify.eclipse;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
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
		if(AnyMenu.isProjectSelected()) {
			if(! AnyMenu.getProject().getFile("Rexfile").exists()) {
				MenuItem create_rexfile = new MenuItem(menu, SWT.NORMAL, index);
				create_rexfile.setText("Create Rexfile");
				create_rexfile.addSelectionListener(AnyMenu.getCreateRexfileAction());
			}
			else {
				// populating menu from rex tasks
				Runtime run = Runtime.getRuntime();
				try {
					// if rex is installed via cpan this is not needed.
					String[] s = {"PERL5LIB=/Users/jan/Projekte/rex/lib", "PATH=/Users/jan/Projekte/rex/bin:/Users/jan/perl5/perlbrew/perls/perl-5.14.2/bin:/usr/bin:/usr/local/bin:/bin:/opt/local"};
					//String[] s = {};
					
					Process p = run.exec("rex -mTq", s, AnyMenu.getProjectPath().toFile());
					p.waitFor();
					BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
					String line;
					while( (line = buf.readLine()) != null) {
						// split lines
						String[] splitted_line = line.split(" = ");
						String task_name = splitted_line[0].substring(1, splitted_line[0].length()-1);
						
						MenuItem task_item = new MenuItem(menu, SWT.NORMAL, index);
						task_item.setText("Run Task: " + task_name);
						task_item.addSelectionListener(AnyMenu.getRunTaskAction(task_name));
					}
				} catch(IOException ex) {
					// Display error message
					System.out.println(ex.toString());
				} catch(InterruptedException int_ex) {
					// Display error message
					System.out.println(int_ex.toString());
				}
			}
		}
	}
}
