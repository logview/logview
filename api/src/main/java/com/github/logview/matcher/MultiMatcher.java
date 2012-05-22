package com.github.logview.matcher;

import java.util.List;
import java.util.Properties;

import com.github.logview.store.Store;
import com.github.logview.value.api.ValueFactory;
import com.google.common.collect.Lists;

public class MultiMatcher implements LineMatcher {
	protected final String key;
	protected final ValueFactory factory;
	private final List<PatternMatcher> matchers = Lists.newLinkedList();

	public MultiMatcher(ValueFactory factory, String key) {
		this.factory = factory;
		this.key = key;
	}

	public MultiMatcher(ValueFactory factory, String key, Properties props) {
		this(factory, key);
		load(key, props);
	}

	public MultiMatcher(ValueFactory factory, String key, Properties manual, Properties auto) {
		this(factory, key);
		load(key, manual);
		load(key, auto);
	}

	public MultiMatcher load(String key, Properties props) {
		for(int i = 0;; i++) {
			String match = props.getProperty(key + "." + i);
			if(match == null) {
				break;
			}
			add(factory.getPatternMatcher(match, true));
		}
		return this;
	}

	@Override
	public Match match(String line) {
		synchronized(matchers) {
			for(LineMatcher matcher : matchers) {
				Match ret = matcher.match(line);
				if(ret != null) {
					return ret;
				}
			}
		}
		return null;
	}

	public boolean add(PatternMatcher match) {
		synchronized(matchers) {
			return matchers.add(match);
		}
	}

	@Override
	public void store(Store store) {
		synchronized(matchers) {
			store.storeInteger(matchers.size());
			for(LineMatcher matcher : matchers) {
				matcher.store(store);
			}
		}
	}
}
