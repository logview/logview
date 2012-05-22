package com.github.logview.stringtable;

import com.github.logview.store.Storeable;

public interface StringTable extends Storeable {
	int addString(String string);

	void touchString(String string);

	void touchString(int id);

	String getString(int id);

	void flush();

	void listStrings();
}
