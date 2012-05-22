package com.github.logview.api;

import java.util.Map;

public interface Tags {
	void add(String tag, Map<String, Object> extra);

	void close();
}
