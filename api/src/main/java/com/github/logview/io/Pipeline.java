package com.github.logview.io;

import java.util.Map.Entry;
import java.util.TreeMap;

import com.google.common.collect.Maps;

public class Pipeline<T> implements Reader<T>, Writer<T> {
	private final TreeMap<Long, T> list = Maps.newTreeMap();
	private final int max;

	protected Pipeline(int max) {
		this.max = max;
	}

	public static <T> Pipeline<T> create() {
		return new Pipeline<T>(100000);
	}

	public static <T> Pipeline<T> create(int max) {
		return new Pipeline<T>(max);
	}

	@Override
	public Entry<Long, T> read() {
		synchronized(list) {
			list.notifyAll();
			return list.pollFirstEntry();
		}
	}

	@Override
	public void write(Entry<Long, T> t) {
		write(t.getKey(), t.getValue());
	}

	@Override
	public void write(long id, T t) {
		synchronized(list) {
			if(list.size() >= max) {
				try {
					list.wait();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
			list.put(id, t);
		}
	}

	public int size() {
		synchronized(list) {
			return list.size();
		}
	}

	@Override
	public Long firstKey() {
		synchronized(list) {
			if(list.isEmpty()) {
				return null;
			}
			return list.firstKey();
		}
	}

	@Override
	public T readValue() {
		Entry<Long, T> ret = read();
		if(ret == null) {
			return null;
		}
		return ret.getValue();
	}
}
