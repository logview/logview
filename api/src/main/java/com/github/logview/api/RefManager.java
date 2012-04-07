package com.github.logview.api;

import java.util.Random;

import com.github.logview.stringpart.api.Part;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class RefManager {
	private final Random random = new Random();
	private final BiMap<Integer, Part> refs = HashBiMap.create();
	private final BiMap<Part, Integer> inverse = refs.inverse();

	public int add(Part ref) {
		int rnd = random.nextInt();
		System.err.printf("ref: %8x=%s\n", rnd, ref.debug());
		refs.put(rnd, ref);
		return rnd;
	}

	public Part get(int value) {
		return refs.get(value);
	}
}
