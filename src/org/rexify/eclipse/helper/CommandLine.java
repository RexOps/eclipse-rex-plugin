package org.rexify.eclipse.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.console.MessageConsoleStream;

public class CommandLine {

	static public List<String> run(List<String> cmd, File wd, MessageConsoleStream out) {
		List<String> ret = new ArrayList<String>();
		
		System.out.println("running command: " + cmd.get(0));
		
		try {
			ProcessBuilder builder = new ProcessBuilder(cmd);
			builder.directory(wd);
			Process process = builder.start();
			
			BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader err_buf = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			
			String line;

			while( (line = err_buf.readLine()) != null) {
				System.out.println("Error: " + line);

				if(out != null) {
					out.println("ERROR: " + line);
				}
			}

			err_buf.close();

			
			while( (line = buf.readLine()) != null) {
				System.out.println(">> " + line);
				ret.add(line);
				if(out != null) {
					out.println(line);
				}
			}
			
			buf.close();


			process.waitFor();

			return ret;
			
		} catch(IOException io_ex) {
			System.out.println(io_ex);
		} catch(InterruptedException int_ex) {
			// Display error message
			System.out.println(int_ex.toString());
		}
		
		return null;
	}
	
	static public List<String> run(List<String> cmd, File wd) {
		return run(cmd, wd, null);
	}

}
