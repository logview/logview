package com.github.logview.api;

import com.github.logview.matcher.Match;


public interface LineAppender {
	void append(Match data);
}
