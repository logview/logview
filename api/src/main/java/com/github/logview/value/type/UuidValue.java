package com.github.logview.value.type;

import java.util.UUID;

import com.github.logview.value.base.AbstractCaseRegexValue;

public class UuidValue extends AbstractCaseRegexValue {
	public UuidValue(boolean uppercase) {
		super("[0-9A-F]{8}\\-[0-9A-F]{4}\\-[0-9A-F]{4}\\-[0-9A-F]{4}\\-[0-9A-F]{12}", uppercase);
	}

	@Override
	public String getType() {
		return "UUID";
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
