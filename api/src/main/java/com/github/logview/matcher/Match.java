package com.github.logview.matcher;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;

public class Match {
	private final String match;
	private final List<Object> data;

	public Match(String match) {
		this.match = match;
		this.data = ImmutableList.of();
	}

	public Match(String match, Collection<Object> data) {
		this.match = match;
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
}
