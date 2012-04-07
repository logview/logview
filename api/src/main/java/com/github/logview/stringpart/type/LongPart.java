package com.github.logview.stringpart.type;

import com.github.logview.api.PartManager;
import com.github.logview.stringpart.api.PartType;
import com.github.logview.stringpart.base.AbstractPart;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class LongPart extends AbstractPart {
	private final long data;

	public LongPart(long data, PartManager manager) {
		super(PartType.LONG, manager);
		this.data = data;
	}

	public LongPart(ByteArrayDataInput data, PartManager manager) {
		this(data.readLong(), manager);
	}

	public LongPart(String data, PartManager manager) {
		this(Long.parseLong(data), manager);
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
