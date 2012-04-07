package com.github.logview.stringtable;

import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class StringTableDebugImpl extends StringTable {
	private final BiMap<Integer, String> strings = HashBiMap.create();
	private final BiMap<String, Integer> inverse = strings.inverse();

	@Override
	public int addString(String string) {
		Integer ret = inverse.get(string);
		if(ret == null) {
			ret = string.hashCode();
			strings.put(ret, string);
		}
		return ret;
	}

	@Override
	public void touchString(String string) {
	}

	@Override
	public void touchString(int id) {
	}

	@Override
	public String getString(int id) {
		return strings.get(id);
	}

	@Override
	public void flush() {
	}

	public Set<String> getStrings() {
		return strings.values();
	}

	@Override
	public void listStrings() {
		for(Entry<Integer, String> key : strings.entrySet()) {
			System.err.printf("%d: '%s'\n", key.getKey(), key.getValue());
		}
	}
}
