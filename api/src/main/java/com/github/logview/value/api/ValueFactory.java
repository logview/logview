package com.github.logview.value.api;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.logview.regex.Match;
import com.github.logview.util.Util;
import com.github.logview.value.type.BooleanValue;
import com.github.logview.value.type.DoubleValue;
import com.github.logview.value.type.LongValue;
import com.github.logview.value.type.SessionHostValue;
import com.github.logview.value.type.SessionValue;
import com.github.logview.value.type.UuidValue;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.Lists;

public final class ValueFactory implements ValueOf, ValueAnalyser {
	private final LoadingCache<String, Pattern> patternCache = CacheBuilder.newBuilder().weakValues()
			.build(new CacheLoader<String, Pattern>() {
				@Override
				public Pattern load(String regex) throws Exception {
					return Pattern.compile(toRegex(regex));
				}
			});

	private final static Set<Value> types = new Builder<Value>()
	//
			.add(new BooleanValue()) //
			.add(new BooleanValue("TRUE", "FALSE")) //
			.add(new SessionHostValue(false)) //
			.add(new SessionHostValue(true)) //
			.add(new SessionValue(false)) //
			.add(new SessionValue(true)) //
			.add(new UuidValue(false)) //
			.add(new UuidValue(true)) //
			.add(new DoubleValue(false)) //
			.add(new DoubleValue(true)) //
			.add(new LongValue()) //
			.build();

	private final static ValueFactory instance = new ValueFactory();

	private final static Pattern token = Pattern.compile("\\$\\([^\\)]*?\\)");

	private ValueFactory() {
	}

	public static ValueFactory getInstance() {
		return instance;
	}

	@Override
	public String analyse(String string) {
		String ret = string;
		for(Value type : types) {
			ret = type.analyse(ret);
		}
		return ret;
	}

	public String toRegex(String string) {
		if(string == null) {
			return null;
		}
		String ret = Util.escape(string);
		while(true) {
			Matcher m = token.matcher(ret);
			if(m.find()) {
				String g = m.group();
				g = g.substring(2, g.length() - 1);
				boolean found = false;
				for(Value type : types) {
					if(g.equals(type.getType() + type.getExtra())) {
						ret = m.replaceFirst("(" + Util.escapeReplace(type.getRegex()) + ")");
						found = true;
						break;
					}
				}
				if(found) {
					continue;
				}
			}
			break;
		}
		return ret;
	}

	@Override
	public Object valueOf(String string) {
		for(Value type : types) {
			Object ret = type.valueOf(string);
			if(ret != null) {
				return ret;
			}
		}
		return string;
	}

	public Match parse(String match, String string) {
		Matcher m = getPattern(match).matcher(string);
		if(m.matches()) {
			List<Object> data = Lists.newLinkedList();
			for(int i = 0; i < m.groupCount(); i++) {
				data.add(valueOf(m.group(i + 1)));
			}
			return new Match(match, data);
		}
		return null;
	}

	public Pattern getPattern(String match) {
		return patternCache.getUnchecked(match);
	}
}
