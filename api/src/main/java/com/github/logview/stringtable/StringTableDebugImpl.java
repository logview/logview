package com.github.logview.stringtable;

import java.util.Map.Entry;
import java.util.Set;

import com.github.logview.store.Store;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class StringTableDebugImpl implements StringTable {
	private final BiMap<Integer, String> strings = HashBiMap.create();
	private final BiMap<String, Integer> inverse = strings.inverse();

	@Override
	public int addString(String string) {
		synchronized(strings) {
			Integer ret = inverse.get(string);
			if(ret == null) {
				ret = string.hashCode();
				strings.put(ret, string);
			}
			return ret;
		}
	}

	@Override
	public void touchString(String string) {
	}

	@Override
	public void touchString(int id) {
	}

	@Override
	public String getString(int id) {
		synchronized(strings) {
			return strings.get(id);
		}
	}

	@Override
	public void flush() {
	}

	public Set<String> getStrings() {
		synchronized(strings) {
			return strings.values();
		}
	}

	@Override
	public void listStrings() {
		synchronized(strings) {
			for(Entry<Integer, String> key : strings.entrySet()) {
				System.err.printf("%d: '%s'\n", key.getKey(), key.getValue());
			}
		}
	}

	@Override
	public void store(Store store) {
		synchronized(strings) {
			store.storeInteger(strings.size());
			for(Entry<Integer, String> key : strings.entrySet()) {
				store.storeInteger(key.getKey());
				store.storeString(key.getValue());
			}
		}
	}
}
