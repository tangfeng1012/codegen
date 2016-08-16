package com.hhly.codgen.service;

import java.util.Map;

import com.hhly.codgen.exception.CodgenException;
import com.hhly.codgen.model.OutputModel;

public interface BuildConfig {
	/**
	 * ��ȡ������뷽ʽ
	 * @return
	 */
	public String getOutputEncoding();
	
	/**
	 * ��ȡ����ģ��
	 * @return
	 */
	public Map<String, Object> getDataModelMap();
	
	/**
	 * ��ȡ���ģ��
	 * @return
	 */
	public Map<String, OutputModel> getOutputModelMap();
}
