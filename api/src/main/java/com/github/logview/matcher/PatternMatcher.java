package com.github.logview.matcher;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.logview.store.Store;
import com.github.logview.util.Util;
import com.github.logview.value.api.Value;
import com.github.logview.value.api.ValueFactory;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class PatternMatcher implements LineMatcher {
	private final ValueFactory factory;
	public final Pattern pattern;
	public final String match;
	private final List<Value> types;
	private final boolean escape;

	public PatternMatcher(ValueFactory factory, String match, boolean escape) {
		this.factory = factory;
		this.escape = escape;
		this.match = match;
		this.pattern = factory.getPattern(match, escape);

		Matcher ma = Util.matchToken(match);
		LinkedList<Value> types = Lists.newLinkedList();
		while(ma.find()) {
			types.add(factory.getType(ma.group(1)));
		}
		this.types = ImmutableList.copyOf(types);
	}

	@Override
	public Match match(String line) {
		Matcher m = pattern.matcher(line);
		if(m.matches()) {
			List<Object> data = Lists.newLinkedList();
			int i = 0;
			for(Value value : getTypes()) {
				i++;
				data.add(value.valueOf(m.group(i)));
			}
			return new Match(factory, this, line, data);
		}
		return null;
	}

	public List<Value> getTypes() {
		return types;
	}

	@Override
	public void store(Store store) {
		store.storeBoolean(escape);
		store.storeString(match);
	}

	public void storeData(Store store, List<Object> data) {
//		System.err.println("storeData");
		store.storeString(match);
		store.storeByte(data.size());
		for(Object o : data) {
			store.storeObject(o);
		}
	}
}
