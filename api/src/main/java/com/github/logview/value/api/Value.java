package com.github.logview.value.api;

import com.github.logview.params.Params;

public interface Value extends ValueOf, ValueAnalyser, Params {
	ValueType getType();

	String getRegex();

	String getExtra();
}
