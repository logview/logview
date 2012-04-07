package com.github.logview.regex;

import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import com.github.logview.api.PartManager;
import com.github.logview.stringpart.api.Part;

public class RegexMultiMatcher implements RegexMatcher {
	private final Map<String, RegexConfigMatcher> map = new LinkedHashMap<String, RegexConfigMatcher>();

	public RegexMultiMatcher() {
	}

	public RegexMultiMatcher(String data, PartManager manager) {
		for(String config : data.split("\n")) {
			String[] kv = config.split("|", 2);
			map.put(kv[0], new RegexConfigMatcher(new Config(kv[1], manager)));
		}
	}

	public RegexMultiMatcher(Properties props, PartManager manager) {
		for(Object key : props.keySet()) {
			String prefix = (String)key;
			if(prefix.indexOf('.') != -1) {
				continue;
			}
			map.put(prefix, new RegexConfigMatcher(Config.load(prefix, props, manager)));
		}
	}

	public RegexConfigMatcher get(String key) {
		return map.get(key);
	}

	public Set<String> keySet() {
		return map.keySet();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for(String key : map.keySet()) {
			if(first == true) {
				first = false;
			} else {
				sb.append('\n');
			}
			sb.append(key);
			sb.append('|');
			sb.append(map.get(key).getConfig().toString());
		}
		return sb.toString();
	}

	@Override
	public Part match(String line) throws ParseException, ExecutionException {
		for(Entry<String, RegexConfigMatcher> e : map.entrySet()) {
			Part mr = e.getValue().match(line);
			if(mr != null) {
				return mr;
			}
		}
		return null;
	}
}
