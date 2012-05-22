package com.github.logview.io;

public interface Pipeline<T> extends Reader<T>, Writer<T> {
	void close();

	int size();
}
