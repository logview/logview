package com.github.logview.value.type;

import java.util.Map;
import java.util.UUID;

import com.github.logview.params.Params;
import com.github.logview.value.api.ValueParams;
import com.github.logview.value.api.ValueType;
import com.github.logview.value.base.AbstractCaseRegexValue;

public class UuidValue extends AbstractCaseRegexValue {
	public final static ValueType TYPE = ValueType.UUID;

	public UuidValue(Map<ValueParams, String> data) {
		this(TYPE.create(data));
	}

	private UuidValue(Params params) {
		super("[0-9A-F]{8}\\-[0-9A-F]{4}\\-[0-9A-F]{4}\\-[0-9A-F]{4}\\-[0-9A-F]{12}", params);
	}

	@Override
	public ValueType getType() {
		return TYPE;
	}

	@Override
	public Object valueOf(String string) {
		String[] match = match(string);
		if(match != null) {
			return UUID.fromString(match[0]);
		}
		return null;
	}
}
