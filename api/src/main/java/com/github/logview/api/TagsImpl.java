package com.github.logview.api;

import java.util.Map;
import java.util.Map.Entry;

import com.github.logview.tagger.AbstractTagger;
import com.github.logview.tagger.ExceptionTagger;
import com.github.logview.tagger.Tagger;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class TagsImpl implements Tags {
	private final String prefix;

	private final LoadingCache<String, Tagger> tagCache = CacheBuilder.newBuilder().build(
		new CacheLoader<String, Tagger>() {
			@Override
			public Tagger load(String tag) throws Exception {
				/*
				if(!"user".equals(prefix)) {
					System.err.printf("new tag: '%s:%s'\n", prefix, tag);
				}
				*/
				if("exception".equals(prefix)) {
					return new ExceptionTagger(tag);
				}
				return new AbstractTagger(prefix, tag);
			}
		});

	public TagsImpl(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public void close() {
		System.err.printf("%d %s tags...\n", tagCache.size(), prefix);
		if("user".equals(prefix)) {
			System.err.println();
			return;
		}
		for(Entry<String, Tagger> e : tagCache.asMap().entrySet()) {
			e.getValue().close();
		}
		System.err.println();
	}

	@Override
	public void add(String tag, Map<String, Object> extra) {
		Tagger tagger;
		synchronized(tagCache) {
			tagger = tagCache.getUnchecked(tag);
		}
		tagger.add(extra);
	}
}
