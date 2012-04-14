package com.github.logview.value.type;

import java.util.Map;

import com.github.logview.params.Params;
import com.github.logview.value.api.ValueParams;
import com.github.logview.value.api.ValueType;
import com.github.logview.value.base.AbstractValue;

public class BooleanValue extends AbstractValue {
	private final static ValueType type = ValueType.BOOLEAN;

	private final String t;
	private final String f;

	public BooleanValue(Map<ValueParams, String> data) {
		this(type.create(data));
	}

	private BooleanValue(Params params) {
		super(params.getParamAsString(ValueParams.TRUE) + "|" + params.getParamAsString(ValueParams.FALSE), params);
		this.t = params.getParamAsString(ValueParams.TRUE);
		this.f = params.getParamAsString(ValueParams.FALSE);
	}

	@Override
	public ValueType getType() {
		return type;
	}

	@Override
	public Object valueOf(String string) {
		if(t.equals(string)) {
			return true;
		} else if(f.equals(string)) {
			return false;
		}
		return null;
	}

	@Override
	public String analyse(String string) {
		return analyseOneStep(string);
	}

	@Override
	public String format(Object data) {
		if(data instanceof Boolean) {
			return Boolean.toString((Boolean)data);
		}
		throw new IllegalArgumentException();
	}
}
