package com.github.logview.api;


public interface Appender {
	void append(LogFile logfile, Entry line) throws Exception;

	void save(LogFile logfile) throws Exception;
}
