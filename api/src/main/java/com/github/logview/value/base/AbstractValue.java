package com.github.logview.value.base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.logview.value.api.Value;

public abstract class AbstractValue implements Value {
	private final String regex;
	private final Pattern analyser;

	public AbstractValue(String regex) {
		this.regex = regex;
		this.analyser = Pattern.compile("([^a-zA-Z_0-9]|^)(" + this.regex + ")([^a-zA-Z_0-9]|$)");
	}

	@Override
	public String analyse(String string) {
		String ret = string;
		while(true) {
			Matcher m = analyser.matcher(ret);
			if(!m.find()) {
				return ret;
			}
			ret = m.replaceFirst("$1\\$(" + getType() + getExtra() + ")$3");
		}
	}

	public String analyseOneStep(String string) {
		Matcher m = analyser.matcher(string);
		if(m.find()) {
			return m.replaceAll("$1\\$(" + getType() + getExtra() + ")$3");
		}
		return string;
	}
}
