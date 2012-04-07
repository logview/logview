package com.github.logview.stringpart.type;

import com.github.logview.stringpart.api.PartFactory;
import com.github.logview.stringpart.api.PartType;
import com.github.logview.stringpart.base.AbstractPart;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class LongPart extends AbstractPart {
	private final long data;

	public LongPart(long data, PartFactory factory) {
		super(PartType.LONG, factory);
		this.data = data;
	}

	public LongPart(ByteArrayDataInput data, PartFactory factory) {
		this(data.readLong(), factory);
	}

	public LongPart(String data, PartFactory factory) {
		this(Long.parseLong(data), factory);
	}

	@Override
	public void writeData(ByteArrayDataOutput out) {
		out.writeLong(data);
	}

	@Override
	public Long value() {
		return data;
	}
}
