package com.github.logview.params;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.github.logview.value.api.ValueParams;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
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

	public ParamsDefinition(Set<ValueParams> required) {
		this(emptyMap, emptySet, required);
	}

	public ParamsDefinition(Map<ValueParams, String> defaults, Set<ValueParams> required) {
		this(defaults, emptySet, required);
	}

	public ParamsDefinition(Map<ValueParams, String> defaults, Set<ValueParams> allowed, Set<ValueParams> required) {
		Set<ValueParams> a = Sets.newLinkedHashSet();
		a.addAll(defaults.keySet());
		a.addAll(allowed);
		a.addAll(required);
		a.add(ValueParams.NAME);
		a.add(ValueParams.TAGS);
		this.allowed = ImmutableSet.copyOf(a);
		this.required = ImmutableSet.copyOf(required);
		LinkedHashMap<ValueParams, String> d = Maps.newLinkedHashMap(defaults);
		d.put(ValueParams.TRIM, "true");
		this.defaults = ImmutableMap.copyOf(d);
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
		return defaults.get(key);
	}
}
