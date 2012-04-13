package com.github.logview.value.type;

import java.util.Map;

import com.github.logview.params.Params;
import com.github.logview.value.api.ValueParams;
import com.github.logview.value.api.ValueType;
import com.github.logview.value.base.AbstractCaseRegexValue;

public class SessionValue extends AbstractCaseRegexValue {
	private final static ValueType type = ValueType.SESSION;

	public SessionValue(Map<ValueParams, String> data) {
		this(type.create(data));
	}

	private SessionValue(Params params) {
		super("[0-9A-F]{32}", params);
	}

	@Override
	public ValueType getType() {
		return type;
	}
}
