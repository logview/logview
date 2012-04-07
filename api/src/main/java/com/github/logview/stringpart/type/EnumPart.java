package com.github.logview.stringpart.type;

import com.github.logview.api.PartManager;
import com.github.logview.stringpart.api.PartType;
import com.github.logview.stringpart.base.AbstractPart;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class EnumPart extends AbstractPart {
	private final byte data;

	public EnumPart(byte data, PartManager manager) {
		super(PartType.ENUM, manager);
		this.data = data;
	}

	public EnumPart(ByteArrayDataInput data, PartManager manager) {
		this(data.readByte(), manager);
	}

	public EnumPart(String data, PartManager manager) {
		this(Byte.parseByte(data), manager);
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
