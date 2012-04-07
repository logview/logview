package com.github.logview.stringpart.type;

import com.github.logview.stringpart.api.PartFactory;
import com.github.logview.stringpart.api.PartType;
import com.github.logview.stringpart.base.AbstractPart;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class IntegerPart extends AbstractPart {
	private final int data;

	public IntegerPart(int data, PartFactory factory) {
		super(PartType.INT, factory);
		this.data = data;
	}

	public IntegerPart(ByteArrayDataInput data, PartFactory factory) {
		this(data.readInt(), factory);
	}

	public IntegerPart(String data, PartFactory factory) {
		this(Integer.parseInt(data), factory);
	}

	@Override
	public void writeData(ByteArrayDataOutput out) {
		out.writeInt(data);
	}

	@Override
	public Integer value() {
		return data;
	}
}
