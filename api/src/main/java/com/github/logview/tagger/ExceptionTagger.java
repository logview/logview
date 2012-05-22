package com.github.logview.tagger;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.logview.api.LogEntry;
import com.google.common.base.Joiner;
import com.google.common.base.Supplier;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;

public class ExceptionTagger extends AbstractTagger {
	private final List<Map<String, Object>> list = Lists.newLinkedList();

	public ExceptionTagger(String tag) {
		super("exception", tag);
	}

	@Override
	public int size() {
		synchronized(list) {
			return list.size();
		}
	}

	@Override
	public void add(Map<String, Object> extra) {
		synchronized(list) {
			list.add(extra);
		}
	}

	@Override
	public void close() {
		super.close();

		LoadingCache<String, AtomicInteger> counter1 = CacheBuilder.newBuilder().build(
			new CacheLoader<String, AtomicInteger>() {
				@Override
				public AtomicInteger load(String key) throws Exception {
					return new AtomicInteger();
				}
			});
		LoadingCache<String, AtomicInteger> counter2 = CacheBuilder.newBuilder().build(
			new CacheLoader<String, AtomicInteger>() {
				@Override
				public AtomicInteger load(String key) throws Exception {
					return new AtomicInteger();
				}
			});
		SetMultimap<String, String> messages = Multimaps.newSetMultimap( //
			new TreeMap<String, Collection<String>>(), //
			new Supplier<Set<String>>() {
				@Override
				public Set<String> get() {
					return Sets.newHashSet();
				}
			});
		synchronized(list) {
			for(Map<String, Object> map : list) {
				String msg = (String)map.get("message");
				if(msg != null) {
					String st = Joiner.on('\n').join(((LogEntry)map.get("entry")).getLines());
					counter1.getUnchecked(msg).incrementAndGet();
					counter2.getUnchecked(st).incrementAndGet();
					messages.put(msg, st);
				}
			}
		}
		for(String key : messages.keySet()) {
			System.err.printf("'%s' (%d times)\n", key, counter1.getUnchecked(key).get());
			for(String s : messages.get(key)) {
				System.err.printf(" **** STACKTRACE: (%d times)\n", counter2.getUnchecked(s).get());
				System.err.println(s);
			}
		}
	}
}
