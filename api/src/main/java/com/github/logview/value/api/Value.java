package com.github.logview.value.api;

public interface Value extends ValueOf, ValueAnalyser {
	String getType();

	String getRegex();

	String getExtra();
}
