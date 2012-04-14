package com.github.logview.matcher;

import java.util.regex.Pattern;

import com.github.logview.value.api.ValueFactory;

public class ValueMatcher implements Matcher {
	private final ValueFactory factory;
	private final String match;
	private final Pattern pattern;

	public ValueMatcher(ValueFactory factory, String match) {
		this.factory = factory;
		this.match = match;
		pattern = factory.getPattern(match);
	}

	@Override
	public Match match(String line) {
		return factory.parse(pattern, match, line);
	}
}
