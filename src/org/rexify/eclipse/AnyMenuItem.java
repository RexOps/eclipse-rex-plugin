package org.rexify.eclipse;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
				List<String> cmd = new ArrayList<String>();
				
				if(System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
					cmd.add("cmd");
					cmd.add("/C");
					cmd.add("rex.bat -mTq");
				}
				else {
					cmd.add("/bin/sh");
					cmd.add("-c");
					cmd.add("rex -mTq");
				}
				
				try {
					ProcessBuilder builder = new ProcessBuilder(cmd);
					builder.directory(AnyMenu.getProjectPath().toFile());
					Process process = builder.start();
					
					BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
					BufferedReader err_buf = new BufferedReader(new InputStreamReader(process.getErrorStream()));
					
					String line;
					
					while( (line = buf.readLine()) != null) {
						System.out.println(line);
						
						// split lines
						String[] splitted_line = line.split(" = ");
						String task_name = splitted_line[0].substring(1, splitted_line[0].length()-1);
						
						MenuItem task_item = new MenuItem(menu, SWT.NORMAL, index);
						task_item.setText("Run Task: " + task_name);
						task_item.addSelectionListener(AnyMenu.getRunTaskAction(task_name));
					}
					
					buf.close();

					while( (line = err_buf.readLine()) != null) {
						System.out.println("ERR: " + line);
					}

					err_buf.close();

					int result = process.waitFor();
					System.out.println("Result: " + result);

				} catch(IOException io_ex) {
					System.out.println(io_ex);
				} catch(InterruptedException int_ex) {
					// Display error message
					System.out.println(int_ex.toString());
				}


				// populating menu from rex tasks
				/*Runtime run = Runtime.getRuntime();
				try {
					// if rex is installed via cpan this is not needed.
					//String[] s = {"PERL5LIB=/Users/jan/Projekte/rex/lib", "PATH=/Users/jan/Projekte/rex/bin:/Users/jan/perl5/perlbrew/perls/perl-5.14.2/bin:/usr/bin:/usr/local/bin:/bin:/opt/local"};
					//String[] s = {"PATH=C:\\strawberry\\perl\\bin;C:\\strawberry\\perl\\site\\bin"};
					String[] s = {};
					String[] cmd = {"C:\\strawberry\\perl\\bin\\perl.exe", "c:/test.pl"};
					
					Process p = run.exec(cmd); //, s, AnyMenu.getProjectPath().toFile());
					
					BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
					BufferedReader err_buf = new BufferedReader(new InputStreamReader(p.getErrorStream()));
					
					String line;
					
					while( (line = buf.readLine()) != null) {
						System.out.println(line);
					}
					
					buf.close();

					while( (line = err_buf.readLine()) != null) {
						System.out.println("ERR: " + line);
					}

					err_buf.close();
					
					int result = p.waitFor();
					System.out.println("Result: " + result);

					
					/*
						// split lines
						String[] splitted_line = line.split(" = ");
						String task_name = splitted_line[0].substring(1, splitted_line[0].length()-1);
						
						MenuItem task_item = new MenuItem(menu, SWT.NORMAL, index);
						task_item.setText("Run Task: " + task_name);
						task_item.addSelectionListener(AnyMenu.getRunTaskAction(task_name));
					}
	*** /				
				} catch(IOException ex) {
					// Display error message
					System.out.println(ex.toString());
				} catch(InterruptedException int_ex) {
					// Display error message
					System.out.println(int_ex.toString());
				} */
			}
		}
	}
}
