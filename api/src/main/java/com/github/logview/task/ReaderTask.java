package com.github.logview.task;

import com.github.logview.io.Reader;

public interface ReaderTask<R> extends Task {
	Reader<R> getReader();

	void setReader(Reader<R> reader);
}
