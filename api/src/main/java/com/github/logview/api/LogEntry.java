package com.github.logview.api;

import java.util.List;

import com.github.logview.matcher.Match;
import com.google.common.collect.ImmutableList;

public class LogEntry {
	private final long id;
	private final Match line;
	private final List<String> lines;

	public LogEntry(long id, Match line, List<String> lines) {
		this.id = id;
		this.line = line;
		this.lines = ImmutableList.copyOf(lines);
	}

	public Match getLine() {
		return line;
	}

	public List<String> getLines() {
		return ImmutableList.copyOf(lines);
	}

	public void add(String line) {
		lines.add(line);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(line.toString());
		sb.append('\n');
		for(String line : lines) {
			sb.append(line);
			sb.append('\n');
		}
		return sb.toString();
	}

	public long getId() {
		return id;
	}
}
