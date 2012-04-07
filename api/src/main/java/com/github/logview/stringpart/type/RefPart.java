package com.github.logview.stringpart.type;

import com.github.logview.stringpart.api.PartFactory;
import com.github.logview.stringpart.api.PartType;
import com.github.logview.stringpart.base.AbstractPart;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class RefPart extends AbstractPart {
	private final int data;

	public RefPart(int data, PartFactory factory) {
		super(PartType.REF, factory);
		this.data = data;
	}

	public RefPart(ByteArrayDataInput data, PartFactory factory) {
		this(data.readInt(), factory);
	}

	public RefPart(String data, PartFactory factory) {
		this(Integer.parseInt(data), factory);
	}

	@Override
	public void writeData(ByteArrayDataOutput out) {
		out.writeInt(data);
	}

	@Override
	public String debug() {
		return String.format("%s(%8x)", type, data);
	}

	@Override
	public Integer value() {
		return data;
	}
}
