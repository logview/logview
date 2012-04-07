package com.github.logview.api;

import com.github.logview.stringpart.api.Part;

public interface Appender {
	void append(LogFile logfile, Part line) throws Exception;

	void save(LogFile logfile) throws Exception;
}
