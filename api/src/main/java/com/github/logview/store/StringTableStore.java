package com.github.logview.store;

import com.github.logview.stringtable.StringTable;

public class StringTableStore extends StoreWrapper {
	private final StringTable st;

	public StringTableStore(StringTable st, Store wrapped) {
		super(wrapped);
		this.st = st;
	}

	@Override
	public void storeString(String s) {
		int i = st.addString(s);
		wrapped.storeInteger(i);
//		System.err.printf("storeStringRef:%08X '%s'\n", i, s);
	}

	@Override
	public void close() {
//		System.err.printf("%dmb bytes without strings...\n", size() / 1024 / 1024);
		st.store(new StoreWrapper(this) {
			@Override
			public void storeString(String s) {
				wrapped.storeString(s);
			}
		});
//		System.err.printf("%dmb bytes with strings...\n", size() / 1024 / 1024);
	}
}
