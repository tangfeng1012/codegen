package com.hhly.codgen.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class ExceptionUtils {
	
	/**
	 * @category 打印异常的信息
	 * @param e
	 * @return
	 */
	public static String printException(Throwable e){
		StringWriter writer = null;
		PrintWriter pw = null;
		try {
			if(e != null){
				writer = new StringWriter();
				pw = new PrintWriter(writer, true);
				e.printStackTrace(pw);
				return writer.toString();
			}
		} catch (Exception e2) {
			// TODO: handle exception
			e2.printStackTrace();
		}finally {
			safelyClose(pw);
			safelyClose(writer);
		}
		return "";
	}

	
	
	public static void safelyClose(Writer writer){
		try {
			if(writer != null){
				writer.close();
				writer = null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
