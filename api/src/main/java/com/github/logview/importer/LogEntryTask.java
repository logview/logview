package com.github.logview.importer;

import java.util.List;

import com.github.logview.api.LogEntry;
import com.github.logview.task.AbstractReaderTask;
import com.google.common.collect.Lists;

public class LogEntryTask extends AbstractReaderTask<LogEntry> {
	private final List<LogEntry> entrys = Lists.newLinkedList();

	@Override
	public boolean actionDo() {
		while(true) {
			final LogEntry e = getReader().readValue();
			if(e == null) {
				return false;
			}
			entrys.add(e);
		}
	}

	@Override
	public void actionFinished() {
		System.err.printf("loaded %d entrys\n", entrys.size());
	}

	@Override
	public String getName() {
		return "log entry";
	}

	public List<LogEntry> getEntrys() {
		return entrys;
	}
}
