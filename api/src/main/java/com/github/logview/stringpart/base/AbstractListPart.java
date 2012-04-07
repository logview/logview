package com.github.logview.stringpart.base;

import com.github.logview.stringpart.api.Part;
import com.github.logview.stringpart.api.PartFactory;
import com.github.logview.stringpart.api.PartType;
import com.github.logview.stringpart.api.SubPart;
import com.google.common.io.ByteArrayDataOutput;

public abstract class AbstractListPart extends AbstractPart implements SubPart {
	protected AbstractListPart(PartType type, PartFactory factory) {
		super(type, factory);
	}

	@Override
	public Object getValue(int index) {
		return getPart(index).value();
	}

	@Override
	public String debug() {
		StringBuffer sb = new StringBuffer();
		sb.append(getType());
		sb.append("(");
		for(int i = 0, to = size(); i < to; i++) {
			sb.append(getPart(i).debug());
		}
		sb.append(")");
		return sb.toString();
	}

	@Override
	public void writeData(ByteArrayDataOutput out) {
		out.writeByte(getParts().size());
		for(Part part : getParts()) {
			if(part instanceof AbstractPart) {
				((AbstractPart)part).getBytes(out);
			} else {
				out.write(part.getBytes());
			}
		}
	}
}
