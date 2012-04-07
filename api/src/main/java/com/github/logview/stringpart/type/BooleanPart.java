package com.github.logview.stringpart.type;

import com.github.logview.stringpart.api.PartFactory;
import com.github.logview.stringpart.api.PartType;
import com.github.logview.stringpart.base.AbstractPart;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class BooleanPart extends AbstractPart {
	private final boolean data;

	public BooleanPart(boolean data, PartFactory factory) {
		super(PartType.SHORT, factory);
		this.data = data;
	}

	public BooleanPart(ByteArrayDataInput data, PartFactory factory) {
		this(data.readBoolean(), factory);
	}

	public BooleanPart(String data, PartFactory factory) {
		this(Boolean.parseBoolean(data), factory);
	}

	@Override
	public void writeData(ByteArrayDataOutput out) {
		out.writeBoolean(data);
	}

	@Override
	public Boolean value() {
		return data;
	}
}
