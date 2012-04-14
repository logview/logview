package com.github.logview.params;

import java.util.Map;
import java.util.Set;

import com.github.logview.value.api.ValueParams;
import com.google.common.collect.ImmutableMap;

public class ParamsImp extends AbstractParams {
	private final ParamsDefinition definition;
	private final Map<ValueParams, String> data;

	public ParamsImp(ParamsDefinition definition, Map<ValueParams, String> data) {
		this.definition = definition;
		this.data = ImmutableMap.copyOf(data);
	}

	@Override
	public String getParamAsStringOrNull(ValueParams key) {
		String ret = data.get(key);
		if(ret == null) {
			ret = definition.getDefault(key);
		}
		return ret;
	}

	@Override
	public Set<ValueParams> getParams() {
		return data.keySet();
	}

	@Override
	public String toString() {
		return getParamsAsString();
	}
}
