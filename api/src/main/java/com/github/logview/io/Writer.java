package com.github.logview.io;

import java.util.Map.Entry;

public interface Writer<T> {
	void write(long id, T t);

	void write(Entry<Long, T> t);
}
