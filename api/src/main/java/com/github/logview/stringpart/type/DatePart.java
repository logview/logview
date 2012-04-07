package com.github.logview.stringpart.type;

import java.util.Date;

import com.github.logview.api.PartManager;
import com.github.logview.stringpart.api.PartType;
import com.github.logview.stringpart.base.AbstractPart;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class DatePart extends AbstractPart {
	private final long data;

	public DatePart(long data, PartManager manager) {
		super(PartType.DATE, manager);
		this.data = data;
	}

	public DatePart(Date data, PartManager manager) {
		this(data.getTime(), manager);
	}

	public DatePart(ByteArrayDataInput data, PartManager manager) {
		this(data.readLong(), manager);
	}

	public DatePart(String data, PartManager manager) {
		this(Long.parseLong(data), manager);
	}

	@Override
	public void writeData(ByteArrayDataOutput out) {
		out.writeLong(data);
	}

	@Override
	public Date value() {
		return new Date(data);
	}
}
