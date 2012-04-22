package com.github.logview.value.type;

import java.util.Map;

import com.github.logview.params.Params;
import com.github.logview.value.api.ValueParams;
import com.github.logview.value.api.ValueType;
import com.github.logview.value.base.AbstractRegexValue;

public class IpValue extends AbstractRegexValue {
	public final static ValueType TYPE = ValueType.IP;

	public IpValue(Map<ValueParams, String> data) {
		this(TYPE.create(data));
	}

	private IpValue(Params params) {
		super("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}", params);
	}

	@Override
	public ValueType getType() {
		return TYPE;
	}
}
