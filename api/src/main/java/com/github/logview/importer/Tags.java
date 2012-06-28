package com.github.logview.importer;

import java.util.Set;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Sets;

public class Tags {
	private final LoadingCache<String, LoadingCache<String, Set<Integer>>> tags = CacheBuilder.newBuilder().build(
		new CacheLoader<String, LoadingCache<String, Set<Integer>>>() {
			@Override
			public LoadingCache<String, Set<Integer>> load(final String tag) throws Exception {
				// System.err.println("new tag " + tag);
				return CacheBuilder.newBuilder().build(new CacheLoader<String, Set<Integer>>() {
					@Override
					public Set<Integer> load(String name) throws Exception {
						// System.err.println("new tag " + tag + ":" + name);
						return Sets.newLinkedHashSet();
					}
				});
			}
		});

	public void tag(String tag, String name, int line) {
		// System.err.printf("%s:%s@%d\n", tag, name, line);
		LoadingCache<String, Set<Integer>> cache;
		synchronized(tags) {
			cache = tags.getUnchecked(tag);
		}
		synchronized(cache) {
			cache.getUnchecked(name).add(line);
		}
	}
}
