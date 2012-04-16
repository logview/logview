package com.github.logview.io;

import java.util.Map.Entry;

public interface Reader<T> {
	Entry<Long, T> read();

	Long firstKey();

	T readValue();

	boolean isFull();
}
