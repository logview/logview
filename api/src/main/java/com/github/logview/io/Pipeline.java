package com.github.logview.io;

import java.util.Map.Entry;
import java.util.TreeMap;

import com.google.common.collect.Maps;

public class Pipeline<T> implements Reader<T>, Writer<T> {
	private final TreeMap<Long, T> list = Maps.newTreeMap();
	private final int max;
	private final String name;

	protected Pipeline(int max, String name) {
		this.max = max;
		this.name = name;
	}

	public static <T> Pipeline<T> create(String name) {
		return new Pipeline<T>(100000, name);
	}

	public static <T> Pipeline<T> create(int max, String name) {
		return new Pipeline<T>(max, name);
	}

	@Override
	public Entry<Long, T> read() {
		synchronized(list) {
			Entry<Long, T> ret = list.pollFirstEntry();
			list.notifyAll();
			return ret;
		}
	}

	@Override
	public void write(Entry<Long, T> t) {
		write(t.getKey(), t.getValue());
	}

	@Override
	public void write(long id, T t) {
		synchronized(list) {
			if(isFull()) {
				try {
					/*
					System.err.println("awaiting " + name + " start");
					*/
					list.wait();
					/*
					System.err.println("awaiting " + name + " done");
					*/
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

	@Override
	public boolean isFull() {
		synchronized(list) {
			return list.size() >= max;
		}
	}

	public void close() {
		synchronized(list) {
			list.clear();
			list.notifyAll();
		}
	}

	public String getName() {
		return name;
	}
}
