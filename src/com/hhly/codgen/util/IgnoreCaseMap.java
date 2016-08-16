package com.hhly.codgen.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class IgnoreCaseMap<K,V> extends LinkedHashMap<K, V>{
	private Map<String, K> keyMap = new HashMap<>();
	
	public IgnoreCaseMap(){
	}
	
	public IgnoreCaseMap(Map<K, V> paramMap) {
		// TODO Auto-generated constructor stub
		super(paramMap);
		for(K k : paramMap.keySet()){
			keyMap.put(k.toString().toLowerCase(), k);
		}
	}
	
	@Override
	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		String keyStr = key.toString().toLowerCase();
		if(!keyMap.containsKey(keyStr)){
			return false;
		}
		K realKey = keyMap.get(keyStr);
		return super.containsKey(realKey);
	}
	
	public V put(K paramK, V paramV){
		if(paramK instanceof String){
			String keyStr = ((String)paramK).toLowerCase();
			keyMap.put(keyStr, paramK);
		}
		return super.put(paramK, paramV);
	}
	
	public V get(Object key){
		String keyStr = key.toString().toLowerCase();
		if(!containsKey(keyStr)){
			return null;
		}
		K realKey = keyMap.get(keyStr);
		return super.get(realKey);
	}
	
}
