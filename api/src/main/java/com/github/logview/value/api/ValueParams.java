package com.github.logview.value.api;

public enum ValueParams {
	TRUE,
	FALSE,
	DOT,
	UPPERCASE,
	FORMAT,
	NAME,
	TRIM,
	MIN,
	TAGS,
	LOCALE,
	ZONE,
	//
	;

	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}
}
