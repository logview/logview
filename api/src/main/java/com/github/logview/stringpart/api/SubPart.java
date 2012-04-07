package com.github.logview.stringpart.api;

import java.util.Collection;

public interface SubPart extends Part {
	Collection<Part> getParts();

	Object getValue(int index);

	Part getPart(int index);

	int size();
}
