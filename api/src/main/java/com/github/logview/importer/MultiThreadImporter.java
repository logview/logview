package com.github.logview.importer;

import com.github.logview.api.Appender;
import com.github.logview.api.LogEntry;
import com.github.logview.api.LogFile;
import com.github.logview.io.Pipeline;
import com.github.logview.io.Writer;
import com.github.logview.matcher.Match;
import com.github.logview.matcher.Matcher;
import com.github.logview.task.TaskListener;
import com.github.logview.task.TaskManager;

public class MultiThreadImporter {
	private final TaskManager manager;
	private final Pipeline<String> strings = Pipeline.create("strings");
	private final Pipeline<String> extrasCache = Pipeline.create("extrasCache");
	private final Pipeline<Match> matchesCache = Pipeline.create("matchesCache");
	private final Pipeline<String> extrasOrdered = Pipeline.create("extrasOrdered");
	private final Pipeline<Match> matchesOrdered = Pipeline.create("matchesOrdered");
	private final Pipeline<LogEntry> entrys = Pipeline.create("entry");

	private final TaskListener aborted = new TaskListener() {
		@Override
		public void notifyAborted() {
			strings.close();
			extrasCache.close();
			matchesCache.close();
			extrasOrdered.close();
			matchesOrdered.close();
			entrys.close();
		}
	};

	public MultiThreadImporter(Matcher matcher, Appender appender, LogFile logfile) {
		this.manager = new TaskManager();
		manager.add(new MatcherTask(strings, matchesCache, extrasCache, matcher), 8);
		manager.add(new EntryOrdererTask(matchesCache, extrasCache, matchesOrdered, extrasOrdered), 1);
		manager.add(new EntryJoinerTask(matchesOrdered, extrasOrdered, entrys), 1);
		manager.add(new LogEntryTask(entrys, appender, logfile), 8);
		manager.addListener(aborted);
	}

	public void close() throws InterruptedException {
		manager.close();
	}

	public Writer<String> getWriter() {
		return strings;
	}

	public void addListener(TaskListener listener) {
		manager.addListener(listener);
	}

	public String getStatus() {
		return String.format("strings:%d extras:c%d/o%d matches:c%d/o%d\n%s", strings.size(), extrasCache.size(),
			extrasOrdered.size(), matchesCache.size(), matchesOrdered.size(), manager.getStatus());
	}
}
