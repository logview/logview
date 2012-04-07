package com.github.logview.stringpart.api;

import com.github.logview.util.DataOutputFactory;
import com.google.common.io.ByteArrayDataOutput;

public interface Part {
	byte[] getBytes();

	byte[] getBytes(DataOutputFactory factory);

	void getBytes(ByteArrayDataOutput out);

	void writeData(ByteArrayDataOutput out);

	Object value();

	String debug();
}
