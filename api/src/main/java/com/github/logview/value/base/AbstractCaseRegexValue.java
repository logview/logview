package com.github.logview.value.base;

import com.github.logview.util.Util;

public abstract class AbstractCaseRegexValue extends AbstractRegexValue {
	private final boolean uppercase;

	public AbstractCaseRegexValue(String regex, boolean uppercase) {
		this(regex.toUpperCase(), regex.toLowerCase(), uppercase);
	}

	public AbstractCaseRegexValue(String regex1, String regex2, boolean uppercase) {
		super(Util.removeGroups(uppercase ? regex1 : regex2));
		this.uppercase = uppercase;
	}

	@Override
	public String getExtra() {
		if(!uppercase) {
			return "";
		}
		return " uppercase:true";
	}
}
