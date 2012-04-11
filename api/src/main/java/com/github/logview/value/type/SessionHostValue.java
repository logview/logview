package com.github.logview.value.type;

import com.github.logview.value.base.AbstractCaseRegexValue;

public class SessionHostValue extends AbstractCaseRegexValue {
	public SessionHostValue(boolean uppercase) {
		super("[0-9A-F]{32}\\.[0-9a-z\\-]+", "[0-9a-f]{32}\\.[0-9a-z\\-]+", uppercase);
	}

	@Override
	public String getType() {
		return "SESSIONHOST";
	}
}
