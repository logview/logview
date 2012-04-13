package com.github.logview.params;

import java.util.Set;

import com.github.logview.value.api.ValueParams;

public interface Params {
	String getParamAsString(ValueParams key);

	boolean getParamAsBoolean(ValueParams key);

	String getParamsAsString();

	Set<ValueParams> getParams();
}
