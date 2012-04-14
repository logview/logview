package com.github.logview.matcher;

import java.util.Collection;
import java.util.List;

import com.github.logview.value.api.ValueFactory;
import com.google.common.collect.ImmutableList;

public class Match {
	private final ValueFactory factory;
	private final String match;
	private final String source;
	private final List<Object> data;

	public Match(ValueFactory factory, String match, String source) {
		this.factory = factory;
		this.match = match;
		this.source = source;
		this.data = ImmutableList.of();
	}

	public Match(ValueFactory factory, String match, String source, Collection<Object> data) {
		this.factory = factory;
		this.match = match;
		this.source = source;
		this.data = ImmutableList.copyOf(data);
	}

	public String getMatch() {
		return match;
	}

	public Object getValue(int index) {
		return data.get(index);
	}

	public int size() {
		return data.size();
	}

	@Override
	public String toString() {
		return factory.toString(match, data);
	}

	public String toString(int index) {
		return factory.getType(index, match).format(data.get(index));
	}

	public String getSource() {
		return source;
	}
}
