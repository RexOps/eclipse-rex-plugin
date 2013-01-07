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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CommandLine {

	static public List<String> run(List<String> cmd, File wd) {
		List<String> ret = new ArrayList<String>();
		
		try {
			ProcessBuilder builder = new ProcessBuilder(cmd);
			builder.directory(wd);
			Process process = builder.start();
			
			BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader err_buf = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			
			String line;

			while( (line = buf.readLine()) != null) {
				System.out.println(line);
				ret.add(line);
			}
			
			buf.close();

			while( (line = err_buf.readLine()) != null) {
				System.out.println("Error: " + line);
			}

			err_buf.close();

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
	
}
