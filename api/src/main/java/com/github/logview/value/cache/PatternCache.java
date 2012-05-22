package com.github.logview.value.cache;

import java.util.regex.Pattern;

import com.github.logview.value.api.ValueFactory;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class PatternCache {
	private final static LoadingCache<String, Pattern> compileCache = CacheBuilder.newBuilder().weakValues()
			.build(new CacheLoader<String, Pattern>() {
				@Override
				public Pattern load(String regex) throws Exception {
					return Pattern.compile(regex);
				}
			});

	public final static Pattern getPattern(String pattern) {
		return compileCache.getUnchecked(pattern);
	}

	private final ValueFactory factory;

	public PatternCache(ValueFactory factory) {
		this.factory = factory;
	}

	private final LoadingCache<String, Pattern> patternCache = CacheBuilder.newBuilder().weakValues()
			.build(new CacheLoader<String, Pattern>() {
				@Override
				public Pattern load(String regex) throws Exception {
					return getPattern(factory.toRegex(regex, false));
				}
			});

	private final LoadingCache<String, Pattern> patternCacheEscape = CacheBuilder.newBuilder().weakValues()
			.build(new CacheLoader<String, Pattern>() {
				@Override
				public Pattern load(String regex) throws Exception {
					return getPattern(factory.toRegex(regex, true));
				}
			});

	public Pattern getPattern(String match, boolean escape) {
		if(escape) {
			return patternCacheEscape.getUnchecked(match);
		} else {
			return patternCache.getUnchecked(match);
		}
	}
}
