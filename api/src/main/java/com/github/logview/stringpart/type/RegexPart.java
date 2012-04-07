package com.github.logview.stringpart.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import com.github.logview.regex.Config;
import com.github.logview.stringpart.api.Part;
import com.github.logview.stringpart.api.PartFactory;
import com.github.logview.stringpart.api.PartType;
import com.github.logview.stringpart.base.AbstractListPart;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class RegexPart extends AbstractListPart {
	private final Config config;
	private final ArrayList<Part> parts = new ArrayList<Part>();

	public RegexPart(String config, PartFactory factory) {
		this(Config.create(config, factory), factory);
	}

	public RegexPart(Config config, PartFactory factory) {
		this(config, new LinkedList<Part>(), factory);
	}

	public RegexPart(Config config, Collection<Part> parts, PartFactory factory) {
		super(PartType.REGEX, factory);
		this.config = config;
		parts.addAll(parts);
	}

	public RegexPart(ByteArrayDataInput data, PartFactory factory) {
		super(PartType.REGEX, factory);
		this.config = Config.create(data.readUTF(), factory);
	}

	@Override
	public void getBytes(ByteArrayDataOutput out) {
		if(config.size() != parts.size()) {
			throw new IllegalArgumentException("RegexPart is not filled!");
		}
		super.getBytes(out);
		out.writeUTF(config.toString());
	}

	@Override
	public Collection<Part> getParts() {
		return new LinkedList<Part>(parts);
	}

	@Override
	public int size() {
		return config.size();
	}

	public void add(Part part) {
		if(parts.size() >= config.size()) {
			throw new IllegalArgumentException("RegexPart is full!");
		}
		parts.add(part);
	}

	@Override
	public Part getPart(int index) {
		return parts.get(index);
	}

	public Config getConfig() {
		return config;
	}

	@Override
	public Object value() {
		String format = config.getFormat();
		if(format != null) {
			Object[] params = parts.toArray();
			for(int i = 0; i < params.length; i++) {
				params[i] = config.format(i, (Part)params[i]);
			}
			return String.format(format, params);
		}
		StringBuilder sb = new StringBuilder();
		int to = size();
		for(int i = 0; i < to; i++) {
			sb.append(config.getFiller(i));
			sb.append(getValue(i));
		}
		sb.append(config.getFiller(to));
		return sb.toString();
	}

	public void set(int index, Part part) {
		parts.set(index, part);
	}

	public Part getPartByName(String name) {
		return getPart(config.getIdByName(name));
	}

	public int getIdByName(String name) {
		return config.getIdByName(name);
	}
}
