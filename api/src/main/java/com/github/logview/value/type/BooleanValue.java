package com.github.logview.value.type;

import com.github.logview.value.base.AbstractValue;

public class BooleanValue extends AbstractValue {
	private final String t;
	private final String f;

	public BooleanValue() {
		this("true", "false");
	}

	public BooleanValue(String t, String f) {
		super(t + "|" + f);
		this.t = t;
		this.f = f;
	}

	@Override
	public String getType() {
		return "BOOLEAN";
	}

	@Override
	public Object valueOf(String string) {
		if(t.equals(string)) {
			return true;
		} else if(f.equals(string)) {
			return false;
		}
		return null;
	}

	@Override
	public String getExtra() {
		StringBuilder sb = new StringBuilder();
		if(!"true".equals(t)) {
			sb.append(" true:");
			sb.append(t);
		}
		if(!"false".equals(f)) {
			sb.append(" false:");
			sb.append(f);
		}
		return sb.toString();
	}

	@Override
	public String analyse(String string) {
		return analyseOneStep(string);
	}
}
