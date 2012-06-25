package com.github.logview.importer;

import java.util.ArrayList;
import java.util.LinkedList;

import com.github.logview.api.LogEntry;
import com.github.logview.task.AbstractReaderTask;
import com.google.common.collect.Lists;

public class LogEntryTask extends AbstractReaderTask<LogEntry> {
	protected final LinkedList<LogEntry> entrys = Lists.newLinkedList();

	@Override
	public boolean actionDo() {
		while(true) {
			final LogEntry e = getReader().readValue();
			if(e == null) {
				return false;
			}
			entrys.add(e);
			// System.err.println("add: " + e.getId());
			update();
		}
	}

	protected void update() {
	}

	@Override
	public void actionFinished() {
		System.err.printf("loaded %d entrys\n", entrys.size());
	}

	@Override
	public String getName() {
		return "log entry";
	}

	public ArrayList<LogEntry> getEntrys() {
		if(entrys.size() == 0) {
			throw new RuntimeException("");
		}
		ArrayList<LogEntry> ret = Lists.newArrayList(entrys);
		entrys.clear();
		return ret;
	}
}
