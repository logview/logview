package com.github.logview.matcher;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import com.github.logview.store.Store;
import com.github.logview.store.Storeable;
import com.github.logview.util.Util;
import com.github.logview.value.api.Value;
import com.github.logview.value.api.ValueFactory;
import com.github.logview.value.api.ValueParams;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

public class Match implements Storeable {
	private final ValueFactory factory;
	private final PatternMatcher match;
	private final String source;
	private final List<Object> data;

	public Match(ValueFactory factory, PatternMatcher match, String source) {
		this.factory = factory;
		this.match = match;
		this.source = source;
		this.data = ImmutableList.of();
	}

	public Match(ValueFactory factory, PatternMatcher match, String source, Collection<Object> data) {
		this.factory = factory;
		this.match = match;
		this.source = source;
		this.data = ImmutableList.copyOf(data);
	}

	public Match(ValueFactory factory, String source) {
		throw new IllegalAccessError();
	}

	public PatternMatcher getMatch() {
		return match;
	}

	public Map<Value, Object> getValueMap() {
		Map<Value, Object> ret = Maps.newLinkedHashMap();
		List<Value> types = match.getTypes();
		if(data.size() != types.size()) {
			throw new RuntimeException();
		}
		for(int i = 0; i < types.size(); i++) {
			ret.put(types.get(i), data.get(i));
		}
		return ret;
	}

	public Map<String, Object> getNameMap() {
		Map<String, Object> ret = Maps.newLinkedHashMap();
		List<Value> types = match.getTypes();
		if(data.size() != types.size()) {
			throw new RuntimeException();
		}
		for(int i = 0; i < types.size(); i++) {
			String name = types.get(i).getParamAsStringOrNull(ValueParams.NAME);
			if(name != null) {
				ret.put(name, data.get(i));
			}
		}
		return ret;
	}

	public Object getValue(int index) {
		return data.get(index);
	}

	public int size() {
		return data.size();
	}

	@Override
	public String toString() {
		String ret = match.match;
		Iterator<Object> it = data.iterator();
		while(true) {
			Matcher m = Util.matchToken(ret);
			if(!m.find()) {
				if(it.hasNext()) {
					throw new IllegalArgumentException("too many arguments!");
				}
				return Util.removeRegexSpaces(ret);
			}
			if(!it.hasNext()) {
				throw new IllegalArgumentException("too few arguments!");
			}
			ret = m.replaceFirst(Util.escape(Util.escape(factory.getType(m.group()).format(it.next()))));
		}
	}

	public String toString(int index) {
		return factory.getType(index, match.match).format(data.get(index));
	}

	public String getSource() {
		return source;
	}

	@Override
	public void store(Store store) {
		match.storeData(store, data);
	}
}
