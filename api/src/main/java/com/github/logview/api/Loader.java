package com.github.logview.api;

import java.io.File;

public interface Loader extends Appender {
	LogFile load(File file, long date);
}
