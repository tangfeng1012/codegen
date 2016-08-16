package com.hhly.codgen.model;

public enum InOutType {
	
	FILE("file","文件"),
	
	TEXT("text","文本");
	
	private InOutType(String type, String desc) {
		// TODO Auto-generated constructor stub
		this.lable = type;
		this.value = desc;
	}
	
	private final String lable;
	private final String value;
}
