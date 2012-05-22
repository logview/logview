package com.github.logview.value.api;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.logview.matcher.Match;
import com.github.logview.matcher.PatternMatcher;
import com.github.logview.util.Util;
import com.github.logview.value.cache.PatternCache;
import com.github.logview.value.cache.PatternMatcherCache;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public final class ValueFactory implements ValueOf, ValueAnalyser {
	private final AtomicReference<Map<String, Value>> types = new AtomicReference<Map<String, Value>>(
			new LinkedHashMap<String, Value>());

	private final static ValueFactory instance;
	private final static Map<String, String> aliase = Maps.newHashMap();

	static {
		try {
			aliase.putAll(Util.loadMap(ValueFactory.class, "../../settings/alias.properties"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		instance = new ValueFactory();
		instance.loadDefaults();
	}

	public static ValueFactory createDefault() {
		return new ValueFactory(instance);
	}

	private final PatternCache patternCache = new PatternCache(this);
	private final PatternMatcherCache patternMatcherCache = new PatternMatcherCache(this);

	public ValueFactory() {
	}

	private ValueFactory(ValueFactory copy) {
		types.get().putAll(copy.types.get());
	}

	public void loadDefaults() {
		try {
			load(ValueFactory.class, "../../settings/types.txt");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void load(Class<?> clazz, String resource) throws IOException {
		load(Util.loadList(ValueFactory.class, resource));
	}

	public void load(Collection<String> types) {
		for(String type : types) {
			load(type);
		}
	}

	public String alias(String string) {
		String[] t = string.split(" ", 2);
		String alias;
		synchronized(aliase) {
			alias = aliase.get(t[0]);
		}
		if(alias == null) {
			return string;
		}
		String[] a = alias.split(" ", 2);
		StringBuilder sb = new StringBuilder();
		sb.append(a[0]);
		sb.append(" analyse:true");
		if(a.length == 2) {
			sb.append(' ');
			sb.append(a[1]);
		}
		if(t.length == 2) {
			sb.append(' ');
			sb.append(t[1]);
		}
		return sb.toString();
	}

	public void load(String type) {
		String alias = alias(type);
		String[] t = alias.split(" ");
		ValueType vt = ValueType.valueOf(t[0]);
		Class<? extends Value> clazz = vt.getValueClass();
		Map<ValueParams, String> data = Maps.newLinkedHashMap();
		if(t.length != 1) {
			for(int i = 1; i < t.length; i++) {
				String[] kv = t[i].split(":", 2);
				if(kv.length != 2) {
					throw new IllegalArgumentException("missing ':' in 'key:value': '" + t[i] + "'");
				}
				data.put(ValueParams.valueOf(kv[0].toUpperCase()), Util.unescapeSpace(kv[1]));
			}
		}
		Value value;
		try {
			value = clazz.getConstructor(Map.class).newInstance(ImmutableMap.copyOf(data));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		synchronized(types) {
			Map<String, Value> tmp = Maps.newLinkedHashMap(types.get());
			tmp.put(type, value);
			types.set(tmp);
		}
	}

	@Override
	public String analyse(String string) {
		String ret = string;
		for(Value entry : types.get().values()) {
			if(entry.useForAnalyse()) {
				ret = entry.analyse(ret);
			}
		}
		return ret;
	}

	public String toRegex(String string, boolean escape) {
		if(string == null) {
			return null;
		}
		String left = string;
		StringBuilder sb = new StringBuilder();
		while(true) {
			Matcher m = Util.matchToken(left);
			if(m.find()) {
				if(escape) {
					sb.append(Util.escape(left.substring(0, m.start())));
				} else {
					sb.append(left.substring(0, m.start()));
				}
				left = left.substring(m.end());
				sb.append('(');
				sb.append(getType(m.group()).getRegex());
				sb.append(')');
				continue;
			}
			break;
		}
		if(escape) {
			sb.append(Util.escape(left));
		} else {
			sb.append(left);
		}
		return sb.toString();
	}

	public Value getType(String type) {
		String t = type;
		if(t.startsWith("$(") && t.endsWith(")")) {
			t = t.substring(2, t.length() - 1);
		}
		Value ret = types.get().get(t);
		if(ret == null) {
			load(t);
		}
		ret = types.get().get(t);
		if(ret == null) {
			throw new IllegalArgumentException("Type '" + t + "' not found!");
		}
		return ret;
	}

	@Override
	public Object valueOf(String string) {
		for(Value type : types.get().values()) {
			Object ret = type.valueOf(string);
			if(ret != null) {
				return ret;
			}
		}
		return string;
	}

	public Match parse(String match, String string, boolean escape) {
		// TODO REMOVE
		return getPatternMatcher(match, escape).match(string);
	}

	public Match parse(PatternMatcher pattern, String string) {
		// TODO REMOVE
		return pattern.match(string);
	}

	public Pattern getPattern(String match, boolean escape) {
		// TODO REMOVE
		return patternCache.getPattern(match, escape);
	}

	public PatternMatcher getPatternMatcher(String match, boolean escape) {
		// TODO REMOVE
		return patternMatcherCache.getPatternMatcher(match, escape);
	}

	public void reset() {
		synchronized(types) {
			types.set(new LinkedHashMap<String, Value>());
		}
	}

	public Value getType(int index, String match) {
		Matcher ma = Util.matchToken(match);
		if(ma.find(index + 1)) {
			return getType(ma.group(1));
		}
		throw new IllegalArgumentException();
	}

}
