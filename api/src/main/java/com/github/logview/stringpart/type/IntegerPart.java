package com.github.logview.stringpart.type;

import com.github.logview.api.PartManager;
import com.github.logview.stringpart.api.PartType;
import com.github.logview.stringpart.base.AbstractPart;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class IntegerPart extends AbstractPart {
	private final int data;

	public IntegerPart(int data, PartManager manager) {
		super(PartType.INT, manager);
		this.data = data;
	}

	public IntegerPart(ByteArrayDataInput data, PartManager manager) {
		this(data.readInt(), manager);
	}

	public IntegerPart(String data, PartManager manager) {
		this(Integer.parseInt(data), manager);
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
