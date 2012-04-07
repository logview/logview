package com.github.logview.stringpart.type;

import com.github.logview.stringpart.api.PartFactory;
import com.github.logview.stringpart.api.PartType;
import com.github.logview.stringpart.base.AbstractPart;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class EmptyPart extends AbstractPart {
	public EmptyPart(PartFactory factory) {
		super(PartType.EMPTY, factory);
	}

	public EmptyPart(ByteArrayDataInput data, PartFactory factory) {
		this(factory);
	}

	public EmptyPart(String data, PartFactory factory) {
		this(factory);
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
