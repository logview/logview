package com.github.logview.stringpart.type;

import com.github.logview.api.PartManager;
import com.github.logview.stringpart.api.PartType;
import com.github.logview.stringpart.base.AbstractPart;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class ShortPart extends AbstractPart {
	private final short data;

	public ShortPart(short data, PartManager manager) {
		super(PartType.SHORT, manager);
		this.data = data;
	}

	public ShortPart(ByteArrayDataInput data, PartManager manager) {
		this(data.readShort(), manager);
	}

	public ShortPart(String data, PartManager manager) {
		this(Short.parseShort(data), manager);
	}

	@Override
	public void writeData(ByteArrayDataOutput out) {
		out.writeShort(data);
	}

	@Override
	public Short value() {
		return data;
	}
}
