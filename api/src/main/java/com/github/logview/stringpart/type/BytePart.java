package com.github.logview.stringpart.type;

import com.github.logview.stringpart.api.PartFactory;
import com.github.logview.stringpart.api.PartType;
import com.github.logview.stringpart.base.AbstractPart;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class BytePart extends AbstractPart {
	private final byte data;

	public BytePart(byte data, PartFactory factory) {
		super(PartType.BYTE, factory);
		this.data = data;
	}

	public BytePart(ByteArrayDataInput data, PartFactory factory) {
		this(data.readByte(), factory);
	}

	public BytePart(String data, PartFactory factory) {
		this(Byte.parseByte(data), factory);
	}

	@Override
	public void writeData(ByteArrayDataOutput out) {
		out.writeByte(data);
	}

	@Override
	public Byte value() {
		return data;
	}
}
