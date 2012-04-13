package com.github.logview.api;

import com.github.logview.matcher.Match;
import com.github.logview.matcher.ValueMatcher;
import com.github.logview.value.api.ValueFactory;
import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class TagsImpl implements Tags {
	private final ValueFactory factory;
	private final ValueMatcher patterns;
	private final String prefix;

	private final LoadingCache<String, Match> tagCache = CacheBuilder.newBuilder()// .maximumSize(10000)
			.build(new CacheLoader<String, Match>() {
				@Override
				public Match load(String tag) throws Exception {
					System.err.printf("new tag: %s:%s\n", prefix, tag);
					return new Match(tag);
				}
			});

	private final LoadingCache<String, Match> regexTagCache = CacheBuilder.newBuilder()// .maximumSize(10000)
			.build(new CacheLoader<String, Match>() {
				@Override
				public Match load(String tag) throws Exception {
					Match ret = patterns == null ? null : patterns.match(tag);
					if(ret == null) {
						return tagCache.get(tag);
					}
					return ret;
				}
			});

	private final LoadingCache<String, Match> tagsCache = CacheBuilder.newBuilder()// .maximumSize(10000)
			.build(new CacheLoader<String, Match>() {
				@Override
				public Match load(String tags) throws Exception {
					/*
					String[] ts = tags.split(" ");
					TagListPart ret = new TagListPart(patterns, manager);
					for(int i = 0; i < ts.length; i++) {
						ret.add(regexTagCache.get(ts[i]));
					}
					/*
					byte[] b = ret.getBytes(factory);
					if(tags.length() < b.length - 1) {
						System.err.printf("new tag list: '%s' %d txt %d bytes [%s]\n[%s]\n", tags, tags.length(),
							b.length, ret.debug(), Util.toHex(b));
					}
					*/
					return null;
				}
			});

	public TagsImpl(ValueFactory factory, ValueMatcher patterns, String prefix) {
		this.factory = factory;
		this.patterns = patterns;
		this.prefix = prefix;
	}

	@Override
	public Match getTag(String tag) {
		if(Strings.isNullOrEmpty(tag)) {
			return null;
		}
		return tagCache.getUnchecked(tag);
	}

	@Override
	public Match getTags(String tags) {
		if(Strings.isNullOrEmpty(tags)) {
			return null;
		}
		if(patterns == null) {
			throw new IllegalStateException("no patterns given!");
		}
		return tagsCache.getUnchecked(tags);
	}

	@Override
	public void close() {
		System.err.printf("%d %s tags...\n", tagCache.size(), prefix);
	}
}
