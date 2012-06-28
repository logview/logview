package com.github.logview.importer;

import java.util.List;
import java.util.Properties;

import com.github.logview.matcher.Match;
import com.github.logview.matcher.MultiMatcher;
import com.github.logview.matcher.PatternMatcher;
import com.github.logview.value.api.ValueFactory;
import com.google.common.collect.Lists;

public class AnalyserMultiMatcher extends MultiMatcher {
	private final List<String> analysed = Lists.newLinkedList();

	public AnalyserMultiMatcher(ValueFactory factory, String key, Properties manual, Properties auto) {
		super(factory, key, manual, auto);
	}

	@Override
	public Match match(String line) {
		synchronized(this) {
			Match ret = super.match(line);
			if(ret == null) {
				PatternMatcher match = factory.getPatternMatcher(factory.analyse(line), true);
				add(match);
				analysed.add(match.match);
				return match.match(line);
			}
			return ret;
		}
	}

	public void store(StringBuilder sb) {
		if(analysed.isEmpty()) {
			return;
		}
		int i = 0;
		sb.append("\n");
		for(String match : analysed) {
			sb.append(key);
			sb.append('.');
			sb.append(i);
			sb.append(" = ");
			sb.append(match);
			sb.append("\n");
			i++;
		}
	}
}
