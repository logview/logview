package com.github.logview.api;

import java.util.LinkedList;
import java.util.List;

import com.github.logview.stringpart.api.Part;

public/*abstract*/class MatchResult {
//	public final int entryNo;
//	public final int lineNo;
//	public final int id;
//	public final Long[] keys;
//	public final Object[] values;
	private final LinkedList<String> lines = new LinkedList<String>();
	private final LinkedList<Part> parts = new LinkedList<Part>();

	public MatchResult(/*int entryNo, int lineNo, * /int id, int max*/) {
//		this.entryNo = entryNo;
//		this.lineNo = lineNo;
//		this.id = id;

//		 keys = new Long[max + 1];
//		 values = new Object[max];
	}

	public void addLine(String line) {
		lines.add(line);
	}

	public List<String> lines() {
		return lines;
	}

//	public int getEntryNo() {
//		return entryNo;
//	}
//
//	public int getLineNo() {
//		return lineNo;
//	}

	/*
	public abstract Set<String> keySet();

	public abstract Object getObject(String key);

	public abstract Long getStringId(String key);

	public abstract String getString(StringTable strings, String key);

	public Date getDate(String key) {
		return (Date)getObject(key);
	}

	public abstract String toString(StringTable strings);

	public abstract byte[] getBytes();
	*/

	public void add(Part part) {
		parts.add(part);
	}

}
