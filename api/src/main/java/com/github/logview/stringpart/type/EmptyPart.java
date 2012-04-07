package com.github.logview.stringpart.type;

import com.github.logview.api.PartManager;
import com.github.logview.stringpart.api.PartType;
import com.github.logview.stringpart.base.AbstractPart;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class EmptyPart extends AbstractPart {
	public EmptyPart(PartManager manager) {
		super(PartType.EMPTY, manager);
	}

	public EmptyPart(ByteArrayDataInput data, PartManager manager) {
		this(manager);
	}

	public EmptyPart(String data, PartManager manager) {
		this(manager);
	}

	@Override
	public Object value() {
		return "";
	}

	@Override
	public void writeData(ByteArrayDataOutput out) {
	}

	@Override
	public String debug() {
		return "EMPTY()";
	}
}
