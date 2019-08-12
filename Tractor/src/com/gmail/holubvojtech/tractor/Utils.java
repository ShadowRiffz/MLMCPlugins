package com.gmail.holubvojtech.tractor;

import java.util.Map;

public class Utils {
	public static <K, V> V getOrDefault(Map<K, V> map, K key, V def) {
		System.out.println("Start get or default");
		V value = map.get(key);
		System.out.println("value: " + value);
		if (value == null) {
			System.out.println("End get or default");
			return def;
		}
		System.out.println("End get or default with value");
		return value;
	}
}
