package com.github.logview.value.type;

import com.github.logview.value.base.AbstractRegexValue;

public class LongValue extends AbstractRegexValue {
	public LongValue() {
		super("\\-?\\d+");
	}

	@Override
	public String getType() {
		return "LONG";
	}

	@Override
	public Object valueOf(String string) {
		String[] match = match(string);
		if(match != null) {
			return Long.valueOf(match[0]);
		}
		return null;
	}

	@Override
	public String getExtra() {
		return "";
	}
}
