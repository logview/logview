package com.github.logview.value.base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.logview.params.Params;
import com.github.logview.util.Util;

public abstract class AbstractRegexValue extends AbstractValue {
	private final Pattern matcher;

	protected AbstractRegexValue(String regex, Params params) {
		super(Util.removeGroups(regex), params);
		this.matcher = Pattern.compile("^" + regex + "$");
	}

	protected String[] match(String string) {
		if(string == null) {
			return null;
		}
		Matcher m = matcher.matcher(string);
		if(m.matches()) {
			String[] ret = new String[m.groupCount() + 1];
			for(int i = 0; i < ret.length; i++) {
				ret[i] = m.group(i);
			}
			return ret;
		}
		return null;
	}

	@Override
	public Object valueOf(String string) {
		String[] match = match(string);
		if(match != null) {
			return match[0];
		}
		return null;
	}
}
