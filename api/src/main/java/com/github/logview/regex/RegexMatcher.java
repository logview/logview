package com.github.logview.regex;

import java.text.ParseException;
import java.util.concurrent.ExecutionException;

import com.github.logview.stringpart.api.Part;

public interface RegexMatcher {
	Part match(String line) throws ParseException, ExecutionException;
}
