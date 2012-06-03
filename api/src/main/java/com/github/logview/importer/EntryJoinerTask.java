package com.github.logview.importer;

import java.util.List;
import java.util.Map.Entry;

import com.github.logview.api.LogEntry;
import com.github.logview.io.Reader;
import com.github.logview.io.Writer;
import com.github.logview.matcher.Match;
import com.github.logview.task.AbstractTask;
import com.google.common.collect.Lists;

public class EntryJoinerTask extends AbstractTask {
	private final List<String> lines = Lists.newLinkedList();
	private final Reader<Match> matches;
	private final Reader<String> extras;
	private final Writer<LogEntry> writer;
	private Entry<Long, Match> current;
	private long counter;
	private int step = 1;

	public EntryJoinerTask(Reader<Match> matches, Reader<String> extras, Writer<LogEntry> writer) {
		this.matches = matches;
		this.extras = extras;
		this.writer = writer;
	}

	private void write(Match match) {
		writer.write(counter, new LogEntry(counter, match, lines));
		lines.clear();
		current = null;
		step = 1;
		counter++;
	}

	@Override
	public boolean actionDo() {
		if(current == null) {
			current = matches.read();
			if(current == null) {
				if(extras.isFull()) {
					StringBuilder sb = new StringBuilder("extras list is full and no matches found!\nfirst 10 extras:");
					for(int i = 0; i < 10; i++) {
						sb.append(String.format("\n%d ", i + 1));
						sb.append(extras.readValue());
					}
					sb.append('\n');
					throw new RuntimeException(sb.toString());
				}
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
