package com.github.logview.io;

import java.util.Map.Entry;

public abstract class AbstractWriter<T> implements Writer<T> {
	@Override
	public void write(Entry<Long, T> t) {
		write(t.getKey(), t.getValue());
	}
}
