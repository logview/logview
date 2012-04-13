package com.github.logview.api;

import com.github.logview.matcher.Match;

public interface Tags {
	Match getTag(String tag);

	Match getTags(String tags);

	void close();
}
