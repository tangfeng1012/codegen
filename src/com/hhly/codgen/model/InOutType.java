package com.hhly.codgen.model;

public enum InOutType {
	
	FILE("file","�ļ�"),
	
	TEXT("text","�ı�");
	
	private InOutType(String type, String desc) {
		// TODO Auto-generated constructor stub
		this.lable = type;
		this.value = desc;
	}
	
	private final String lable;
	private final String value;
}
