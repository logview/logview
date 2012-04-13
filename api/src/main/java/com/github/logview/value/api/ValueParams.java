package com.github.logview.value.api;

public enum ValueParams {
	TRUE,
	FALSE,
	DOT,
	UPPERCASE,
	//
	;

	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}
}