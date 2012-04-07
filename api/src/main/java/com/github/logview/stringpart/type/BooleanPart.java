package com.github.logview.stringpart.type;

import com.github.logview.api.PartManager;
import com.github.logview.stringpart.api.PartType;
import com.github.logview.stringpart.base.AbstractPart;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class BooleanPart extends AbstractPart {
	private final boolean data;

	public BooleanPart(boolean data, PartManager manager) {
		super(PartType.SHORT, manager);
		this.data = data;
	}

	public BooleanPart(ByteArrayDataInput data, PartManager manager) {
		this(data.readBoolean(), manager);
	}

	public BooleanPart(String data, PartManager manager) {
		this(Boolean.parseBoolean(data), manager);
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
