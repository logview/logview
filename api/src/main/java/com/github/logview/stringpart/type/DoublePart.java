package com.github.logview.stringpart.type;

import com.github.logview.api.PartManager;
import com.github.logview.stringpart.api.PartType;
import com.github.logview.stringpart.base.AbstractPart;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class DoublePart extends AbstractPart {
	private final double data;

	public DoublePart(double data, PartManager manager) {
		super(PartType.DOUBLE, manager);
		this.data = data;
	}

	public DoublePart(ByteArrayDataInput data, PartManager manager) {
		this(data.readDouble(), manager);
	}

	public DoublePart(String data, PartManager manager) {
		this(Double.parseDouble(data), manager);
	}

	@Override
	public void writeData(ByteArrayDataOutput out) {
		out.writeDouble(data);
	}

	@Override
	public Double value() {
		return data;
	}
}
