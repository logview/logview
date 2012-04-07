package com.github.logview.regex;

import java.text.ParseException;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;

import com.github.logview.stringpart.api.Part;
import com.github.logview.stringpart.type.RegexPart;

public class RegexConfigMatcher implements RegexMatcher {
	private final Config config;

	public RegexConfigMatcher(Config config) {
		this.config = config;
	}

	@Override
	public Part match(String line) throws ParseException, ExecutionException {
		Matcher pm = config.match(line);
		if(!pm.find()) {
			return null;
		}
		final int max = config.size();
		final int count = pm.groupCount();
		if(count != 0 && count != max) {
			throw new IllegalArgumentException("wrong group count!");
		}
		if(count == 0) {
			return config.createPart(0, pm.group(0));
		}
		RegexPart ret = new RegexPart(config, config.getManager());
		for(int i = 0; i < max; i++) {
			ret.add(config.createPart(i, pm.group(i + 1)));
		}
		return ret;
	}

	public Config getConfig() {
		return config;
	}
}
