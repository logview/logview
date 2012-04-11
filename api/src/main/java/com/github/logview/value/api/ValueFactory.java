package com.github.logview.value.api;

import java.util.Set;

import com.github.logview.value.type.BooleanValue;
import com.github.logview.value.type.DoubleValue;
import com.github.logview.value.type.LongValue;
import com.github.logview.value.type.SessionHostValue;
import com.github.logview.value.type.SessionValue;
import com.github.logview.value.type.UuidValue;
import com.google.common.collect.ImmutableSet.Builder;

public final class ValueFactory implements ValueOf, ValueAnalyser {
	private final static Set<Value> types = new Builder<Value>()
	//
			.add(new BooleanValue()) //
			.add(new BooleanValue("TRUE", "FALSE")) //
			.add(new SessionHostValue(false)) //
			.add(new SessionHostValue(true)) //
			.add(new SessionValue(false)) //
			.add(new SessionValue(true)) //
			.add(new UuidValue(false)) //
			.add(new UuidValue(true)) //
			.add(new DoubleValue(false)) //
			.add(new DoubleValue(true)) //
			.add(new LongValue()) //
			.build();

	private final static ValueFactory instance = new ValueFactory();

	private ValueFactory() {
	}

	public static ValueFactory getInstance() {
		return instance;
	}

	@Override
	public String analyse(String string) {
		String ret = string;
		for(Value type : types) {
			ret = type.analyse(ret);
		}
		return ret;
	}

	@Override
	public Object valueOf(String string) {
		for(Value type : types) {
			Object ret = type.valueOf(string);
			if(ret != null) {
				return ret;
			}
		}
		return string;
	}
}
