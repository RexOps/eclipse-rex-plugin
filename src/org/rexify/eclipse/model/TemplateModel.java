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
