package com.github.logview.importer;

import java.util.List;
import java.util.Map.Entry;

import com.github.logview.api.LogEntry;
import com.github.logview.io.Reader;
import com.github.logview.io.Writer;
import com.github.logview.matcher.Match;
import com.github.logview.task.Task;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class EntryJoinerTask extends Task {
	private final List<String> emptyList = ImmutableList.of();
	private final List<String> lines = Lists.newLinkedList();
	private final Reader<Match> matches;
	private final Reader<String> extras;
	private final Writer<LogEntry> writer;
	private long counter = 0;
	private Entry<Long, Match> current;
	private int step = 1;

	public EntryJoinerTask(Reader<Match> matches, Reader<String> extras, Writer<LogEntry> writer) {
		this.matches = matches;
		this.extras = extras;
		this.writer = writer;
	}

	private void write(Match match) {
		write(match, emptyList);
	}

	private void write(Match match, List<String> lines) {
		writer.write(counter, new LogEntry(counter, match, lines));
		current = null;
		step = 1;
		counter++;
	}

	@Override
	protected boolean actionDo() {
		if(current == null) {
			current = matches.read();
			if(current == null) {
				return false;
			}
		}
		while(true) {
			final long id = current.getKey();
			final Match match = current.getValue();
			while(true) {
				Long a = matches.firstKey();
				Long b = extras.firstKey();
				if(a != null || b != null) {
					if(a != null && a == id + step) {
						write(match);
						return true;
					} else if(b != null && b == id + step) {
						lines.add(extras.readValue());
						step++;
						continue;
					}
				}
				return false;
			}
		}
	}

	@Override
	public int getMaxThreads() {
		return 1;
	}

	@Override
	public String getName() {
		return "entry joiner";
	}
}
