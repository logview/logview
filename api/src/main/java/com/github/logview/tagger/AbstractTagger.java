package com.github.logview.tagger;

import java.util.Map;

public class AbstractTagger implements Tagger {
	private final String prefix;
	private final String tag;

	public AbstractTagger(String prefix, String tag) {
		this.prefix = prefix;
		this.tag = tag;
	}

	@Override
	public void close() {
		System.err.printf("%s:%s (%d times)\n", prefix, tag, size());
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public void add(Map<String, Object> extra) {
	}
}
