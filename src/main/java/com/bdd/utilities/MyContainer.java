package com.bdd.utilities;

import java.util.Hashtable;
import java.util.Map;

public class MyContainer {
	
	private static Map<Long, Object> containerMap = new Hashtable();
	
	public MyContainer() {
		
	}
	
	public static void putInstance(long l, Object obj) {
		
		containerMap.put(l, obj);
		
	}
	
	public static Object getInstance(long l) {
		
		Object obj = null;
		obj = containerMap.get(l);
		return obj;
		
	}
}
