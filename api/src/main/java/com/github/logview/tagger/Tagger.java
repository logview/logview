package com.github.logview.tagger;

import java.util.Map;

public interface Tagger {
	void close();

	int size();

	void add(Map<String, Object> extra);
}
