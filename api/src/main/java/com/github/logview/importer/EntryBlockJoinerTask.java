package com.github.logview.importer;

import java.util.List;

import com.github.logview.api.LogEntry;
import com.github.logview.io.Reader;
import com.github.logview.io.Writer;
import com.github.logview.task.AbstractTask;
import com.google.common.collect.Lists;

public class EntryBlockJoinerTask extends AbstractTask {
	private static final int SIZE = 5000;

	private final Reader<LogEntry> reader;
	private final Writer<List<LogEntry>> writer;
	private final List<LogEntry> cache = Lists.newArrayListWithCapacity(SIZE);
	private long counter;

	public EntryBlockJoinerTask(Reader<LogEntry> reader, Writer<List<LogEntry>> writer) {
		this.reader = reader;
		this.writer = writer;
	}

	@Override
	public boolean actionDo() {
		while(true) {
			LogEntry entry = reader.readValue();
			if(entry == null) {
				return false;
			}
			cache.add(entry);
			if(cache.size() >= SIZE) {
				writer.write(counter, cache);
				counter++;
				cache.clear();
			}
			continue;
		}
	}

	@Override
	public int getMaxThreads() {
		return 1;
	}

	@Override
	public String getName() {
		return "entry block joiner";
	}
}
