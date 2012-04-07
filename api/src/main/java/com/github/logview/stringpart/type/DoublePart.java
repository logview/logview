package com.github.logview.stringpart.type;

import com.github.logview.stringpart.api.PartFactory;
import com.github.logview.stringpart.api.PartType;
import com.github.logview.stringpart.base.AbstractPart;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class DoublePart extends AbstractPart {
	private final double data;

	public DoublePart(double data, PartFactory factory) {
		super(PartType.DOUBLE, factory);
		this.data = data;
	}

	public DoublePart(ByteArrayDataInput data, PartFactory factory) {
		this(data.readDouble(), factory);
	}

	public DoublePart(String data, PartFactory factory) {
		this(Double.parseDouble(data), factory);
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
