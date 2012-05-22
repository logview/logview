package com.github.logview.value.cache;

import java.util.Map;

import com.github.logview.api.Tags;
import com.github.logview.api.TagsImpl;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class TagsCache {
	private final LoadingCache<String, Tags> tagCache = CacheBuilder.newBuilder().build(
		new CacheLoader<String, Tags>() {
			@Override
			public Tags load(String prefix) throws Exception {
				return new TagsImpl(prefix);
			}
		});

	public void add(String prefix, String tag, Map<String, Object> extra) {
		tagCache.getUnchecked(prefix).add(tag, extra);
	}

	public void close() {
		for(Tags tags : tagCache.asMap().values()) {
			tags.close();
		}
	}
}
