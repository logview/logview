package com.github.logview.stringtable;

import com.github.logview.util.ByteArrayDataOutputWrapper;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class StringTableByteArrayDataOutput extends ByteArrayDataOutputWrapper {
	private final StringTable strings;

	public StringTableByteArrayDataOutput(StringTable strings) {
		super(ByteStreams.newDataOutput());
		this.strings = strings;
	}

	public StringTableByteArrayDataOutput(StringTable strings, ByteArrayDataOutput wrapped) {
		super(wrapped);
		this.strings = strings;
	}

	@Override
	public void writeUTF(String s) {
		super.writeInt(strings.addString(s));
	}
}
