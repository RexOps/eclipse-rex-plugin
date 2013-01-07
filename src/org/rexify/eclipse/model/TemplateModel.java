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
package org.rexify.eclipse.model;

public class TemplateModel {
	protected String name;
	protected String template_code;
	
	public TemplateModel(String _name, String _template_code) {
		name = _name;
		template_code = _template_code;
	}
	
	public String getName() {
		return name;
	}
	
	public String getTemplateCode() {
		return template_code;
	}
}
