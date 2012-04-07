package com.github.logview.stringpart.type;

import com.github.logview.api.PartManager;
import com.github.logview.stringpart.api.PartType;
import com.github.logview.stringpart.base.AbstractPart;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class StringPart extends AbstractPart {
	private final String data;

	public StringPart(String data, PartManager manager) {
		super(PartType.STRING, manager);
		this.data = data;
	}

	public StringPart(ByteArrayDataInput data, PartManager manager) {
		this(data.readUTF(), manager);
	}

	@Override
	public void writeData(ByteArrayDataOutput out) {
		out.writeUTF(data);
	}

	@Override
	public String value() {
		return data;
	}
}
