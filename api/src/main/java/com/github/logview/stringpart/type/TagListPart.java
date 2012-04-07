package com.github.logview.stringpart.type;

import java.util.Collection;
import java.util.LinkedHashSet;

import com.github.logview.api.PartManager;
import com.github.logview.regex.RegexMultiMatcher;
import com.github.logview.stringpart.api.Part;
import com.github.logview.stringpart.api.PartType;
import com.github.logview.stringpart.base.AbstractListPart;
import com.github.logview.stringpart.base.AbstractPart;
import com.google.common.collect.Sets;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class TagListPart extends AbstractListPart {
	private final LinkedHashSet<Part> parts = Sets.newLinkedHashSet();
	private final RegexMultiMatcher patterns;

	public TagListPart(ByteArrayDataInput data, PartManager manager) {
		this(new RegexMultiMatcher(data.readUTF(), manager), manager);
		int to = data.readByte();
		for(int i = 0; i < to; i++) {
			add(factory.createFrom(data));
		}
	}

	public TagListPart(String data, PartManager manager) {
		this(new RegexMultiMatcher(data, manager), manager);
	}

	public TagListPart(RegexMultiMatcher patterns, PartManager manager) {
		super(PartType.TAGLIST, manager);
		this.patterns = patterns;
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
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for(Part p : parts) {
			if(first) {
				first = false;
			} else {
				sb.append(' ');
			}
			sb.append(p.toString());
		}
		return sb.toString();
	}

	@Override
	public void getBytes(ByteArrayDataOutput out) {
		out.writeByte(getType());
		out.writeUTF(patterns.toString());
		Collection<Part> parts = getParts();
		out.writeByte(parts.size());

		for(Part part : parts) {
			boolean written = false;
			if(part instanceof RegexPart) {
				RegexPart rp = (RegexPart)part;
				String config = rp.getConfig().toString();
				for(int i = 0, to = rp.size(); i < to; i++) {
					int id = 2;
					for(String key : patterns.keySet()) {
						if(config.equals(patterns.get(key).getConfig().toString())) {
							out.writeByte(id);
							byte[] child = rp.getPart(i).getBytes();
							// skip type
							out.write(child, 1, child.length - 1);
							written = true;
						}
						id++;
					}
				}
			} else if(part instanceof StringPart) {
				out.writeByte(1);
				out.writeUTF((String)part.value());
				written = true;
			}
			if(written) {
				continue;
			}
			out.writeByte(0);
			if(part instanceof AbstractPart) {
				((AbstractPart)part).getBytes(out);
			} else {
				out.write(part.getBytes());
			}
		}
	}

	@Override
	public String debug() {
		StringBuilder sb = new StringBuilder();
		sb.append("TAGLIST[");
		sb.append(patterns.toString());
		sb.append("](");
		for(Part p : parts) {
			sb.append(p.debug());
		}
		sb.append(")");
		return sb.toString();
	}
}
