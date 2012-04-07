package com.github.logview.stringpart.type;

import java.util.Collection;
import java.util.LinkedHashSet;

import com.github.logview.api.PartManager;
import com.github.logview.stringpart.api.Part;
import com.github.logview.stringpart.api.PartType;
import com.github.logview.stringpart.base.AbstractListPart;
import com.google.common.collect.Sets;
import com.google.common.io.ByteArrayDataInput;

public class ListPart extends AbstractListPart {
	private final LinkedHashSet<Part> parts = Sets.newLinkedHashSet();

	public ListPart(PartManager factory) {
		this(PartType.LIST, factory);
	}

	protected ListPart(PartType type, PartManager manager) {
		super(type, manager);
	}

	public ListPart(ByteArrayDataInput data, PartManager manager) {
		this(PartType.LIST, data, manager);
	}

	protected ListPart(PartType type, ByteArrayDataInput data, PartManager manager) {
		this(type, manager);
		int to = data.readByte();
		for(int i = 0; i < to; i++) {
			add(factory.createFrom(data));
		}
	}

	public ListPart(String data, PartManager manager) {
		this(manager);
		throw new RuntimeException("new ListPart(data) is not implemented!");
	}

	@Override
	public Collection<Part> getParts() {
		return parts;
	}

	public void add(Part part) {
		int s = parts.size();
		if(s < 0 || s > 0xFF) {
			throw new IllegalStateException("list is to long!");
		}
		parts.add(part);
	}

	@Override
	public int size() {
		return parts.size();
	}

	@Override
	public Part getPart(int index) {
		throw new RuntimeException("getPart(index) is not implemented!");
	}

	@Override
	public Object value() {
		throw new RuntimeException("value() is not implemented!");
	}

	@Override
	public String debug() {
		StringBuilder sb = new StringBuilder();
		sb.append("LIST(");
		for(Part p : parts) {
			sb.append(p.debug());
		}
		sb.append(")");
		return sb.toString();
	}
}
