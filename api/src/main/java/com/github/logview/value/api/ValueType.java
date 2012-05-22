package com.github.logview.value.api;

import java.util.Map;

import com.github.logview.params.Params;
import com.github.logview.params.ParamsDefinition;
import com.github.logview.value.type.BooleanValue;
import com.github.logview.value.type.DateValue;
import com.github.logview.value.type.DoubleValue;
import com.github.logview.value.type.IpValue;
import com.github.logview.value.type.LongValue;
import com.github.logview.value.type.SessionHostValue;
import com.github.logview.value.type.SessionValue;
import com.github.logview.value.type.StringValue;
import com.github.logview.value.type.UuidValue;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public enum ValueType {
	BOOLEAN(BooleanValue.class, ValueParamsDefinitions.booleanParams),
	DATE(DateValue.class, ValueParamsDefinitions.dateParams),
	DOUBLE(DoubleValue.class, ValueParamsDefinitions.doubleParams),
	IP(IpValue.class, ValueParamsDefinitions.emptyParams),
	LONG(LongValue.class, ValueParamsDefinitions.emptyParams),
	SESSIONHOST(SessionHostValue.class, ValueParamsDefinitions.uppercaseParams),
	SESSION(SessionValue.class, ValueParamsDefinitions.uppercaseParams),
	STRING(StringValue.class, ValueParamsDefinitions.stringParams),
	UUID(UuidValue.class, ValueParamsDefinitions.uppercaseParams),
	//
	;

	private final static Map<ValueType, Class<? extends Value>> types;
	static {
		Map<ValueType, Class<? extends Value>> map = Maps.newTreeMap();
		for(ValueType type : ValueType.values()) {
			map.put(type, type.getValueClass());
		}
		types = ImmutableMap.copyOf(map);
	}

	public static Map<ValueType, Class<? extends Value>> getTypes() {
		return types;
	}

	private final Class<? extends Value> valueClass;
	private final ParamsDefinition definition;

	ValueType(Class<? extends Value> valueClass, ParamsDefinition definition) {
		this.valueClass = valueClass;
		this.definition = definition;
	}

	public Params create(Map<ValueParams, String> data) {
		return definition.create(data);
	}

	public Class<? extends Value> getValueClass() {
		return valueClass;
	}
}
