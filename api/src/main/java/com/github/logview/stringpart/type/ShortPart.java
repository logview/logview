package com.github.logview.stringpart.type;

import com.github.logview.stringpart.api.PartFactory;
import com.github.logview.stringpart.api.PartType;
import com.github.logview.stringpart.base.AbstractPart;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class ShortPart extends AbstractPart {
	private final short data;

	public ShortPart(short data, PartFactory factory) {
		super(PartType.SHORT, factory);
		this.data = data;
	}

	public ShortPart(ByteArrayDataInput data, PartFactory factory) {
		this(data.readShort(), factory);
	}

	public ShortPart(String data, PartFactory factory) {
		this(Short.parseShort(data), factory);
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
