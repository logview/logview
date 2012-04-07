package com.github.logview.stringpart.base;

import com.github.logview.api.PartManager;
import com.github.logview.stringpart.api.Part;
import com.github.logview.stringpart.api.PartFactory;
import com.github.logview.stringpart.api.PartType;
import com.github.logview.util.DataOutputFactory;
import com.github.logview.util.DefaultDataOutputFactory;
import com.google.common.io.ByteArrayDataOutput;

public abstract class AbstractPart implements Part {
	protected final PartType type;
	protected final PartFactory factory;
	protected final PartManager manager;

	protected AbstractPart(PartType type, PartManager manager) {
		this.type = type;
		this.manager = manager;
		this.factory = manager.getFactory();
	}

	@Override
	public byte[] getBytes() {
		return getBytes(DefaultDataOutputFactory.instance());
	}

	@Override
	public byte[] getBytes(DataOutputFactory factory) {
		ByteArrayDataOutput out = factory.create();
		getBytes(out);
		return out.toByteArray();
	}

	@Override
	public void getBytes(ByteArrayDataOutput out) {
		out.writeByte(getType());
		writeData(out);
	}

	public byte getType() {
		return (byte)type.ordinal();
	}

	@Override
	public String toString() {
		return value().toString();
	}

	@Override
	public String debug() {
		return type.toString() + "(" + value().toString() + ")";
	}
}
