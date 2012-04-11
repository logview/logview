package com.github.logview.value.type;

import com.github.logview.value.base.AbstractRegexValue;

public class DoubleValue extends AbstractRegexValue {
	private final boolean dot;

	public DoubleValue(boolean dot) {
		super("(\\-?)(\\d+)" + (dot ? "\\." : ",") + "(\\d+)");
		this.dot = dot;
	}

	@Override
	public String getType() {
		return "DOUBLE";
	}

	@Override
	public Object valueOf(String string) {
		String[] match = match(string);
		if(match != null) {
			return Double.valueOf(String.format("%s%s.%s", match[1], match[2], match[3]));
		}
		return null;
	}

	@Override
	public String getExtra() {
		if(dot) {
			return "";
		}
		return " dot:false";
	}
}
