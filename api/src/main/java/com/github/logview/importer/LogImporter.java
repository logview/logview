package com.github.logview.importer;

import com.github.logview.matcher.PatternMatcher;
import com.github.logview.util.Util;
import com.github.logview.value.api.ValueFactory;

public class LogImporter {
	public static void main2(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		String mach = Util.loadString(Importers.class, "../settings/format.properties", "default");
		PatternMatcher matcher = new PatternMatcher(ValueFactory.createDefault(), mach, false);
		MultiThreadLoader loader = new MultiThreadLoader(matcher, new LogEntryTask());
		Importers.loadPath(loader, loader, ".");
		System.err.printf("%d ms\n", System.currentTimeMillis() - start);
	}
}
