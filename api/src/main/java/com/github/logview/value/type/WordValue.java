package com.github.logview.value.type;

import java.util.Map;

import com.github.logview.params.Params;
import com.github.logview.value.api.ValueParams;
import com.github.logview.value.api.ValueType;
import com.github.logview.value.base.AbstractRegexValue;

public class WordValue extends AbstractRegexValue {
	private final static ValueType type = ValueType.WORD;

	public WordValue(Map<ValueParams, String> data) {
		this(type.create(data));
	}

	private WordValue(Params params) {
		super(params.getParamAsString(ValueParams.FORMAT), params);
	}

	@Override
	public ValueType getType() {
		return type;
	}

	@Override
	public String analyse(String string) {
		return analyseOneStep(string);
	}

	@Override
	public boolean isGeneric() {
		return true;
	}
}
