package com.github.logview.value.type;

import com.github.logview.value.base.AbstractCaseRegexValue;

public class SessionValue extends AbstractCaseRegexValue {
	public SessionValue(boolean uppercase) {
		super("[0-9A-F]{32}", uppercase);
	}

	@Override
	public String getType() {
		return "SESSION";
	}
}
