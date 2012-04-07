package com.github.logview.stringpart.type;

import com.github.logview.stringpart.api.PartFactory;
import com.github.logview.stringpart.api.PartType;
import com.github.logview.stringpart.base.AbstractPart;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class StringPart extends AbstractPart {
	private final String data;

	public StringPart(String data, PartFactory factory) {
		super(PartType.STRING, factory);
		this.data = data;
	}

	public StringPart(ByteArrayDataInput data, PartFactory factory) {
		this(data.readUTF(), factory);
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
