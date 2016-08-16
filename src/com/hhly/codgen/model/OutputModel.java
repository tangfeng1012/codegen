package com.hhly.codgen.model;

public class OutputModel {
	
	private String name;
	
	private InOutType type;
	
	private TemplateModel templateModel;
	
	private String output; //输出路径
	
	private Boolean disabled;//是否禁用
	
	public OutputModel(String name) {
		// TODO Auto-generated constructor stub
		this.name = name;
	}

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

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public TemplateModel getTemplateModel() {
		return templateModel;
	}

	public void setTemplateModel(TemplateModel templateModel) {
		this.templateModel = templateModel;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}
	
	
}
