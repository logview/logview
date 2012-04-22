package com.github.logview.value.type;

import java.util.Map;

import com.github.logview.params.Params;
import com.github.logview.value.api.ValueParams;
import com.github.logview.value.api.ValueType;
import com.github.logview.value.base.AbstractRegexValue;

public class LongValue extends AbstractRegexValue {
	private final static ValueType type = ValueType.LONG;

	public LongValue(Map<ValueParams, String> data) {
		this(type.create(data));
	}

	private LongValue(Params params) {
		super("\\-?\\d+", params);
	}

	@Override
	public ValueType getType() {
		return type;
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
	public String format(Object data) {
		if(data instanceof Long) {
			return Long.toString((Long)data);
		}
		throw new IllegalArgumentException();
	}
}
