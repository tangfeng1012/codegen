package com.hhly.codgen.model;

public class TemplateModel {
	
	private String name;
	
	private InOutType type;
	
	private String template;//Ä£°åÂ·¾¶(/template/AOWork)

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public InOutType getType() {
		return type;
	}

	public void setType(InOutType type) {
		this.type = type;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}
	
}
