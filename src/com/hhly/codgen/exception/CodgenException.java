package com.hhly.codgen.exception;

public class CodgenException extends Exception{
	
	public CodgenException() {
		// TODO Auto-generated constructor stub
		super("Codgen Exception occurs ...");
	}
	
	public CodgenException(String message) {
		// TODO Auto-generated constructor stub
		super(message);
	}
	
	public CodgenException(String message, Throwable cause) {
		// TODO Auto-generated constructor stub
		super(message,cause);
	}
}
