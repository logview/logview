package com.github.logview.api;

import java.io.File;
import java.util.List;
import java.util.Scanner;
import java.util.prefs.Preferences;

import com.github.logview.matcher.Match;
import com.github.logview.matcher.Matcher;
import com.github.logview.matcher.ValueMatcher;
import com.github.logview.util.AutomaticScanner;
import com.github.logview.util.Util;
import com.github.logview.value.api.ValueFactory;
import com.google.common.collect.Lists;

public class Importer {
	public static void load(ValueFactory factory, String prefix, Loader loader) throws Exception {
		Preferences prefs = Preferences.userNodeForPackage(Importer.class);
		String path = prefs.get("path", null);
		if(path == null) {
			throw new RuntimeException("please set path pref!");
		}
		load(factory, prefix, path, loader);
	}

	public static void load(ValueFactory factory, String key, String path, Loader loader) throws Exception {
		String mach = Util.loadString(Importer.class, "../settings/format.properties", key);
		Importer.loadPath(new ValueMatcher(factory, mach), path, loader);
	}

	public static void loadPath(Matcher parser, String path, Loader loader) throws Exception {
		File dir = new File(path);
		for(File file : dir.listFiles()) {
			if(!file.isFile()) {
				continue;
			}
			LogFile logfile = loader.load(file, 1332028800);
			loadLog(parser, file, loader, logfile);
			loader.save(logfile);
		}
	}

	public static void loadLog(Matcher parser, File file, Appender appender, LogFile logfile) throws Exception {
		Scanner scanner = AutomaticScanner.create(file);

		try {
			final long a = System.currentTimeMillis();
			long bytes = 0;
			Match last = null;
			List<String> lines = Lists.newLinkedList();
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				bytes += line.length();
				if(bytes >= 1024 * 1024 * 10) {
					break;
				}
				Match match = parser.match(line);
				if(match != null) {
					if(last != null) {
						appender.append(logfile, new Entry(last, lines));
						lines.clear();
					}
					last = match;
				} else {
					lines.add(line);
				}
			}

			System.err.printf("%d ms\n", System.currentTimeMillis() - a);
			if(last != null) {
				appender.append(logfile, new Entry(last, lines));
			}
		} finally {
			scanner.close();
		}
	}
}
