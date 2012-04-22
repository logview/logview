package com.github.logview.value.api;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.logview.matcher.Match;
import com.github.logview.util.Util;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public final class ValueFactory implements ValueOf, ValueAnalyser {
	private final static LoadingCache<String, Pattern> compileCache = CacheBuilder.newBuilder().weakValues()
			.build(new CacheLoader<String, Pattern>() {
				@Override
				public Pattern load(String regex) throws Exception {
					return Pattern.compile(regex);
				}
			});

	private final LoadingCache<String, Pattern> patternCache = CacheBuilder.newBuilder().weakValues()
			.build(new CacheLoader<String, Pattern>() {
				@Override
				public Pattern load(String regex) throws Exception {
					return compileCache.get(toRegex(regex, false));
				}
			});

	private final LoadingCache<String, Pattern> patternCacheEscape = CacheBuilder.newBuilder().weakValues()
			.build(new CacheLoader<String, Pattern>() {
				@Override
				public Pattern load(String regex) throws Exception {
					return compileCache.get(toRegex(regex, true));
				}
			});

	private final AtomicReference<Map<String, Value>> types = new AtomicReference<Map<String, Value>>(
			new LinkedHashMap<String, Value>());

	private final static Pattern token = Pattern.compile("\\$\\(([^\\)]*?)\\)");

	private final static ValueFactory instance;

	static {
		instance = new ValueFactory();
		instance.loadDefaults();
	}

	public static ValueFactory createDefault() {
		return new ValueFactory(instance);
	}

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

	public void load(String type) {
		String[] t = type.split(" ");
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
			if(!entry.isGeneric()) {
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
			Matcher m = token.matcher(left);
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
		return parse(getPattern(match, escape), match, string);
	}

	public Match parse(Pattern pattern, String match, String string) {
		Matcher m = pattern.matcher(string);
		if(m.matches()) {
			List<Object> data = Lists.newLinkedList();
			Matcher ma = token.matcher(match);
			for(int i = 0; i < m.groupCount(); i++) {
				if(!ma.find()) {
					throw new IllegalArgumentException("no match found!");
				}
				data.add(getType(ma.group(1)).valueOf(m.group(i + 1)));
			}
			return new Match(this, match, string, data);
		}
		return null;
	}

	public Pattern getPattern(String match, boolean escape) {
		if(escape) {
			return patternCacheEscape.getUnchecked(match);
		} else {
			return patternCache.getUnchecked(match);
		}
	}

	public void reset() {
		synchronized(types) {
			types.set(new LinkedHashMap<String, Value>());
		}
	}

	public String toString(String match, List<Object> data) {
		String ret = match;
		Iterator<Object> it = data.iterator();
		while(true) {
			Matcher m = token.matcher(ret);
			if(!m.find()) {
				if(it.hasNext()) {
					throw new IllegalArgumentException("too many arguments!");
				}
				return Util.removeRegexSpaces(ret);
			}
			if(!it.hasNext()) {
				throw new IllegalArgumentException("too few arguments!");
			}
			ret = m.replaceFirst(Util.escape(Util.escape(getType(m.group()).format(it.next()))));
		}
	}

	public Value getType(int index, String match) {
		Matcher ma = token.matcher(match);
		if(ma.find(index + 1)) {
			return getType(ma.group(1));
		}
		throw new IllegalArgumentException();
	}
}
