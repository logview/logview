package com.github.logview.api;

import com.github.logview.stringpart.api.PartFactory;

public class PartManager {
	private final RefManager refs;
	private final PartFactory factory;

	public PartManager() {
		refs = new RefManager();
		factory = new PartFactory(this);
	}

	public RefManager getRefs() {
		return refs;
	}

	public PartFactory getFactory() {
		return factory;
	}
}
