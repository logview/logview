package com.github.logview.importer;

import java.util.Map.Entry;

import com.github.logview.api.Appender;
import com.github.logview.api.LogEntry;
import com.github.logview.api.LogFile;
import com.github.logview.io.Reader;
import com.github.logview.task.Task;

public class LogEntryTask extends Task {
	private final Reader<LogEntry> entrys;
	private final Appender appender;
	private final LogFile logfile;

	public LogEntryTask(Reader<LogEntry> entrys, Appender appender, LogFile logfile) {
		this.entrys = entrys;
		this.appender = appender;
		this.logfile = logfile;
	}

	@Override
	protected boolean actionDo() {
		while(true) {
			final Entry<Long, LogEntry> m = entrys.read();
			if(m == null) {
				return false;
			}
			/*
			long key = m.getKey();
			*/
			LogEntry value = m.getValue();
			appender.append(logfile, value);
			/*
			if(key % 100000 == 0) {
				System.err.printf("%6.1fm %s", key / 1000.0 / 1000.0, value);
			}
			*/
		}
	}

	@Override
	public String getName() {
		return "log entry";
	}
}
