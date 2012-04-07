package com.github.logview.api;

public class TableNames {
	public final String log;
	public final String string2Id;
	public final String id2String;

	public TableNames(String log, String string2Id, String id2String) {
		this.log = log;
		this.string2Id = string2Id;
		this.id2String = id2String;
	}

	public TableNames extend(String prefix, String suffix) {
		return new TableNames(prefix + log + suffix, prefix + string2Id + suffix, prefix + id2String + suffix);
	}
}
