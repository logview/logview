package com.github.logview.value.api;

import java.util.Set;

import com.github.logview.params.ParamsDefinition;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public interface ValueParamsDefinitions {
	final static Set<ValueParams> globalParams = ImmutableSet.of( //
		ValueParams.NAME, //
		ValueParams.TAGS, //
		ValueParams.ANALYSE, //
		ValueParams.INLINE, //
		ValueParams.TO);

	final static ImmutableSet<ValueParams> emptyValueParams = ImmutableSet.of();

	final static ParamsDefinition emptyParams = new ParamsDefinition();

	final static ParamsDefinition booleanParams = new ParamsDefinition(ImmutableMap.of( //
		ValueParams.TRUE, ValueValues.TRUE, //
		ValueParams.FALSE, ValueValues.FALSE));

	final static ParamsDefinition uppercaseParams = new ParamsDefinition(ImmutableMap.of( //
		ValueParams.UPPERCASE, ValueValues.FALSE));

	final static ParamsDefinition doubleParams = new ParamsDefinition(ImmutableMap.of( //
		ValueParams.DOT, ValueValues.TRUE));

	final static ParamsDefinition dateParams = new ParamsDefinition(ImmutableMap.of( //
		ValueParams.LOCALE, ValueValues.DEFAULT_LOCALE), //
			ImmutableSet.of(ValueParams.ZONE), //
			ImmutableSet.of(ValueParams.FORMAT));

	final static ParamsDefinition stringParams = new ParamsDefinition(ImmutableMap.of( //
		ValueParams.FORMAT, ValueValues.FORMAT_STRING, //
		ValueParams.ANALYSE, ValueValues.FALSE, //
		ValueParams.TRIM, ValueValues.TRUE), //
			ImmutableSet.of(ValueParams.MIN), emptyValueParams);
}
