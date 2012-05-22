package com.github.logview.importer;

import java.util.Map.Entry;

import com.github.logview.io.Reader;
import com.github.logview.io.Writer;
import com.github.logview.matcher.LineMatcher;
import com.github.logview.matcher.Match;
import com.github.logview.task.AbstractTask;

public class MatcherTask extends AbstractTask {
	private final Reader<String> input;
	private final Writer<String> output;
	private final Writer<Match> matches;
	private final LineMatcher matcher;

	public MatcherTask(Reader<String> input, Writer<Match> matches, Writer<String> output, LineMatcher matcher) {
		this.input = input;
		this.matches = matches;
		this.output = output;
		this.matcher = matcher;
	}

	@Override
	public boolean actionDo() {
		while(true) {
			Entry<Long, String> entity = input.read();
			if(entity == null) {
				return false;
			}
			Match m = matcher.match(entity.getValue());
			if(m == null) {
				output.write(entity);
			} else {
				matches.write(entity.getKey(), m);
			}
		}
	}

	@Override
	public String getName() {
		return "matcher";
	}
}
