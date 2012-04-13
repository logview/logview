package com.github.logview.value.api;

import com.github.logview.params.ParamsDefinition;
import com.google.common.collect.ImmutableMap;

public interface ValueParamsDefinitions {
	final static ParamsDefinition emptyParams = new ParamsDefinition();

	final static ParamsDefinition booleanParams = new ParamsDefinition(ImmutableMap.of( //
		ValueParams.TRUE, ValueValues.TRUE, //
		ValueParams.FALSE, ValueValues.FALSE));

	final static ParamsDefinition uppercaseParams = new ParamsDefinition(ImmutableMap.of( //
		ValueParams.UPPERCASE, ValueValues.FALSE));

	final static ParamsDefinition doubleParams = new ParamsDefinition(ImmutableMap.of( //
		ValueParams.DOT, ValueValues.TRUE));
}
