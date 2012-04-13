package com.github.logview.value.type;

import java.util.Map;

import com.github.logview.params.Params;
import com.github.logview.value.api.ValueParams;
import com.github.logview.value.api.ValueType;
import com.github.logview.value.base.AbstractCaseRegexValue;

public class SessionHostValue extends AbstractCaseRegexValue {
	private final static ValueType type = ValueType.SESSIONHOST;

	public SessionHostValue(Map<ValueParams, String> data) {
		this(type.create(data));
	}

	private SessionHostValue(Params params) {
		super("[0-9A-F]{32}\\.[0-9a-z\\-]+", "[0-9a-f]{32}\\.[0-9a-z\\-]+", params);
	}

	@Override
	public ValueType getType() {
		return type;
	}
}
