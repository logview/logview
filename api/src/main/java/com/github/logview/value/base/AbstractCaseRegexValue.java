package com.github.logview.value.base;

import com.github.logview.params.Params;
import com.github.logview.util.Util;
import com.github.logview.value.api.ValueParams;

public abstract class AbstractCaseRegexValue extends AbstractRegexValue {
	private final boolean uppercase;

	public AbstractCaseRegexValue(String regex, Params params) {
		this(regex.toUpperCase(), regex.toLowerCase(), params);
	}

	public AbstractCaseRegexValue(String regex1, String regex2, Params params) {
		super(Util.removeGroups(params.getParamAsBoolean(ValueParams.UPPERCASE) ? regex1 : regex2), params);
		this.uppercase = params.getParamAsBoolean(ValueParams.UPPERCASE);
	}

	@Override
	public String getExtra() {
		if(!uppercase) {
			return "";
		}
		return " uppercase:true";
	}
}
