package com.github.logview.importer;

import java.util.Map.Entry;

import com.github.logview.api.LogEntry;
import com.github.logview.io.Pipeline;
import com.github.logview.io.Pipelines;
import com.github.logview.matcher.LineMatcher;
import com.github.logview.matcher.Match;
import com.github.logview.task.ReaderTask;

public class MultiThreadLoader extends AbstractLoader {
	private final Pipeline<String> strings = Pipelines.create("strings");
	private final Pipeline<String> extrasCache = Pipelines.create("extrasCache");
	private final Pipeline<Match> matchesCache = Pipelines.create("matchesCache");
	private final Pipeline<String> extrasOrdered = Pipelines.create("extrasOrdered");
	private final Pipeline<Match> matchesOrdered = Pipelines.create("matchesOrdered");
	private final Pipeline<LogEntry> tag = Pipelines.create("tag");
	private final Pipeline<LogEntry> entrys = Pipelines.create("entry");

	public MultiThreadLoader(LineMatcher matcher, ReaderTask<LogEntry> reader) {
		reader.setReader(entrys);
		manager.add(new MatcherTask(strings, matchesCache, extrasCache, matcher), 8);
		manager.add(new EntryOrdererTask(matchesCache, extrasCache, matchesOrdered, extrasOrdered), 1);
		manager.add(new EntryJoinerTask(matchesOrdered, extrasOrdered, tag), 1);
		manager.add(new TaggerTask(tag, entrys), 8);
		manager.add(reader, 1);
	}

	@Override
	public void notifyAborted() {
		strings.close();
		extrasCache.close();
		matchesCache.close();
		extrasOrdered.close();
		matchesOrdered.close();
		entrys.close();
	}

	public String getStatus() {
		return String.format("strings:%d extras:c%d/o%d matches:c%d/o%d\n%s", strings.size(), extrasCache.size(),
			extrasOrdered.size(), matchesCache.size(), matchesOrdered.size(), manager.getStatus());
	}

	@Override
	public void write(long id, String t) {
		strings.write(id, t);
	}

	@Override
	public void write(Entry<Long, String> t) {
		strings.write(t);
	}
}
