package com.github.logview.matcher;

import java.util.List;
import java.util.Properties;

import com.github.logview.value.api.ValueFactory;
import com.google.common.collect.Lists;

public class MultiMatcher implements Matcher {
	private final ValueFactory factory;
	private final List<Matcher> matchers = Lists.newLinkedList();

	public MultiMatcher(ValueFactory factory, String key, Properties props, boolean escape) {
		this.factory = factory;
		for(int i = 0;; i++) {
			String match = props.getProperty(key + "." + i);
			if(match == null) {
				break;
			}
			add(match, escape);
		}
	}

	@Override
	public Match match(String line) {
		synchronized(matchers) {
			for(Matcher matcher : matchers) {
				Match ret = matcher.match(line);
				if(ret != null) {
					return ret;
				}
			}
		}
		return null;
	}

	public boolean add(String match, boolean escape) {
		synchronized(matchers) {
			return matchers.add(new ValueMatcher(factory, match, escape));
		}
	}
}
