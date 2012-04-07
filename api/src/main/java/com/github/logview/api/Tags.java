package com.github.logview.api;

import com.github.logview.stringpart.api.Part;

public interface Tags {
	Part getTag(String tag);

	Part getTags(String tags);

	void close();
}
