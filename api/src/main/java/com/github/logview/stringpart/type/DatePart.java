package com.github.logview.stringpart.type;

import java.util.Date;

import com.github.logview.stringpart.api.PartFactory;
import com.github.logview.stringpart.api.PartType;
import com.github.logview.stringpart.base.AbstractPart;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class DatePart extends AbstractPart {
	private final long data;

	public DatePart(long data, PartFactory factory) {
		super(PartType.DATE, factory);
		this.data = data;
	}

	public DatePart(Date data, PartFactory factory) {
		this(data.getTime(), factory);
	}

	public DatePart(ByteArrayDataInput data, PartFactory factory) {
		this(data.readLong(), factory);
	}

	public DatePart(String data, PartFactory factory) {
		this(Long.parseLong(data), factory);
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
