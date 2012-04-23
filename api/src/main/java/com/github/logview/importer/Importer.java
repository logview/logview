package com.github.logview.importer;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.prefs.Preferences;

import com.github.logview.api.Appender;
import com.github.logview.api.Loader;
import com.github.logview.api.LogFile;
import com.github.logview.io.Writer;
import com.github.logview.matcher.Matcher;
import com.github.logview.matcher.ValueMatcher;
import com.github.logview.task.TaskListener;
import com.github.logview.util.AutomaticScanner;
import com.github.logview.util.Util;
import com.github.logview.value.api.ValueFactory;

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
		Importer.loadPath(new ValueMatcher(factory, mach, false), path, loader);
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
		final AtomicBoolean aborted = new AtomicBoolean();

		final long start = System.currentTimeMillis();
		MultiThreadImporter mti = new MultiThreadImporter(parser, appender, logfile);
		mti.addListener(new TaskListener() {
			@Override
			public void notifyAborted() {
				aborted.set(true);
			}
		});
		Writer<String> writer = mti.getWriter();
		try {
			long id = 0;
			/*
			long bytes = 0;
			long mb = 0, lastmb = 0;
			*/
			while(scanner.hasNextLine() && !aborted.get()) {
				final String line = scanner.nextLine();
				/*
				bytes += line.length();
				if(bytes >= 1024 * 1024 * 1024) {
					break;
				}
				*/
				writer.write(id, line.trim());
				id++;
				/*
				mb = bytes / 1024 / 1024 / 100;
				if(mb != lastmb) {
					lastmb = mb;
					System.err.printf("read %d MB, %6.1fm lines: %s\n", bytes / 1024 / 1024, id / 1000.0 / 1000.0,
						mti.getStatus());
				}
				*/
			}
		} finally {
			System.err.printf("reading: %d ms\n", System.currentTimeMillis() - start);
			mti.close();
			scanner.close();
		}
	}
}
