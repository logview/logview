package com.github.logview.api;

import com.github.logview.regex.RegexMultiMatcher;
import com.github.logview.stringpart.api.Part;
import com.github.logview.stringpart.api.PartCreator;
import com.github.logview.stringpart.api.PartType;
import com.github.logview.stringpart.type.RefPart;
import com.github.logview.stringpart.type.TagListPart;
import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class TagsImpl implements Tags {
	private final PartManager manager;
	private final PartCreator stringFactory;
	private final RegexMultiMatcher patterns;
	private final String prefix;
	private final RefManager refs;

	private final LoadingCache<String, Part> tagCache = CacheBuilder.newBuilder().maximumSize(1000)
			.build(new CacheLoader<String, Part>() {
				@Override
				public Part load(String tag) throws Exception {
					System.err.printf("new tag: %s:%s\n", prefix, tag);
					return stringFactory.createInstance(tag);
				}
			});

	private final LoadingCache<String, Part> regexTagCache = CacheBuilder.newBuilder().maximumSize(1000)
			.build(new CacheLoader<String, Part>() {
				@Override
				public Part load(String tag) throws Exception {
					Part ret = patterns == null ? null : patterns.match(tag);
					if(ret == null) {
						return tagCache.get(tag);
					}
					return ret;
				}
			});

	private final LoadingCache<String, Part> tagsCache = CacheBuilder.newBuilder().maximumSize(1000)
			.build(new CacheLoader<String, Part>() {
				@Override
				public Part load(String tags) throws Exception {
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
					return new RefPart(refs.add(ret), manager);
				}
			});

	public TagsImpl(RegexMultiMatcher patterns, PartManager manager, String prefix) {
		this.patterns = patterns;
		this.manager = manager;
		this.refs = manager.getRefs();
		this.prefix = prefix;
		stringFactory = manager.getFactory().getCreator(PartType.STRING);
	}

	public TagsImpl(PartManager manager, String prefix) {
		this(null, manager, prefix);
	}

	@Override
	public Part getTag(String tag) {
		if(Strings.isNullOrEmpty(tag)) {
			return null;
		}
		return tagCache.getUnchecked(tag);
	}

	@Override
	public Part getTags(String tags) {
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
	}
}
