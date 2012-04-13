package com.github.logview.value.base;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.logview.params.AbstractParams;
import com.github.logview.params.Params;
import com.github.logview.value.api.Value;
import com.github.logview.value.api.ValueParams;

public abstract class AbstractValue extends AbstractParams implements Value {
	private final String regex;
	private final Pattern analyser;
	protected final Params params;

	public AbstractValue(String regex, Params params) {
		this.regex = regex;
		this.params = params;
		this.analyser = Pattern.compile("([^a-zA-Z_0-9]|^)(" + this.regex + ")([^a-zA-Z_0-9]|$)");
	}

	@Override
	public String getParamAsString(ValueParams key) {
		return params.getParamAsString(key);
	}

	@Override
	public String getExtra() {
		return params.toString();
	}

	@Override
	public Set<ValueParams> getParams() {
		return params.getParams();
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

	@Override
	public String getRegex() {
		return regex;
	}
}
