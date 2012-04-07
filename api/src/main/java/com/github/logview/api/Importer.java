package com.github.logview.api;

import java.io.File;
import java.util.Scanner;
import java.util.prefs.Preferences;

import com.github.logview.regex.RegexMatcher;
import com.github.logview.regex.RegexMultiMatcher;
import com.github.logview.stringpart.api.Part;
import com.github.logview.util.AutomaticScanner;
import com.github.logview.util.Util;

public class Importer {
	public static void load(String prefix, PartManager manager, Loader loader) throws Exception {
		Preferences prefs = Preferences.userNodeForPackage(Importer.class);
		String path = prefs.get("path", null);
		if(path == null) {
			throw new RuntimeException("please set path pref!");
		}
		load(prefix, path, manager, loader);
	}

	public static void load(String key, String path, PartManager manager, Loader loader) throws Exception {
		RegexMultiMatcher parser = new RegexMultiMatcher(
				Util.loadProps(Importer.class, "../settings/format.properties"), manager);
		Importer.loadPath(parser.get(key), path, loader);
	}

	public static void loadPath(RegexMatcher parser, String path, Loader loader) throws Exception {
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

	public static void loadLog(RegexMatcher parser, File file, Appender appender, LogFile logfile) throws Exception {
		Part entry = null;
		Scanner scanner = AutomaticScanner.create(file);

		try {
			final long a = System.currentTimeMillis();
			long bytes = 0;
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				bytes += line.length();
				if(bytes >= 1024 * 1024 * 100) {
					break;
				}
				Part current = parser.match(line);
				if(current != null) {
					if(entry != null) {
						// entryNo++;
						appender.append(logfile, entry);
					}
					entry = current;
				} else if(entry != null) {
					// entry.addLine(line);
				}
			}

			System.err.printf("%d ms\n", System.currentTimeMillis() - a);
			if(entry != null) {
				appender.append(logfile, entry);
			}
		} finally {
			scanner.close();
		}
	}
}
