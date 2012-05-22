package com.github.logview.value.cache;

import com.github.logview.matcher.PatternMatcher;
import com.github.logview.value.api.ValueFactory;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class PatternMatcherCache {
	private final ValueFactory factory;

	public PatternMatcherCache(ValueFactory factory) {
		this.factory = factory;
	}

	private final LoadingCache<String, PatternMatcher> patternCache = CacheBuilder.newBuilder().weakValues()
			.build(new CacheLoader<String, PatternMatcher>() {
				@Override
				public PatternMatcher load(String match) throws Exception {
					return new PatternMatcher(factory, match, false);
				}
			});

	private final LoadingCache<String, PatternMatcher> patternCacheEscape = CacheBuilder.newBuilder().weakValues()
			.build(new CacheLoader<String, PatternMatcher>() {
				@Override
				public PatternMatcher load(String match) throws Exception {
					return new PatternMatcher(factory, match, true);
				}
			});

	public PatternMatcher getPatternMatcher(String match, boolean escape) {
		if(escape) {
			return patternCacheEscape.getUnchecked(match);
		} else {
			return patternCache.getUnchecked(match);
		}
	}
}
