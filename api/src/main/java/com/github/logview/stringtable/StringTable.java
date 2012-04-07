package com.github.logview.stringtable;

import com.github.logview.stringpart.api.Part;
import com.github.logview.util.DataOutputFactory;
import com.google.common.io.ByteArrayDataOutput;

public abstract class StringTable implements DataOutputFactory {
	public abstract int addString(String string);

	public abstract void touchString(String string);

	public abstract void touchString(int id);

	public abstract String getString(int id);

	public abstract void flush();

	public abstract void listStrings();

	public byte[] toByteArray(Part p) {
		StringTableByteArrayDataOutput out = new StringTableByteArrayDataOutput(this);
		p.getBytes(out);
		return out.toByteArray();
	}

	@Override
	public ByteArrayDataOutput create() {
		return new StringTableByteArrayDataOutput(this);
	}
}
