package com.github.logview.task;

import com.github.logview.io.Reader;

public abstract class AbstractReaderTask<R> extends AbstractTask implements ReaderTask<R> {
	private Reader<R> reader;

	@Override
	public Reader<R> getReader() {
		return reader;
	}

	@Override
	public void setReader(Reader<R> reader) {
		this.reader = reader;
	}
}
