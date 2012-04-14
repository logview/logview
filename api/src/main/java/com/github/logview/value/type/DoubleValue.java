package com.github.logview.value.type;

import java.util.Map;

import com.github.logview.params.Params;
import com.github.logview.value.api.ValueParams;
import com.github.logview.value.api.ValueType;
import com.github.logview.value.base.AbstractRegexValue;

public class DoubleValue extends AbstractRegexValue {
	private final static ValueType type = ValueType.DOUBLE;

	private final boolean dot;

	public DoubleValue(Map<ValueParams, String> data) {
		this(type.create(data));
	}

	private DoubleValue(Params params) {
		super("(\\-?)(\\d+)" + (params.getParamAsBoolean(ValueParams.DOT) ? "\\." : ",") + "(\\d+)", params);
		this.dot = params.getParamAsBoolean(ValueParams.DOT);
	}

	@Override
	public ValueType getType() {
		return type;
	}

	@Override
	public Object valueOf(String string) {
		String[] match = match(string);
		if(match != null) {
			return Double.valueOf(String.format("%s%s.%s", match[1], match[2], match[3]));
		}
		return null;
	}

	@Override
	public String getExtra() {
		if(dot) {
			return "";
		}
		return " dot:false";
	}

	@Override
	public String format(Object data) {
		if(data instanceof Double) {
			return Double.toString((Double)data);
		}
		throw new IllegalArgumentException();
	}
}
