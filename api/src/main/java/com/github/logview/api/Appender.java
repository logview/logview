package com.github.logview.api;

public interface Appender {
	void append(LogFile logfile, LogEntry line);

	void save(LogFile logfile);
}
