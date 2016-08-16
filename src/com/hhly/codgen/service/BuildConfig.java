package com.hhly.codgen.service;

import java.util.Map;

import com.hhly.codgen.exception.CodgenException;
import com.hhly.codgen.model.OutputModel;

public interface BuildConfig {
	/**
	 * 获取输出编码方式
	 * @return
	 */
	public String getOutputEncoding();
	
	/**
	 * 获取数据模型
	 * @return
	 */
	public Map<String, Object> getDataModelMap();
	
	/**
	 * 获取输出模型
	 * @return
	 */
	public Map<String, OutputModel> getOutputModelMap();
}
