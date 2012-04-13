package com.github.logview.params;

import java.util.Map;
import java.util.Set;

import com.github.logview.value.api.ValueParams;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

public class ParamsDefinition {
	private final static Set<ValueParams> emptySet = ImmutableSet.of();
	private final static Map<ValueParams, String> emptyMap = ImmutableMap.of();
	private final Set<ValueParams> allowed;
	private final Set<ValueParams> required;
	private final Map<ValueParams, String> defaults;

	public ParamsDefinition() {
		this(emptyMap, emptySet, emptySet);
	}

	public ParamsDefinition(Map<ValueParams, String> defaults) {
		this(defaults, emptySet, emptySet);
	}

	public ParamsDefinition(Set<ValueParams> allowed) {
		this(emptyMap, allowed, emptySet);
	}

	public ParamsDefinition(Map<ValueParams, String> defaults, Set<ValueParams> allowed) {
		this(defaults, allowed, emptySet);
	}

	public ParamsDefinition(Map<ValueParams, String> defaults, Set<ValueParams> allowed, Set<ValueParams> required) {
		Set<ValueParams> a = Sets.newLinkedHashSet();
		a.addAll(defaults.keySet());
		a.addAll(allowed);
		a.addAll(required);
		this.allowed = ImmutableSet.copyOf(a);
		this.required = ImmutableSet.copyOf(required);
		this.defaults = ImmutableMap.copyOf(defaults);
	}

	public Params create(Map<ValueParams, String> data) {
		if(!allowed.containsAll(data.keySet())) {
			throw new IllegalArgumentException("found not allowed keys!");
		}
		if(!data.keySet().containsAll(required)) {
			throw new IllegalArgumentException("missing required key!");
		}
		return new ParamsImp(this, ImmutableMap.copyOf(data));
	}

	public String getDefault(ValueParams key) {
		String ret = defaults.get(key);
		if(ret == null) {
			throw new RuntimeException("key '" + key + "' not found!");
		}
		return ret;
	}
}
