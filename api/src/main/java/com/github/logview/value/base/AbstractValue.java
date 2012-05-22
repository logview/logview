package com.github.logview.value.base;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.logview.params.AbstractParams;
import com.github.logview.params.Params;
import com.github.logview.util.Util;
import com.github.logview.value.api.Value;
import com.github.logview.value.api.ValueParams;

public abstract class AbstractValue extends AbstractParams implements Value {
	private final String regex;
	private final Pattern analyser;
	private final boolean useForAnalyse;
	private final boolean inline;
	protected final Params params;

	public AbstractValue(String regex, Params params) {
		this.regex = regex;
		this.params = params;
		this.useForAnalyse = params.getParamAsBoolean(ValueParams.ANALYSE);
		this.inline = params.getParamAsBoolean(ValueParams.INLINE);
		if(inline) {
			this.analyser = Pattern.compile("(" + this.regex + ")");
		} else {
			this.analyser = Pattern.compile("([^a-zA-Z_0-9]|^)(" + this.regex + ")([^a-zA-Z_0-9]|$)");
		}
	}

	@Override
	public String getParamAsStringOrNull(ValueParams key) {
		return params.getParamAsStringOrNull(key);
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
	public List<String> getParamAsList(ValueParams key) {
		return params.getParamAsList(key);
	}

	private String getReplaceString() {
		String ret = getType() + Util.escape(getExtra());
		if(inline) {
			return "\\$(" + ret + ")";
		} else {
			return "$1\\$(" + ret + ")$3";
		}
	}

	@Override
	public String analyse(String string) {
		String ret = string;
		while(true) {
			Matcher m = analyser.matcher(ret);
			if(!m.find()) {
				return ret;
			}
			ret = m.replaceFirst(getReplaceString());
		}
	}

	public String analyseOneStep(String string) {
		Matcher m = analyser.matcher(string);
		if(m.find()) {
			return m.replaceAll(getReplaceString());
		}
		return string;
	}

	@Override
	public boolean useForAnalyse() {
		return useForAnalyse;
	}

	@Override
	public String getRegex() {
		return regex;
	}

}
