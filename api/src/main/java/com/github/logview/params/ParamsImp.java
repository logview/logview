package com.github.logview.params;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.logview.value.api.ValueParams;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ParamsImp extends AbstractParams {
	private final ParamsDefinition definition;
	private final Map<ValueParams, String> data;
	private final Map<ValueParams, List<String>> lists = Maps.newHashMap();

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

	@Override
	public List<String> getParamAsList(ValueParams key) {
		synchronized(lists) {
			List<String> ret = lists.get(key);
			if(ret == null) {
				ret = Lists.newLinkedList();
				String entrys = getParamAsStringOrNull(key);
				if(entrys != null) {
					for(String entry : entrys.split(",")) {
						ret.add(entry);
					}
				}
				lists.put(key, ret);
			}
			return ret;
		}
	}
}
