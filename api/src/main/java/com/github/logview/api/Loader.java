package com.github.logview.api;

import com.github.logview.importer.Importer;

public interface Loader extends Importer {
	void load();

	void close();
}
