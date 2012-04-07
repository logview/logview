package com.github.logview.stringpart.type;

import com.github.logview.api.PartManager;
import com.github.logview.stringpart.api.Part;
import com.github.logview.stringpart.api.PartType;
import com.github.logview.stringpart.base.AbstractPart;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class RefPart extends AbstractPart {
	private final int data;

	public RefPart(int data, PartManager manager) {
		super(PartType.REF, manager);
		this.data = data;
	}

	public RefPart(ByteArrayDataInput data, PartManager manager) {
		this(data.readInt(), manager);
	}

	public RefPart(String data, PartManager manager) {
		this(Integer.parseInt(data), manager);
	}

	@Override
	public void writeData(ByteArrayDataOutput out) {
		out.writeInt(data);
	}

	@Override
	public String debug() {
		return String.format("%s(%8x)", type, data);
	}

	@Override
	public Part value() {
		return manager.getRefs().get(data);
	}
}
