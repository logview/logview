package com.github.logview.matcher;

import com.github.logview.value.api.ValueFactory;

public class ValueMatcher implements Matcher {
	private final ValueFactory factory;
	private final String match;

	public ValueMatcher(ValueFactory factory, String match) {
		this.factory = factory;
		this.match = match;
	}

	@Override
	public Match match(String line) {
		return factory.parse(match, line);
	}
}
