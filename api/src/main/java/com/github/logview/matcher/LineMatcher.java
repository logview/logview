package com.github.logview.matcher;

import com.github.logview.store.Storeable;

public interface LineMatcher extends Storeable {
	Match match(String line);
}
